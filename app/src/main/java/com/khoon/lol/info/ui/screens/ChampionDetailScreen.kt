package com.khoon.lol.info.ui.screens

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.khoon.lol.info.model.ChampionDetail
import com.khoon.lol.info.model.ChampionDetailViewModel
// Ensure ChampionSkillsSection is imported from .components if not already handled by wildcard
import com.khoon.lol.info.ui.components.ChampionInfoSection
import com.khoon.lol.info.ui.components.ChampionSkillsSection // Explicit import for clarity
import com.khoon.lol.info.ui.components.InfoSection
import com.khoon.lol.info.ui.components.PassiveSection
import com.khoon.lol.info.ui.components.StatsSection

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

@Composable
fun ChampionDetailTabbedContent(
    name: String,
    detail: ChampionDetail?,
    modifier: Modifier = Modifier // Allow passing modifiers for Portrait/Landscape specific layout
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("Info", "Stats", "Skills")

    Column(modifier = modifier) { // Apply modifier here
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        // Content based on selected tab
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Ensure this column takes remaining space if parent has weight
                .verticalScroll(rememberScrollState())
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp, // Added top padding for content below TabRow
                    bottom = WindowInsets.navigationBars.getBottom(LocalDensity.current).dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Spacer(modifier = Modifier.height(16.dp)) // This spacer is now part of the top padding of the Column
            when (selectedTabIndex) {
                0 -> { // Info Tab
                    InfoSection(detail = detail)
                    Spacer(modifier = Modifier.height(20.dp))
                    ChampionInfoSection(detail = detail, name = name)
                    Spacer(modifier = Modifier.height(20.dp))
                    PassiveSection(detail = detail)
                }
                1 -> { // Stats Tab
                    StatsSection(detail = detail)
                }
                2 -> { // Skills Tab
                    // This now calls the composable from the .components package
                    ChampionSkillsSection(detail = detail)
                }
            }
            Spacer(modifier = Modifier.height(16.dp)) // Add spacer at the bottom for scroll padding
        }
    }
}

// The old placeholder ChampionSkillsSection composable definition is removed from here.
