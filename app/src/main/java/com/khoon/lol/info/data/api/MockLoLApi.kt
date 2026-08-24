package com.khoon.lol.info.data.api

import com.khoon.lol.info.model.ChampionRotation
import com.khoon.lol.info.model.Summoner
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("app/v1/accounts/search")
    suspend fun searchAccounts(
        @Query("q") query: String,
        @Query("region") region: String? = null,
    ): Response<List<AccountDto>>

    @GET("lol/league/v4/entries/by-puuid/{puuid}")
    suspend fun getLeagueEntriesByPuuid(
        @Path("puuid") puuid: String,
    ): Response<List<LeagueEntryDto>>
}

data class AccountDto(
    val puuid: String,
    val gameName: String,
    val tagLine: String,
)

data class LeagueEntryDto(
    val leagueId: String? = null,
    val queueType: String,
    val tier: String,
    val rank: String,
    val puuid: String? = null,
    val summonerId: String? = null,
    val leaguePoints: Int,
    val wins: Int,
    val losses: Int,
    val hotStreak: Boolean = false,
    val veteran: Boolean = false,
    val freshBlood: Boolean = false,
    val inactive: Boolean = false,
)
