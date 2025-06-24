package com.example.lol_manina_app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lol_manina_app.model.ChampionViewModel
import com.example.lol_manina_app.ui.components.ChampionListContent
import com.example.lol_manina_app.ui.components.ChampionListHeaderBar
import com.example.lol_manina_app.ui.utils.view.PullDownHeaderScrollConnection

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
    val isSearchMode by viewModel.isSearchMode.collectAsState()

    var showHeader by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    val nestedScrollConnection = remember { PullDownHeaderScrollConnection(listState) {
        showHeader = it }
    }
    val headerHeight = if (showHeader) 60.dp else 0.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        // Draw the list content first, in the background
        ChampionListContent(
            champions = champions,
            onFavoriteClick = { viewModel.toggleFavorite(it) },
            onChampionClick = onChampionClick,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
            headerHeight = headerHeight
        )
        // Draw the header on top of the content, so it can be clicked
        AnimatedVisibility(visible = showHeader) {
            ChampionListHeaderBar(
                isSearchMode = isSearchMode,
                searchQuery = searchQuery,
                onSearchQueryChange = { viewModel.setSearchQuery(it) },
                onSearchModeChange = { viewModel.setSearchMode(it) },
                showOnlyFavorites = showOnlyFavorites,
                onToggleFavorites = { viewModel.toggleShowOnlyFavorites() }
            )
        }
    }
}