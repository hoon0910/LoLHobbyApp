package com.khoon.lol.info.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.khoon.lol.info.ui.screens.ChampionDetailScreen
import com.khoon.lol.info.ui.screens.ChampionListScreen
import com.khoon.lol.info.ui.screens.HomeScreen

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.homeScreen(
    onImageClick: () -> Unit,
    onSummonerClick: () -> Unit
) {
    composable(NavRoutes.Home.route) {
        HomeScreen(onImageClick = onImageClick, onSummonerClick = onSummonerClick)
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.championListScreen(
    onChampionClick: (String, String, Boolean) -> Unit,
    sharedScope: SharedTransitionScope,
) {
    composable(NavRoutes.ChampionList.route) {
        val animatedScope = this@composable
        ChampionListScreen(
            viewModel = hiltViewModel(),
            onChampionClick = onChampionClick,
            animatedVisibilityScope = animatedScope,
            sharedTransitionScope = sharedScope
        )
    }
}


@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.championDetailScreen(
    sharedScope: SharedTransitionScope,
    ) {
    composable(
        route = NavRoutes.ChampionDetail.route,
        arguments = listOf(
            navArgument("championId") { type = NavType.StringType },
            navArgument("imageUrl") { type = NavType.StringType },
            navArgument("favorite") { type = NavType.BoolType }

        )
    ) { backStackEntry ->
        val animatedScope = this@composable
        val championId = backStackEntry.arguments?.getString("championId") ?: return@composable
        val imageUrl = backStackEntry.arguments?.getString("imageUrl")?.let { 
            NavRoutes.decodeUrl(it)
        } ?: return@composable
        ChampionDetailScreen(
            name = championId,
            imageUrl = imageUrl,
            animatedVisibilityScope = animatedScope,
            sharedTransitionScope = sharedScope
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.summonerSearchScreen() {
    composable(NavRoutes.SummonerSearch.route) {
        com.khoon.lol.info.ui.screens.SummonerSearchScreen()
    }
} 