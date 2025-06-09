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
        val sharedScope = this
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier
        ) {
            championListScreen(
                onChampionClick = { championId, imageUrl, favorite->
                    navController.navigate(NavRoutes.ChampionDetail.createRoute(championId, imageUrl
                    ,favorite))
                },
                sharedScope
            )
            //searchScreen()
            championDetailScreen(
                sharedScope
            )
        }
    }
} 