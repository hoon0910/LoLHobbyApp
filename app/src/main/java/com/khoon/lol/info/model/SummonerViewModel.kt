package com.khoon.lol.info.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khoon.lol.info.data.api.LeagueOfLegendAPI
import com.khoon.lol.info.utils.constant.AppConstant.API_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SummonerViewModel @Inject constructor(
    private val riotApiService: LeagueOfLegendAPI
) : ViewModel() {

    private val _result = MutableStateFlow<String>("")
    val result = _result.asStateFlow()

    fun fetchSummonerInfo(name: String) {
        val in_name = name
        viewModelScope.launch {
            try {
                Log.d("khoon", "test")

                val response = riotApiService.getSummoner(summonerName = in_name, API_KEY)
                if (response.isSuccessful) {
                    Log.d("khoon", "successFul")
                    val dto = response.body()
                    _result.value = "level: ${dto?.summonerLevel}"
                } else {
                    _result.value = "API error: ${response.code()}"
                    Log.d("khoon", _result.value)
                }
            } catch (e: Exception) {
                _result.value = "Network error: ${e.message}"
                Log.e("khoon", _result.value)
            }
        }
    }
}