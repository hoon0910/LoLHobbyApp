package com.example.lol_manina_app.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lol_manina_app.data.repository.ChampionRepository
import com.example.lol_manina_app.di.IoDispatcher
import com.example.lol_manina_app.utils.constant.AppConstant.TAG
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
    private val _championEntity = MutableStateFlow<ChampionEntity?>(null)
    val championEntity: StateFlow<ChampionEntity?> = _championEntity

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
                        championToUpdate = championRepository.insertChampion(newChampion)
                    } else {
                        // If champion exists, just update its detail
                        championToUpdate = championToUpdate.copy(detail = detail)
                        championRepository.updateChampion(championToUpdate)
                    }

                    _championEntity.value = championToUpdate // Set the value with the entity that has the correct ID

                } catch (e: Exception) {
                    Log.e(TAG, "Failed to fetch champion data: ${e.message}")
                }
            }
        }
    }

    fun toggleFavorite(champion: ChampionEntity) = viewModelScope.launch {
        val updatedChampion = champion.copy(isFavorite = !champion.isFavorite)
        Log.d(TAG, "Toggle with ${!champion.isFavorite}, ${champion.name}" )
        championRepository.updateChampion(updatedChampion)
        Log.d(TAG, "Favorite status updated for ${updatedChampion.name} to ${updatedChampion.isFavorite}")
        _championEntity.value = updatedChampion
    }
}