package com.khoon.lol.info.ui.screens

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun HomeScreen(onImageClick: () -> Unit, onSummonerClick: () -> Unit) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        HomeScreenLandscape(onImageClick = onImageClick, onSummonerClick = onSummonerClick)
    } else {
        HomeScreenPortrait(onImageClick = onImageClick, onSummonerClick = onSummonerClick)
    }
} 