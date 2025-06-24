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
) {
    if (isSearchMode) {
        ChampionListHeaderBarSearchMode(
            searchQuery = searchQuery,
            onSearchQueryChange = onSearchQueryChange,
            onSearchModeChange = onSearchModeChange,
            showOnlyFavorites = showOnlyFavorites,
            onToggleFavorites = onToggleFavorites
        )
    } else {
        ChampionListHeaderBarNormalMode(
            onSearchModeChange = onSearchModeChange,
            onSearchQueryChange = onSearchQueryChange,
            showOnlyFavorites = showOnlyFavorites,
            onToggleFavorites = onToggleFavorites
        )
    }
} 