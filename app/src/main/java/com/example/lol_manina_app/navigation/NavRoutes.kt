package com.example.lol_manina_app.navigation

sealed class NavRoutes(val route: String) {
    object ChampionList : NavRoutes("champion_list")
    object Search : NavRoutes("search")
    object ChampionDetail : NavRoutes("champion_detail/{championId}/{imageUrl}") {
        fun createRoute(championId: String, imageUrl: String) = 
            "champion_detail/$championId/${encodeUrl(imageUrl)}"
    }

    companion object {
        fun encodeUrl(url: String): String = android.net.Uri.encode(url)
        fun decodeUrl(encodedUrl: String): String = android.net.Uri.decode(encodedUrl)
    }
} 