package com.khoon.lol.info.model

data class ChampionDataResponse(
    val data: Map<String, ChampionDetail>
)

data class ChampionDetail(
    val id: String,
    val key: String,
    val name: String,
    val title: String,
    val lore: String,
    val tags: List<String>,
    val info: Info,
    val stats: Stats,
    val spells: List<Spell>,
    val passive: Passive
)

data class Info(
    val attack: Int,
    val defense: Int,
    val magic: Int,
    val difficulty: Int
)

data class Stats(
    val hp: Float,
    val hpperlevel: Float,
    val mp: Float,
    val attackdamage: Float,
    val armor: Float
    // Add more fields as needed
)

data class Spell(
    val id: String,
    val name: String,
    val description: String,
    val cooldown: List<Float>
)

data class Passive(
    val name: String,
    val description: String
)