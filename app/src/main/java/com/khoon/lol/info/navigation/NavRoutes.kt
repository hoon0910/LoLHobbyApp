package com.khoon.lol.info.navigation

sealed class NavRoutes(val route: String) {
    object ChampionList : NavRoutes("champion_list")
    object ChampionDetail : NavRoutes("champion_detail/{championId}/{imageUrl}/{favorite}") {
        fun createRoute(championId: String, imageUrl: String, favorite: Boolean) =
            "champion_detail/$championId/${encodeUrl(imageUrl)}/${favorite}"
    }

    companion object {
        fun encodeUrl(url: String): String = android.net.Uri.encode(url)
        fun decodeUrl(encodedUrl: String): String = android.net.Uri.decode(encodedUrl)
    }
} 