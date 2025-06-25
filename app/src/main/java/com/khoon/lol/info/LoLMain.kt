package com.khoon.lol.info

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.khoon.lol.info.model.ChampionDetailViewModel
import com.khoon.lol.info.navigation.NavGraph
import com.khoon.lol.info.navigation.NavRoutes
import com.khoon.lol.info.ui.components.LoLAppBar
import com.khoon.lol.info.utils.constant.AppConstant.TAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoLMain : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MainCompose()
        }
    }
}

@Composable
fun MainCompose() {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route
    val championId = currentBackStack?.arguments?.getString("championId")
    val viewModel: ChampionDetailViewModel = hiltViewModel()
    val championEntity = viewModel.championEntity.collectAsState().value

    Log.d(TAG, "called MainCompose")

    // Load champion data when in detail screen
    LaunchedEffect(championId) {
        if (currentRoute?.startsWith(NavRoutes.ChampionDetail.route) == true && championId != null) {
            viewModel.loadChampionJsonData(championId)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LoLAppBar(
            showBackButton = currentRoute?.startsWith(NavRoutes.ChampionDetail.route) == true,
            onBackClick = { navController.popBackStack() },
            title = if (currentRoute?.startsWith(NavRoutes.ChampionDetail.route) == true) {
                championId ?: "LOL DETAIL"
            } else {
                "LOL COMPOSE"
            },
            isFavorite = championEntity?.isFavorite ?: false,
            onFavoriteClick = {
                championEntity?.let { entity ->
                    viewModel.toggleFavorite(entity)
                }
            }
        )
        
        NavGraph(
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = WindowInsets.navigationBars.getBottom(LocalDensity.current).dp
                )
        )
    }
}