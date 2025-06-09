package com.example.lol_manina_app.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.lol_manina_app.ui.screens.ChampionDetailScreen
import com.example.lol_manina_app.ui.screens.ChampionListScreen

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
        val favorite = backStackEntry.arguments!!.getBoolean("favorite")
        ChampionDetailScreen(
            name = championId,
            imageUrl = imageUrl,
            favorite = favorite,
            animatedVisibilityScope = animatedScope,
            sharedTransitionScope = sharedScope
        )
    }
} 