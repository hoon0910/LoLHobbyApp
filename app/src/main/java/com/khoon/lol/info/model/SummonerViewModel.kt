package com.khoon.lol.info.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khoon.lol.info.BuildConfig
import com.khoon.lol.info.R
import com.khoon.lol.info.data.api.LeagueOfLegendAPI
import com.khoon.lol.info.data.api.MockLoLApi
import com.khoon.lol.info.data.repository.ChampionRepository
import com.khoon.lol.info.di.DispatcherModule
import com.khoon.lol.info.utils.constant.AppConstant.API_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SummonerViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val riotApiService: LeagueOfLegendAPI,
    private val mockLoLApi: MockLoLApi,
    private val championRepository: ChampionRepository,
) : ViewModel() {

    private val _result = MutableStateFlow<String>("")
    val result = _result.asStateFlow()

    private val _accountResult = MutableStateFlow("")
    val accountResult = _accountResult.asStateFlow()

    private val _summonerByPuuidResult = MutableStateFlow("")
    val summonerByPuuidResult = _summonerByPuuidResult.asStateFlow()

    private val _rotationChampions = MutableStateFlow<List<Pair<String, String?>>>(emptyList())
    val rotationChampions: StateFlow<List<Pair<String, String?>>> = _rotationChampions.asStateFlow()

    fun fetchAccountByRiotId(riotId: String) {
        viewModelScope.launch(DispatcherModule.provideIoDispatcher()) {
            _summonerByPuuidResult.value = ""

            val parsed = parseRiotId(riotId)
            if (parsed == null) {
                _accountResult.value = context.getString(R.string.invalid_riot_id)
                return@launch
            }
            val (gameName, tagLine) = parsed

            if (!BuildConfig.USE_MOCK_SERVER) {
                _accountResult.value = context.getString(R.string.account_riot_id_stg_only)
                return@launch
            }

            try {
                Log.d("khoon", "Account by Riot ID: $gameName#$tagLine → ${BuildConfig.MOCK_BASE_URL}")
                val accountResponse = mockLoLApi.getAccountByRiotId(gameName, tagLine)
                if (!accountResponse.isSuccessful) {
                    _accountResult.value = context.getString(
                        R.string.api_error,
                        accountResponse.code(),
                        accountResponse.errorBody()?.string().orEmpty()
                    )
                    return@launch
                }

                val account = accountResponse.body()
                _accountResult.value = context.getString(
                    R.string.account_info_format,
                    account?.gameName.orEmpty(),
                    account?.tagLine.orEmpty(),
                    account?.puuid.orEmpty()
                )

                val puuid = account?.puuid
                if (puuid.isNullOrBlank()) {
                    _summonerByPuuidResult.value = context.getString(R.string.summoner_lookup_skipped)
                    return@launch
                }

                Log.d("khoon", "Summoner by puuid: $puuid")
                val summonerResponse = mockLoLApi.getSummonerByPuuid(puuid)
                if (summonerResponse.isSuccessful) {
                    val summoner = summonerResponse.body()
                    _summonerByPuuidResult.value = context.getString(
                        R.string.summoner_by_puuid_format,
                        summoner?.summonerLevel?.toString().orEmpty(),
                        summoner?.profileIconId?.toString().orEmpty(),
                        summoner?.id.orEmpty(),
                        summoner?.accountId.orEmpty(),
                        summoner?.puuid.orEmpty()
                    )
                } else {
                    _summonerByPuuidResult.value = context.getString(
                        R.string.summoner_api_error,
                        summonerResponse.code(),
                        summonerResponse.errorBody()?.string().orEmpty()
                    )
                }
            } catch (e: Exception) {
                Log.e("khoon", "Account by Riot ID failed: ${e.message}", e)
                _accountResult.value = context.getString(R.string.network_error, e.message.orEmpty())
                _summonerByPuuidResult.value = ""
            }
        }
    }

    /** Split "Name#Tag" into gameName / tagLine. */
    private fun parseRiotId(raw: String): Pair<String, String>? {
        val trimmed = raw.trim()
        val hash = trimmed.indexOf('#')
        if (hash <= 0 || hash == trimmed.lastIndex) return null
        val gameName = trimmed.substring(0, hash).trim()
        val tagLine = trimmed.substring(hash + 1).trim()
        if (gameName.isEmpty() || tagLine.isEmpty()) return null
        return gameName to tagLine
    }

    fun fetchSummonerInfo(name: String) {
        val in_name = name
        viewModelScope.launch(DispatcherModule.provideIoDispatcher()) {
            try {
                Log.d("khoon", "=== API Call Debug Info ===")
                Log.d("khoon", "Summoner name: $in_name")
                Log.d("khoon", "API key: ${API_KEY}")
                Log.d("khoon", "Base URL: https://kr.api.riotgames.com/lol/")
                Log.d("khoon", "Full endpoint: summoner/v4/summoners/by-name/$in_name")

                // 먼저 champion rotation API로 API 키 테스트
                Log.d("khoon", "Testing API key with champion rotation...")
                val rotationResponse = riotApiService.getChampionRotation(API_KEY)
                Log.d("khoon", "Champion rotation response code: ${rotationResponse.code()}")

                val response = riotApiService.getSummoner(summonerName = in_name, API_KEY)

                Log.d("khoon", "Response code: ${response.code()}")
                Log.d("khoon", "Response headers: ${response.headers()}")

                if (response.isSuccessful) {
                    Log.d("khoon", "API call successful")
                    val dto = response.body()
                    _result.value = context.getString(
                        R.string.summoner_result_format,
                        in_name,
                        dto?.summonerLevel?.toString().orEmpty(),
                        dto?.id.orEmpty()
                    )
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("khoon", "=== API Error Details ===")
                    Log.e("khoon", "Error code: ${response.code()}")
                    Log.e("khoon", "Error body: $errorBody")
                    Log.e("khoon", "Error headers: ${response.headers()}")

                    _result.value = context.getString(
                        R.string.summoner_api_error_with_body,
                        response.code(),
                        errorBody.orEmpty()
                    )
                }
            } catch (e: Exception) {
                Log.e("khoon", "=== Network Exception ===")
                Log.e("khoon", "Exception type: ${e.javaClass.simpleName}")
                Log.e("khoon", "Exception message: ${e.message}")
                e.printStackTrace()

                _result.value = context.getString(R.string.network_error, e.message.orEmpty())
            }
        }
    }

    fun fetchRotationChampions() {
        Log.d("khoon", "called fetchRotationChampions USE_MOCK_SERVER=${BuildConfig.USE_MOCK_SERVER}")

        viewModelScope.launch(DispatcherModule.provideIoDispatcher()) {
            val result = if (BuildConfig.USE_MOCK_SERVER) {
                fetchRotationFromMock()
            } else {
                championRepository.fetchRotationChampionImages()
            }
            _rotationChampions.value = result
        }
    }

    private suspend fun fetchRotationFromMock(): List<Pair<String, String?>> {
        return try {
            val response = mockLoLApi.getChampionRotation()
            Log.d("khoon", "mock rotation code: ${response.code()}")
            if (!response.isSuccessful) {
                Log.e("khoon", "mock rotation failed: ${response.errorBody()?.string()}")
                return emptyList()
            }
            val ids = response.body()?.freeChampionIds.orEmpty()
            Log.d("khoon", "mock rotation ids: $ids")
            championRepository.resolveRotationImages(ids)
        } catch (e: Exception) {
            Log.e("khoon", "mock rotation exception: ${e.message}", e)
            emptyList()
        }
    }
}
