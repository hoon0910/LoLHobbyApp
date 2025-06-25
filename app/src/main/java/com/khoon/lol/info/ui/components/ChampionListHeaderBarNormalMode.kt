package com.khoon.lol.info.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable

@Composable
fun ChampionListHeaderBarNormalMode(
    onSearchModeChange: (Boolean) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    showOnlyFavorites: Boolean,
    onToggleFavorites: () -> Unit,
    allTags: List<String> = emptyList(),
    selectedTags: Set<String> = emptySet(),
    onTagSelected: (String) -> Unit = {}
) {
    BaseHeaderLayout(
        showOnlyFavorites = showOnlyFavorites,
        onToggleFavorites = onToggleFavorites,
        allTags = allTags,
        selectedTags = selectedTags,
        onTagSelected = onTagSelected,
        leftContent = {
            // This is the specific content for the normal mode
            GradientIconButton(
                onClick = {
                    onSearchQueryChange("")
                    onSearchModeChange(true)
                },
                icon = Icons.Default.Search,
                contentDescription = "Search"
            )
        }
    )
} 