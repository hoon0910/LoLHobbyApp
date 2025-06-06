package com.example.lol_manina_app.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
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
    onChampionClick: (String, String) -> Unit
) {
    composable(NavRoutes.ChampionList.route) {
        ChampionListScreen(
            viewModel = hiltViewModel(),
            onChampionClick = onChampionClick
        )
    }
}

fun NavGraphBuilder.searchScreen() {
    composable(NavRoutes.Search.route) {
        SearchScreen(
            viewModel = hiltViewModel(),
            modifier = androidx.compose.ui.Modifier
        )
    }
}

fun NavGraphBuilder.championDetailScreen(
    onBackClick: () -> Unit
) {
    composable(
        route = NavRoutes.ChampionDetail.route,
        arguments = listOf(
            navArgument("championId") { type = NavType.StringType },
            navArgument("imageUrl") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val championId = backStackEntry.arguments?.getString("championId") ?: return@composable
        val imageUrl = backStackEntry.arguments?.getString("imageUrl")?.let { 
            NavRoutes.decodeUrl(it)
        } ?: return@composable
        ChampionDetailScreen(
            name = championId,
            imageUrl = imageUrl,
        )
    }
} 