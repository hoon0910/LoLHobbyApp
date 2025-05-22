package com.example.lol_manina_app.model

data class ChampionDataResponse(
    val data: Map<String, ChampionInfo>
)

data class ChampionInfo(
    val key: String,
    val id: String,
    val name: String
)