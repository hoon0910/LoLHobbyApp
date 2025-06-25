package com.example.lol_manina_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BaseHeaderLayout(
    leftContent: @Composable RowScope.() -> Unit,
    showOnlyFavorites: Boolean,
    modifier: Modifier = Modifier,
    onToggleFavorites: () -> Unit,
    allTags: List<String> = emptyList(),
    selectedTags: Set<String> = emptySet(),
    onTagSelected: (String) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        // Top row with search and favorite button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 8.dp)
        ) {
            // The left side content is passed in as a slot
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                leftContent()
            }

            // Favorite toggle button
            GradientIconButton(
                onClick = onToggleFavorites,
                icon = if (showOnlyFavorites) Icons.Filled.Star else Icons.Outlined.StarBorder,
                contentDescription = "Filter Favorites",
                isActive = showOnlyFavorites
            )
        }

        // Tag filter chips row
        if (allTags.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                items(allTags) { tag ->
                    val isSelected = selectedTags.contains(tag)
                    TagChip(
                        tag = tag,
                        isSelected = isSelected,
                        onClick = { onTagSelected(tag) }
                    )
                }
            }
        }
    }
} 