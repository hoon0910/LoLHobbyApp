package com.example.lol_manina_app.data.api

import com.example.lol_manina_app.model.ChampionDataResponse
import com.example.lol_manina_app.model.ChampionRotation
import com.example.lol_manina_app.model.CurrentGameInfo
import com.example.lol_manina_app.model.LeagueEntryDTO
import com.example.lol_manina_app.model.Summoner
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LeagueOfLegendAPI {
    @GET("summoner/v4/summoners/{summonerId}")
    suspend fun getSummoner(
        @Path("summonerId") summonerName: String,
        @Query("api_key") api_key: String
    ): Response<Summoner>

    @GET("league/v4/entries/by-summoner/{encryptedSummonerId}")
    suspend fun getLeague(
        @Path("encryptedSummonerId") encryptedSummonerId: String?,
        @Query("api_key") api_key: String
    ): Response<Set<LeagueEntryDTO>>

    @GET("spectator/v4/active-games/by-summoner/{encryptedSummonerId}")
    suspend fun getSpectator(
        @Path("encryptedSummonerId") encryptedSummonerId: String?,
        @Query("api_key") api_key: String
    ): Response<CurrentGameInfo>

    @GET("platform/v3/champion-rotations")
    suspend fun getChampionRotation(): Response<ChampionRotation>

}