package com.khoon.lol.info.ui.screens

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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel // 변경된 import 경로
import com.khoon.lol.info.model.ChampionViewModel
import com.khoon.lol.info.ui.components.ChampionListContent
import com.khoon.lol.info.ui.components.ChampionListHeaderBar
import com.khoon.lol.info.ui.utils.view.PullDownHeaderScrollConnection

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
    val allTags by viewModel.allAvailableTags.collectAsState()
    val selectedTags by viewModel.selectedTags.collectAsState()

    var showHeader by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    val nestedScrollConnection = remember {
        PullDownHeaderScrollConnection(listState) {
            showHeader = it
        }
    }
    val headerHeight = if (showHeader) 120.dp else 0.dp // Increased height to accommodate tag chips

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
                onToggleFavorites = { viewModel.toggleShowOnlyFavorites() },
                allTags = allTags.toList(),
                selectedTags = selectedTags,
                onTagSelected = { viewModel.toggleTag(it) }
            )
        }
    }
} 