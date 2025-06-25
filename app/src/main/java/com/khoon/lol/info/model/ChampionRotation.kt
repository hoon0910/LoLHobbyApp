package com.khoon.lol.info.model

data class ChampionRotation(
    val freeChampionIds: List<Int>,
    val freeChampionIdsForNewPlayers: List<Int>,
    val maxNewPlayerLevel: Int
)