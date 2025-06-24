package com.example.lol_manina_app.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChampionListHeaderBarSearchMode(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchModeChange: (Boolean) -> Unit,
    showOnlyFavorites: Boolean,
    onToggleFavorites: () -> Unit,
) {
    BaseHeaderLayout(
        showOnlyFavorites = showOnlyFavorites,
        onToggleFavorites = onToggleFavorites,
        leftContent = {
            // This is the specific content for the search mode
            CustomSearchBar(
                query = searchQuery,
                onQueryChange = onSearchQueryChange,
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            )
            IconButton(
                onClick = {
                    onSearchQueryChange("")
                    onSearchModeChange(false)
                },
                modifier = Modifier
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close Search")
            }
        }
    )
} 