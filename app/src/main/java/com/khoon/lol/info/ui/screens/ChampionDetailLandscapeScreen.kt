package com.khoon.lol.info.ui.screens

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.khoon.lol.info.model.ChampionDetail
import com.khoon.lol.info.ui.components.*

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ChampionDetailLandscapeScreen(
    name: String,
    imageUrl: String?,
    detail: ChampionDetail?,
    animatedVisibilityScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope
) {
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        // Left side - Image
        ChampionImageLoader(
            imagePath = if (imageUrl != null) {
                "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/${name}_0.jpg"
            } else {
                null
            },
            name = name,
            animatedVisibilityScope = animatedVisibilityScope,
            sharedTransitionScope = sharedTransitionScope,
            modifier = Modifier
                .weight(0.4f)
                .fillMaxHeight()
        )

        // Right side - Scrollable content
        Column(
            modifier = Modifier
                .weight(0.6f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = WindowInsets.navigationBars.getBottom(LocalDensity.current).dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Info Section
            InfoSection(detail = detail)
                        
            // Stats Section
            StatsSection(detail = detail)
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Champion Info Section
            ChampionInfoSection(detail = detail, name = name)
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Passive Section
            PassiveSection(detail = detail)
        }
    }
} 