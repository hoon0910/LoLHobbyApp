package com.example.lol_manina_app.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lol_manina_app.model.ChampionEntity
import com.example.lol_manina_app.model.ChampionViewModel
import com.example.lol_manina_app.ui.components.ChampionImage
import com.example.lol_manina_app.ui.components.FavoriteButton

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ChampionListScreen(
    viewModel: ChampionViewModel = hiltViewModel(),
    onChampionClick: (String, String, Boolean) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope
) {
    val champions by viewModel.filteredChampions.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val showOnlyFavorites by viewModel.showOnlyFavorites.collectAsState()

    Log.d("khoon", "initScreen")

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                bottom = WindowInsets.navigationBars.getBottom(LocalDensity.current).dp
            )
    ) {
        val (searchRow, content) = createRefs()
        
        Row(
            modifier = Modifier
                .constrainAs(searchRow) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                label = { Text("Enter Champion") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        modifier = Modifier.size(24.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear search",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
                    .animateContentSize(),
                shape = MaterialTheme.shapes.large,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                ),
                singleLine = true
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Only Favorite")
                Spacer(modifier = Modifier.width(8.dp))
                Switch(checked = showOnlyFavorites, onCheckedChange = { viewModel.toggleShowOnlyFavorites() })
            }
        }

        Box(
            modifier = Modifier
                .constrainAs(content) {
                    top.linkTo(searchRow.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            when {
                champions.isEmpty() && searchQuery.isBlank() -> {
                    Text("No data")
                }
                else -> {
                    ChampIconList(
                        filteredList = champions,
                        onFavoriteClick = { viewModel.toggleFavorite(it) },
                        onChampionClick = onChampionClick,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ChampIconList(
    filteredList: List<ChampionEntity>,
    modifier: Modifier = Modifier,
    onFavoriteClick: (ChampionEntity) -> Unit,
    onChampionClick: (String, String, Boolean) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope
) {
    val bottomPadding = WindowInsets.navigationBars.getBottom(LocalDensity.current).dp + 32.dp
    val state = rememberLazyListState()

    Box(modifier = modifier.fillMaxWidth()) {
        LazyColumn(
            state = state,
            contentPadding = PaddingValues(bottom = bottomPadding)
        ) {
            items(filteredList.chunked(2)) { rowItems ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (rowItems.size == 1) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(4.dp)
                        ) {
                            with(sharedTransitionScope) {
                                ChampionImage(
                                    champion = rowItems[0],
                                    onChampionClick = onChampionClick,
                                    modifier = Modifier
                                        .sharedElement(
                                            rememberSharedContentState(key = "champion_${rowItems[0].name}"),
                                            animatedVisibilityScope = animatedVisibilityScope
                                        )
                                        .sharedBounds(
                                            rememberSharedContentState(key = "champion_bounds_${rowItems[0].name}"),
                                            animatedVisibilityScope = animatedVisibilityScope
                                        )
                                )
                                FavoriteButton(
                                    isFavorite = rowItems[0].isFavorite,
                                    onClick = { onFavoriteClick(rowItems[0])},
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .sharedElement(
                                            rememberSharedContentState(key = "favorite_button_${rowItems[0].name}"),
                                            animatedVisibilityScope = animatedVisibilityScope
                                        )
                                        .sharedBounds(
                                            rememberSharedContentState(key = "favorite_button_bounds_${rowItems[0].name}"),
                                            animatedVisibilityScope = animatedVisibilityScope
                                        )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    } else {
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
                                                rememberSharedContentState(key = "champion_${champion.name}"),
                                                animatedVisibilityScope = animatedVisibilityScope
                                            )
                                            .sharedBounds(
                                                rememberSharedContentState(key = "champion_bounds_${champion.name}"),
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
                                                rememberSharedContentState(key = "favorite_button_${champion.name}"),
                                                animatedVisibilityScope = animatedVisibilityScope
                                            )
                                            .sharedBounds(
                                                rememberSharedContentState(key = "favorite_button_bounds_${champion.name}"),
                                                animatedVisibilityScope = animatedVisibilityScope
                                            )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
} 