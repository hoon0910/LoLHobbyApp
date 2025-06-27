package com.khoon.lol.info.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = NavRoutes.Home.route
) {
    SharedTransitionLayout {
        val sharedScope = this
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier
        ) {
            homeScreen(
                onImageClick = {
                    navController.navigate(NavRoutes.ChampionList.route) {
                        popUpTo(NavRoutes.Home.route) { inclusive = true }
                    }
                },
                onSummonerClick = {
                    navController.navigate(NavRoutes.SummonerSearch.route)
                }
            )
            championListScreen(
                onChampionClick = { championId, imageUrl, favorite->
                    navController.navigate(NavRoutes.ChampionDetail.createRoute(championId, imageUrl, favorite))
                },
                sharedScope
            )
            summonerSearchScreen()
            championDetailScreen(
                sharedScope
            )
        }
    }
} 