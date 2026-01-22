package com.khoon.lol.info.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.khoon.lol.info.data.repository.ChampionRepository
import com.khoon.lol.info.di.IoDispatcher
import com.khoon.lol.info.utils.constant.AppConstant.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChampionDetailViewModel @Inject constructor(
    application: Application,
    private val championRepository: ChampionRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AndroidViewModel(application) {

    fun observeChampion(name: String): StateFlow<ChampionEntity?> {
        return championRepository.observeChampionByName(name)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )
    }

    fun loadChampionJsonData(name: String) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                try {
                    val existingChampion = championRepository.getChampionByName(name)
                    val currentIsFavorite = existingChampion?.isFavorite ?: false

                    // Fetch detail from API
                    val detail = championRepository.getChampionDetail(name)

                    var championToUpdate = existingChampion

                    if (championToUpdate == null) {
                        // If champion doesn't exist, create a new one and insert it
                        val newChampion = ChampionEntity(
                            name = name,
                            imagePath = null, // Image path will be handled by ChampionRepository later
                            isFavorite = currentIsFavorite,
                            detail = detail
                        )
                        championRepository.insertChampion(newChampion)
                    } else {
                        // If champion exists, just update its detail
                        championToUpdate = championToUpdate.copy(detail = detail)
                        championRepository.updateChampion(championToUpdate)
                    }
                    // Flow will automatically emit the updated value

                } catch (e: Exception) {
                    Log.e(TAG, "Failed to fetch champion data: ${e.message}")
                }
            }
        }
    }

    fun toggleFavorite(champion: ChampionEntity) = viewModelScope.launch {
        val updatedChampion = champion.copy(isFavorite = !champion.isFavorite)
        Log.d(TAG, "Toggle with ${!champion.isFavorite}, ${champion.name}")
        championRepository.updateChampion(updatedChampion)
        Log.d(TAG, "Favorite status updated for ${updatedChampion.name} to ${updatedChampion.isFavorite}")
        // Flow will automatically emit the updated value
    }
}