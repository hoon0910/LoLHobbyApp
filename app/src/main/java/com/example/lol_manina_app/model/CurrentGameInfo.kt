package com.example.lol_manina_app.model

data class CurrentGameInfo(
    val gameId: Long,
    val mapId: Int,
    val gameMode: String,
    val gameType: String,
    val gameQueueConfigId: Int,
    val participants: List<CurrentGameParticipant>
)