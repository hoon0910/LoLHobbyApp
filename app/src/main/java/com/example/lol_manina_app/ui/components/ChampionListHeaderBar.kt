package com.example.lol_manina_app.ui.components

import androidx.compose.runtime.Composable

@Composable
fun ChampionListHeaderBar(
    isSearchMode: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchModeChange: (Boolean) -> Unit,
    showOnlyFavorites: Boolean,
    onToggleFavorites: () -> Unit,
    allTags: List<String> = emptyList(),
    selectedTags: Set<String> = emptySet(),
    onTagSelected: (String) -> Unit = {}
) {
    if (isSearchMode) {
        ChampionListHeaderBarSearchMode(
            searchQuery = searchQuery,
            onSearchQueryChange = onSearchQueryChange,
            onSearchModeChange = onSearchModeChange,
            showOnlyFavorites = showOnlyFavorites,
            onToggleFavorites = onToggleFavorites,
            allTags = allTags,
            selectedTags = selectedTags,
            onTagSelected = onTagSelected
        )
    } else {
        ChampionListHeaderBarNormalMode(
            onSearchModeChange = onSearchModeChange,
            onSearchQueryChange = onSearchQueryChange,
            showOnlyFavorites = showOnlyFavorites,
            onToggleFavorites = onToggleFavorites,
            allTags = allTags,
            selectedTags = selectedTags,
            onTagSelected = onTagSelected
        )
    }
} 