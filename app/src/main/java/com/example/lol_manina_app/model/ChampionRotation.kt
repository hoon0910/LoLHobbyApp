package com.example.lol_manina_app.model

data class ChampionRotation(
    val freeChampionIds: List<Int>,
    val freeChampionIdsForNewPlayers: List<Int>,
    val maxNewPlayerLevel: Int
)