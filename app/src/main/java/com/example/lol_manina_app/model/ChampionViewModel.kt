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

    private val _allChampions = MutableStateFlow<List<ChampionEntity>>(emptyList())
    val allChampions: StateFlow<List<ChampionEntity>> = _allChampions

    // New state for search query and favorite filter
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _showOnlyFavorites = MutableStateFlow(false)
    val showOnlyFavorites: StateFlow<Boolean> = _showOnlyFavorites

    // Add state for search mode
    private val _isSearchMode = MutableStateFlow(false)
    val isSearchMode: StateFlow<Boolean> = _isSearchMode

    // Combined flow for filtered list
    val filteredChampions: StateFlow<List<ChampionEntity>> =
        combine<List<ChampionEntity>, String, Boolean, Boolean, List<ChampionEntity>>(
        allChampions,
        _searchQuery,
        _showOnlyFavorites,
        _isSearchMode
    ) { champions, query, onlyFavorites, isSearchMode ->
        champions
            .filter { 
                // When in search mode, return an empty list for a blank query
                if (isSearchMode && query.isBlank()) {
                    false
                } else if (query.isNotBlank()) {
                    it.name.contains(query, ignoreCase = true)
                } else {
                    true // Otherwise, show all champions
                }
            }
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

    fun setSearchMode(isSearchMode: Boolean) {
        _isSearchMode.value = isSearchMode
    }

    fun toggleShowOnlyFavorites() {
        _showOnlyFavorites.value = !_showOnlyFavorites.value
    }

    fun toggleFavorite(champion: ChampionEntity) = viewModelScope.launch {
        val updatedChampion = champion.copy(isFavorite = !champion.isFavorite)
        championRepository.updateChampion(updatedChampion)
    }
}