package com.example.lol_manina_app.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun ChampionListHeaderBarNormalMode(
    onSearchModeChange: (Boolean) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    showOnlyFavorites: Boolean,
    onToggleFavorites: () -> Unit,
) {
    BaseHeaderLayout(
        showOnlyFavorites = showOnlyFavorites,
        onToggleFavorites = onToggleFavorites,
        leftContent = {
            // This is the specific content for the normal mode
            IconButton(onClick = {
                onSearchQueryChange("")
                onSearchModeChange(true)
            }) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        }
    )
} 