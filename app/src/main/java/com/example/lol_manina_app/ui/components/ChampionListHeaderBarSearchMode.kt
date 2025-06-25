package com.example.lol_manina_app.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
            // This is the specific content for the search mode
            CustomSearchBar(
                query = searchQuery,
                onQueryChange = onSearchQueryChange,
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            )
            GradientIconButton(
                onClick = {
                    onSearchQueryChange("")
                    onSearchModeChange(false)
                },
                icon = Icons.Default.Close,
                contentDescription = "Close Search"
            )
        }
    )
} 