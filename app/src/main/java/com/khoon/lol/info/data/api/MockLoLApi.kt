package com.khoon.lol.info.data.api

import com.khoon.lol.info.model.ChampionRotation
import com.khoon.lol.info.model.Summoner
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * LAN mock server APIs (stg only). Paths match Riot docs for bridge-ready switching.
 */
interface MockLoLApi {
    @GET("riot/account/v1/accounts/by-riot-id/{gameName}/{tagLine}")
    suspend fun getAccountByRiotId(
        @Path("gameName") gameName: String,
        @Path("tagLine") tagLine: String,
    ): Response<AccountDto>

    @GET("lol/platform/v3/champion-rotations")
    suspend fun getChampionRotation(): Response<ChampionRotation>

    @GET("lol/summoner/v4/summoners/by-puuid/{puuid}")
    suspend fun getSummonerByPuuid(
        @Path("puuid") puuid: String,
    ): Response<Summoner>
}

data class AccountDto(
    val puuid: String,
    val gameName: String,
    val tagLine: String,
)
