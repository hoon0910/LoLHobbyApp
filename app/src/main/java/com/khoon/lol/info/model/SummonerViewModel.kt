package com.khoon.lol.info.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khoon.lol.info.BuildConfig
import com.khoon.lol.info.R
import com.khoon.lol.info.data.api.AccountDto
import com.khoon.lol.info.data.api.MockLoLApi
import com.khoon.lol.info.data.repository.ChampionRepository
import com.khoon.lol.info.di.DispatcherModule
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
    private val mockLoLApi: MockLoLApi,
    private val championRepository: ChampionRepository,
) : ViewModel() {

    private val _accountResult = MutableStateFlow("")
    val accountResult = _accountResult.asStateFlow()

    private val _summonerByPuuidResult = MutableStateFlow("")
    val summonerByPuuidResult = _summonerByPuuidResult.asStateFlow()

    private val _leagueEntriesResult = MutableStateFlow("")
    val leagueEntriesResult = _leagueEntriesResult.asStateFlow()

    private val _searchCandidates = MutableStateFlow<List<AccountDto>>(emptyList())
    val searchCandidates: StateFlow<List<AccountDto>> = _searchCandidates.asStateFlow()

    private val _rotationChampions = MutableStateFlow<List<Pair<String, String?>>>(emptyList())
    val rotationChampions: StateFlow<List<Pair<String, String?>>> = _rotationChampions.asStateFlow()

    private val _newPlayerRotationChampions =
        MutableStateFlow<List<Pair<String, String?>>>(emptyList())
    val newPlayerRotationChampions: StateFlow<List<Pair<String, String?>>> =
        _newPlayerRotationChampions.asStateFlow()

    private val _rotationError = MutableStateFlow<String?>(null)
    val rotationError: StateFlow<String?> = _rotationError.asStateFlow()

    /** Name only → mock search list; Name#Tag → Account → Summoner → League. */
    fun searchRiotId(input: String, region: String) {
        viewModelScope.launch(DispatcherModule.provideIoDispatcher()) {
            clearSearchResults()

            val trimmed = input.trim()
            if (trimmed.isEmpty()) return@launch

            if (!BuildConfig.USE_MOCK_SERVER) {
                _accountResult.value = context.getString(R.string.summoner_search_stg_only)
                return@launch
            }

            if (trimmed.contains('#')) {
                fetchAccountAndSummoner(trimmed)
            } else {
                searchAccountsByName(trimmed, region)
            }
        }
    }

    fun selectSearchCandidate(candidate: AccountDto) {
        searchRiotId("${candidate.gameName}#${candidate.tagLine}", region = "")
    }

    private suspend fun searchAccountsByName(gameName: String, region: String) {
        try {
            Log.d("khoon", "Account search: q=$gameName region=$region")
            val response = mockLoLApi.searchAccounts(gameName, region.ifBlank { null })
            if (!response.isSuccessful) {
                val err = context.getString(
                    R.string.search_error,
                    response.code(),
                    response.errorBody()?.string().orEmpty(),
                )
                Log.d("khoon", err)
                _accountResult.value = err
                return
            }
            val candidates = response.body().orEmpty()
            _searchCandidates.value = candidates
            Log.d("khoon", "Account search candidates=${candidates.size}: $candidates")
            if (candidates.isEmpty()) {
                _accountResult.value = context.getString(R.string.no_accounts_found, gameName)
            }
        } catch (e: Exception) {
            Log.e("khoon", "Account search failed: ${e.message}", e)
            _accountResult.value = context.getString(R.string.network_error, e.message.orEmpty())
        }
    }

    private suspend fun fetchAccountAndSummoner(riotId: String) {
        _searchCandidates.value = emptyList()

        val parsed = parseRiotId(riotId)
        if (parsed == null) {
            _accountResult.value = context.getString(R.string.invalid_riot_id)
            return
        }
        val (gameName, tagLine) = parsed

        try {
            Log.d("khoon", "Account by Riot ID: $gameName#$tagLine → ${BuildConfig.MOCK_BASE_URL}")
            val accountResponse = mockLoLApi.getAccountByRiotId(gameName, tagLine)
            if (!accountResponse.isSuccessful) {
                _accountResult.value = context.getString(
                    R.string.api_error,
                    accountResponse.code(),
                    accountResponse.errorBody()?.string().orEmpty(),
                )
                return
            }

            val account = accountResponse.body()
            _accountResult.value = context.getString(
                R.string.account_info_format,
                account?.gameName.orEmpty(),
                account?.tagLine.orEmpty(),
                account?.puuid.orEmpty(),
            )

            val puuid = account?.puuid
            if (puuid.isNullOrBlank()) {
                _summonerByPuuidResult.value = context.getString(R.string.summoner_lookup_skipped)
                return
            }

            Log.d("khoon", "Summoner by puuid: $puuid")
            val summonerResponse = mockLoLApi.getSummonerByPuuid(puuid)
            if (summonerResponse.isSuccessful) {
                val summoner = summonerResponse.body()
                _summonerByPuuidResult.value = buildString {
                    append("summonerLevel: ${summoner?.summonerLevel}\n")
                    append("profileIconId: ${summoner?.profileIconId}\n")
                    append("puuid: ${summoner?.puuid}")
                    if (!summoner?.id.isNullOrBlank()) {
                        append("\nid: ${summoner?.id}")
                    }
                    if (!summoner?.accountId.isNullOrBlank()) {
                        append("\naccountId: ${summoner?.accountId}")
                    }
                }
                fetchLeagueEntriesByPuuid(puuid)
            } else {
                _summonerByPuuidResult.value = context.getString(
                    R.string.summoner_api_error,
                    summonerResponse.code(),
                    summonerResponse.errorBody()?.string().orEmpty(),
                )
                _leagueEntriesResult.value = ""
            }
        } catch (e: Exception) {
            Log.e("khoon", "Account by Riot ID failed: ${e.message}", e)
            _accountResult.value = context.getString(R.string.network_error, e.message.orEmpty())
            _summonerByPuuidResult.value = ""
            _leagueEntriesResult.value = ""
        }
    }

    private suspend fun fetchLeagueEntriesByPuuid(puuid: String) {
        try {
            Log.d("khoon", "League entries by puuid: $puuid")
            val response = mockLoLApi.getLeagueEntriesByPuuid(puuid)
            if (!response.isSuccessful) {
                _leagueEntriesResult.value = context.getString(
                    R.string.api_error,
                    response.code(),
                    response.errorBody()?.string().orEmpty(),
                )
                return
            }
            val entries = response.body().orEmpty()
            if (entries.isEmpty()) {
                _leagueEntriesResult.value = context.getString(R.string.unranked)
                return
            }
            _leagueEntriesResult.value = entries.joinToString(separator = "\n\n") { entry ->
                buildString {
                    append(queueLabel(entry.queueType))
                    append(": ${entry.tier} ${entry.rank} (${entry.leaguePoints} LP)\n")
                    append("W/L: ${entry.wins}/${entry.losses}")
                }
            }
        } catch (e: Exception) {
            Log.e("khoon", "League entries failed: ${e.message}", e)
            _leagueEntriesResult.value = context.getString(R.string.network_error, e.message.orEmpty())
        }
    }

    private fun queueLabel(queueType: String): String = when (queueType) {
        "RANKED_SOLO_5x5" -> "Solo/Duo"
        "RANKED_FLEX_SR" -> "Flex"
        else -> queueType
    }

    private fun clearSearchResults() {
        _accountResult.value = ""
        _summonerByPuuidResult.value = ""
        _leagueEntriesResult.value = ""
        _searchCandidates.value = emptyList()
    }

    private fun parseRiotId(raw: String): Pair<String, String>? {
        val trimmed = raw.trim()
        val hash = trimmed.indexOf('#')
        if (hash <= 0 || hash == trimmed.lastIndex) return null
        val gameName = trimmed.substring(0, hash).trim()
        val tagLine = trimmed.substring(hash + 1).trim()
        if (gameName.isEmpty() || tagLine.isEmpty()) return null
        return gameName to tagLine
    }

    fun fetchRotationChampions() {
        Log.d("khoon", "called fetchRotationChampions USE_MOCK_SERVER=${BuildConfig.USE_MOCK_SERVER}")

        viewModelScope.launch(DispatcherModule.provideIoDispatcher()) {
            _rotationError.value = null
            val rows = if (BuildConfig.USE_MOCK_SERVER) {
                fetchRotationFromMock()
            } else {
                championRepository.fetchRotationChampionImages()
            }
            _rotationChampions.value = rows.freeRotation
            _newPlayerRotationChampions.value = rows.newPlayerRotation
            if (rows.freeRotation.isEmpty() && rows.newPlayerRotation.isEmpty() && _rotationError.value == null) {
                _rotationError.value = context.getString(
                    R.string.rotation_unavailable,
                    BuildConfig.MOCK_BASE_URL,
                )
            }
        }
    }

    private suspend fun fetchRotationFromMock(): RotationChampionRows {
        return try {
            val response = mockLoLApi.getChampionRotation()
            Log.d("khoon", "mock rotation code: ${response.code()}")
            if (!response.isSuccessful) {
                val err = response.errorBody()?.string().orEmpty()
                Log.e("khoon", "mock rotation failed: $err")
                _rotationError.value = context.getString(R.string.api_error, response.code(), err)
                return RotationChampionRows()
            }
            val body = response.body()
            val freeIds = body?.freeChampionIds.orEmpty()
            val newPlayerIds = body?.freeChampionIdsForNewPlayers.orEmpty()
            Log.d("khoon", "mock rotation sr=$freeIds newplayer=$newPlayerIds")
            RotationChampionRows(
                freeRotation = championRepository.resolveRotationImages(freeIds),
                newPlayerRotation = championRepository.resolveRotationImages(newPlayerIds),
            )
        } catch (e: Exception) {
            Log.e("khoon", "mock rotation exception: ${e.message}", e)
            _rotationError.value = context.getString(R.string.network_error, e.message.orEmpty())
            RotationChampionRows()
        }
    }
}
