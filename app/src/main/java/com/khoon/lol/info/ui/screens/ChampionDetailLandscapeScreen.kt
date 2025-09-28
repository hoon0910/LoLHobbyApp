package com.khoon.lol.info.ui.screens

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
// Unused imports related to old tab logic might be here, IDE can clean them up.
// import androidx.compose.foundation.rememberScrollState
// import androidx.compose.foundation.verticalScroll
// import androidx.compose.material3.Tab
// import androidx.compose.material3.TabRow
// import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
// import androidx.compose.runtime.getValue
// import androidx.compose.runtime.mutableStateOf
// import androidx.compose.runtime.remember
// import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
// import androidx.compose.ui.platform.LocalDensity
// import androidx.compose.ui.unit.dp
import com.khoon.lol.info.model.ChampionDetail
import com.khoon.lol.info.ui.components.ChampionImageLoader
// ChampionSkillsSection is now defined in ChampionDetailScreen.kt
// Components like InfoSection, StatsSection etc. are still needed if ChampionDetailTabbedContent uses them.

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

        // Right side - Tabbed content
        ChampionDetailTabbedContent(
            name = name,
            detail = detail,
            modifier = Modifier
                .weight(0.6f)
                .fillMaxHeight() // Ensure the tabbed content fills the right side
        )
    }
}