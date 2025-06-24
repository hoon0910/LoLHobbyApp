package com.example.lol_manina_app.ui.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.lol_manina_app.model.ChampionEntity

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ChampionListContent(
    champions: List<ChampionEntity>,
    onFavoriteClick: (ChampionEntity) -> Unit,
    onChampionClick: (String, String, Boolean) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    headerHeight: Dp
) {
    val bottomPadding = WindowInsets.navigationBars.getBottom(LocalDensity.current).dp + 32.dp
    val state = rememberLazyListState()

    // Detect screen orientation and determine grid columns
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    val columnsPerRow = if (isLandscape) 4 else 2

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (champions.isEmpty()) {
            // Display a message when there are no search results
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = bottomPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No Champion",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        } else {
            LazyColumn(
                state = state,
                contentPadding = PaddingValues(bottom = bottomPadding, top = headerHeight)
            ) {
                items(champions.chunked(columnsPerRow)) { rowItems ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        rowItems.forEach { champion ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(4.dp)
                            ) {
                                with(sharedTransitionScope) {

                                    ChampionImage(
                                        champion = champion,
                                        onChampionClick = onChampionClick,
                                        modifier = Modifier
                                            .sharedElement(
                                                rememberSharedContentState(
                                                    key = "champion_${champion.name}"),
                                                animatedVisibilityScope = animatedVisibilityScope
                                            )
                                            .sharedBounds(
                                                rememberSharedContentState(
                                                    key = "champion_bounds_${champion.name}"),
                                                animatedVisibilityScope = animatedVisibilityScope
                                            )
                                    )
                                    FavoriteButton(
                                        isFavorite = champion.isFavorite,
                                        onClick = { onFavoriteClick(champion)},
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .offset(x = 4.dp, y = (-4).dp)
                                            .sharedElement(
                                                rememberSharedContentState(
                                                    key = "favorite_button_${champion.name}"),
                                                animatedVisibilityScope = animatedVisibilityScope
                                            )
                                            .sharedBounds(
                                                rememberSharedContentState(
                                                    key = "favorite_button_bounds_${champion.name}"),
                                                animatedVisibilityScope = animatedVisibilityScope
                                            )
                                    )
                                }
                            }
                        }
                        // Add spacers for the remaining columns to fill the row
                        repeat(columnsPerRow - rowItems.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
} 