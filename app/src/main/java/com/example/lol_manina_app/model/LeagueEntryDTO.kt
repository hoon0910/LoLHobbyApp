package com.example.lol_manina_app.model

data class LeagueEntryDTO(
    val leagueId: String,
    val queueType: String,
    val tier: String,
    val rank: String,
    val leaguePoints: Int,
    val wins: Int,
    val losses: Int,
    val miniSeries: MiniSeriesTypeConverter.MiniSeries? // optional, only for promotion series
)