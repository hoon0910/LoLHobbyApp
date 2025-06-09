package com.example.lol_manina_app.utils.view

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lol_manina_app.navigation.NavGraph
import com.example.lol_manina_app.navigation.NavRoutes
import com.example.lol_manina_app.ui.components.LoLAppBar
import com.example.lol_manina_app.utils.constant.AppConstant.TAG
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

    Log.d(TAG, "called MainCompose")

    Column(modifier = Modifier.fillMaxSize()) {
        LoLAppBar(
            showBackButton = currentRoute?.startsWith(NavRoutes.ChampionDetail.route) == true,
            onBackClick = { navController.popBackStack() },
            title = if (currentRoute?.startsWith(NavRoutes.ChampionDetail.route) == true) {
                "LOL DETAIL"
            } else {
                "LOL COMPOSE"
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