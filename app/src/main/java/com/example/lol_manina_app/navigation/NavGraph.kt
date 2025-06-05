package com.example.lol_manina_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = NavRoutes.ChampionList.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        championListScreen(
            onSearchClick = { navController.navigate(NavRoutes.Search.route) },
            onChampionClick = { championId ->
                navController.navigate(NavRoutes.ChampionDetail.createRoute(championId))
            }
        )
        //searchScreen()
        championDetailScreen(
            onBackClick = { navController.popBackStack() }
        )
    }
} 