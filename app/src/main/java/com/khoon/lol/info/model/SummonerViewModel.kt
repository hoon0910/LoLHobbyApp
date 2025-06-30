package com.khoon.lol.info.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khoon.lol.info.data.api.LeagueOfLegendAPI
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
    private val championRepository: ChampionRepository,
) : ViewModel() {

    private val _result = MutableStateFlow<String>("")
    val result = _result.asStateFlow()

    private val _rotationChampions = MutableStateFlow<List<Pair<String, String?>>>(emptyList())
    val rotationChampions: StateFlow<List<Pair<String, String?>>> = _rotationChampions.asStateFlow()

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
        Log.d("khoon", "called fetchRotationChampions")

        viewModelScope.launch(DispatcherModule.provideIoDispatcher()) {
            val result = championRepository.fetchRotationChampionImages()
            _rotationChampions.value = result
        }
    }
}