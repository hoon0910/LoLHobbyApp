package com.khoon.lol.info.model

data class Summoner(
    val id: String? = null,
    val accountId: String? = null,
    val puuid: String,
    val profileIconId: Int,
    val revisionDate: Long,
    val summonerLevel: Int,
)
