package com.khoon.lol.info.model

data class Summoner(
    val id: String,                // encryptedSummonerId
    val accountId: String,
    val puuid: String,
    val profileIconId: Int,
    val revisionDate: Long,
    val summonerLevel: Int
)