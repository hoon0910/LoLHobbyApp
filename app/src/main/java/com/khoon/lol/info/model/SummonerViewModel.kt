package com.khoon.lol.info.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khoon.lol.info.BuildConfig
import com.khoon.lol.info.data.api.LeagueOfLegendAPI
import com.khoon.lol.info.data.api.MockLoLApi
import com.khoon.lol.info.data.repository.ChampionRepository
import com.khoon.lol.info.di.DispatcherModule
import com.khoon.lol.info.utils.constant.AppConstant.API_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SummonerViewModel @Inject constructor(
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
                _accountResult.value = "Invalid Riot ID. Use Name#Tag (e.g. Faker#KR1)"
                return@launch
            }
            val (gameName, tagLine) = parsed

            if (!BuildConfig.USE_MOCK_SERVER) {
                _accountResult.value = "Account by Riot ID is available in stg (mock) only"
                return@launch
            }

            try {
                Log.d("khoon", "Account by Riot ID: $gameName#$tagLine → ${BuildConfig.MOCK_BASE_URL}")
                val accountResponse = mockLoLApi.getAccountByRiotId(gameName, tagLine)
                if (!accountResponse.isSuccessful) {
                    _accountResult.value =
                        "API error: ${accountResponse.code()}\n${accountResponse.errorBody()?.string().orEmpty()}"
                    return@launch
                }

                val account = accountResponse.body()
                _accountResult.value = buildString {
                    append("gameName: ${account?.gameName}\n")
                    append("tagLine: ${account?.tagLine}\n")
                    append("puuid: ${account?.puuid}")
                }

                val puuid = account?.puuid
                if (puuid.isNullOrBlank()) {
                    _summonerByPuuidResult.value = "Summoner lookup skipped: missing puuid"
                    return@launch
                }

                Log.d("khoon", "Summoner by puuid: $puuid")
                val summonerResponse = mockLoLApi.getSummonerByPuuid(puuid)
                if (summonerResponse.isSuccessful) {
                    val summoner = summonerResponse.body()
                    _summonerByPuuidResult.value = buildString {
                        append("summonerLevel: ${summoner?.summonerLevel}\n")
                        append("profileIconId: ${summoner?.profileIconId}\n")
                        append("id: ${summoner?.id}\n")
                        append("accountId: ${summoner?.accountId}\n")
                        append("puuid: ${summoner?.puuid}")
                    }
                } else {
                    _summonerByPuuidResult.value =
                        "Summoner API error: ${summonerResponse.code()}\n" +
                            summonerResponse.errorBody()?.string().orEmpty()
                }
            } catch (e: Exception) {
                Log.e("khoon", "Account by Riot ID failed: ${e.message}", e)
                _accountResult.value = "Network error: ${e.message}"
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
                    _result.value = "Name: $in_name\nLevel: ${dto?.summonerLevel}\nID: ${dto?.id}"
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("khoon", "=== API Error Details ===")
                    Log.e("khoon", "Error code: ${response.code()}")
                    Log.e("khoon", "Error body: $errorBody")
                    Log.e("khoon", "Error headers: ${response.headers()}")

                    _result.value = "API error: ${response.code()}\nError: $errorBody"
                }
            } catch (e: Exception) {
                Log.e("khoon", "=== Network Exception ===")
                Log.e("khoon", "Exception type: ${e.javaClass.simpleName}")
                Log.e("khoon", "Exception message: ${e.message}")
                e.printStackTrace()

                _result.value = "Network error: ${e.message}"
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
