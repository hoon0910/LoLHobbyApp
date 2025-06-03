package com.example.lol_manina_app.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lol_manina_app.data.repository.ChampionRepository
import com.example.lol_manina_app.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChampionDetailViewModel @Inject constructor(
    application: Application,
    private val championRepository: ChampionRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AndroidViewModel(application) {
    private val _championDetail = MutableStateFlow<ChampionDetail?>(null)
    val championDetail: StateFlow<ChampionDetail?> = _championDetail

    fun loadChampionJsonData(name: String) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                try {
                    val detail = championRepository.getChampionDetail(name)
                    _championDetail.value = detail
                } catch (e: Exception) {
                    Log.e("ChampionVM", "Failed to fetch champion data: ${e.message}")
                }
            }
        }
    }
}