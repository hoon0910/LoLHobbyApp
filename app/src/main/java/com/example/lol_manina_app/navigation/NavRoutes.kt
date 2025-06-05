package com.example.lol_manina_app.navigation

sealed class NavRoutes(val route: String) {
    object ChampionList : NavRoutes("champion_list")
    object Search : NavRoutes("search")
    object ChampionDetail : NavRoutes("champion_detail/{championId}") {
        fun createRoute(championId: String) = "champion_detail/$championId"
    }
} 