package com.example.lol_manina_app.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.lol_manina_app.ui.components.ChampionDetailScreen
import com.example.lol_manina_app.utils.view.ChampionListScreen
import com.example.lol_manina_app.utils.view.SearchScreen

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.championListScreen(
    onSearchClick: () -> Unit,
    onChampionClick: (String, String) -> Unit,
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

fun NavGraphBuilder.searchScreen() {
    composable(NavRoutes.Search.route) {
        SearchScreen(
            viewModel = hiltViewModel(),
            modifier = Modifier
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.championDetailScreen(
    onBackClick: () -> Unit,
    sharedScope: SharedTransitionScope,
    ) {
    composable(
        route = NavRoutes.ChampionDetail.route,
        arguments = listOf(
            navArgument("championId") { type = NavType.StringType },
            navArgument("imageUrl") { type = NavType.StringType }
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