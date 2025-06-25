package com.khoon.lol.info.model

data class CurrentGameParticipant(
    val summonerName: String,
    val championId: Int,
    val teamId: Int,
    val summonerId: String,
    val spell1Id: Int,
    val spell2Id: Int,
    val perks: Perks
)