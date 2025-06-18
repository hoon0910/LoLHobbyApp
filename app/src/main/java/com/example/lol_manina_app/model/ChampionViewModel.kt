package com.example.lol_manina_app.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lol_manina_app.data.repository.ChampionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChampionViewModel @Inject constructor(
    application: Application,
    private val championRepository: ChampionRepository
) : AndroidViewModel(application) {
    private val _championList = MutableStateFlow<List<String>>(emptyList())
    private val _championMap = MutableStateFlow<Map<Int, String>>(emptyMap())

    private val _allChampions = MutableStateFlow<List<ChampionEntity>>(emptyList())
    val allChampions: StateFlow<List<ChampionEntity>> = _allChampions

    // New state for search query and favorite filter
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _showOnlyFavorites = MutableStateFlow(false)
    val showOnlyFavorites: StateFlow<Boolean> = _showOnlyFavorites

    // Combined flow for filtered list
    val filteredChampions: StateFlow<List<ChampionEntity>> =
        combine<List<ChampionEntity>, String, Boolean, List<ChampionEntity>>(
        allChampions,
        _searchQuery,
        _showOnlyFavorites
    ) { champions, query, onlyFavorites ->
        champions
            .filter { it.name.contains(query, ignoreCase = true) }
            .filter { !onlyFavorites || it.isFavorite }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        fetchChampionData()
        // Collect the Flow from repository
        viewModelScope.launch {
            championRepository.getAllChampions().collect { champions ->
                _allChampions.value = champions
            }
        }
    }

    private fun fetchChampionData() {
        viewModelScope.launch {
            try {
                championRepository.fetchChampionData()
            } catch (e: Exception) {
                println("Error fetching champion data: ${e.message}")
                Log.e("khoon", e.message.toString())
            }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleShowOnlyFavorites() {
        _showOnlyFavorites.value = !_showOnlyFavorites.value
    }

    fun addOrUpdateChampion(id: Int, name: String) {
        Log.d("khoon", "addOrUpdateChampion : " + name)
        _championMap.value = _championMap.value.toMutableMap().apply {
            this[id] = name
        }
    }

    fun fetchChampionRotation() {
        viewModelScope.launch {
            try {
                val champions = championRepository.fetchChampionRotation()
                _championList.value = champions
                Log.d("khoon", "fetchChampionRotation Success :  " + _championList.value)
            } catch (e: Exception) {
                _championList.value = listOf("Network error: ${e.message}")
                Log.d("khoon", "fetchChampionRotation exception")
            }
        }
    }

    fun toggleFavorite(champion: ChampionEntity) = viewModelScope.launch {
        val updatedChampion = champion.copy(isFavorite = !champion.isFavorite)
        championRepository.updateChampion(updatedChampion)
    }
}