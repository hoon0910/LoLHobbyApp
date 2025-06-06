package com.example.lol_manina_app.navigation

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
    startDestination: String = NavRoutes.ChampionList.route
) {
    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier
        ) {
            championListScreen(
                onSearchClick = { navController.navigate(NavRoutes.Search.route) },
                onChampionClick = { championId, imageUrl ->
                    navController.navigate(NavRoutes.ChampionDetail.createRoute(championId, imageUrl))
                }
            )
            //searchScreen()
            championDetailScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
} 