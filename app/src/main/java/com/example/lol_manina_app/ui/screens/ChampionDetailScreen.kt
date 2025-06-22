package com.example.lol_manina_app.ui.screens

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lol_manina_app.model.ChampionDetailViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ChampionDetailScreen(
    viewModel: ChampionDetailViewModel = hiltViewModel(),
    name: String,
    imageUrl: String?,
    animatedVisibilityScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope
) {
    val championEntity = viewModel.championEntity.collectAsState().value
    val detail = championEntity?.detail

    LaunchedEffect(name) {
        viewModel.loadChampionJsonData(name)
    }

    // Detect screen orientation
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        ChampionDetailLandscapeScreen(
            name = name,
            imageUrl = imageUrl,
            detail = detail,
            animatedVisibilityScope = animatedVisibilityScope,
            sharedTransitionScope = sharedTransitionScope
        )
    } else {
        ChampionDetailPortraitScreen(
            name = name,
            imageUrl = imageUrl,
            detail = detail,
            animatedVisibilityScope = animatedVisibilityScope,
            sharedTransitionScope = sharedTransitionScope
        )
    }
}