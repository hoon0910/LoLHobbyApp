package com.example.lol_manina_app.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
    val champions by viewModel.allChampions.observeAsState(emptyList())
    var searchQuery by remember { mutableStateOf("") }
    var showOnlyFavorites by remember { mutableStateOf(false) }

    val filteredList = champions
        .filter { it.name.contains(searchQuery, ignoreCase = true) }
        .filter { !showOnlyFavorites || it.isFavorite }

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
                onValueChange = { searchQuery = it },
                label = { Text("Enter search a Champion") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
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
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Only Favorite")
                Spacer(modifier = Modifier.width(8.dp))
                Switch(checked = showOnlyFavorites, onCheckedChange = { showOnlyFavorites = it })
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
                filteredList.isEmpty() -> {
                    Text("No result data")
                }
                else -> {
                    ChampIconList(
                        filteredList = filteredList,
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

@OptIn(ExperimentalSharedTransitionApi::class)
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

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(bottom = bottomPadding)
    ) {
        items(filteredList.chunked(4)) { rowItems ->
            LazyRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(rowItems) { champion ->
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .padding(8.dp)
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
                                modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
} 