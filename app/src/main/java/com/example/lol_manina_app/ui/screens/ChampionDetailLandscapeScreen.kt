package com.example.lol_manina_app.ui.screens

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.lol_manina_app.R
import com.example.lol_manina_app.model.ChampionDetail
import com.example.lol_manina_app.ui.components.*

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
        with(sharedTransitionScope) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
                    .sharedElement(
                        rememberSharedContentState(key = "champion_${name}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .sharedBounds(
                        rememberSharedContentState(key = "champion_bounds_${name}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    AsyncImage(
                        model = if (imageUrl != null) {
                            "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/${name}_0.jpg"
                        } else {
                            R.drawable.no_image
                        },
                        contentDescription = name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        error = painterResource(id = R.drawable.no_image),
                        placeholder = painterResource(id = R.drawable.no_image)
                    )
                }
            }
        }

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