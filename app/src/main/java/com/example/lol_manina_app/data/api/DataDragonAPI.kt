package com.example.lol_manina_app.data.api

import com.example.lol_manina_app.model.ChampionDataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface DataDragonAPI {

    @GET("cdn/{version}/data/en_US/champion.json")
    suspend fun getChampionData(@Path("version") version: String): Response<ChampionDataResponse>

    @GET("api/versions.json")
    suspend fun getVersions(): Response<List<String>>

    @GET("cdn/{version}/data/en_US/champion/{champion}.json")
    suspend fun getChampionDetail(
        @Path("version") version: String,
        @Path("champion") champion: String
    ): ChampionDataResponse

    @GET("cdn/{version}/img/champion/{champion}.png")
    suspend fun getChampionImage(
        @Path("version") version: String,
        @Path("champion") champion: String
    ): Response<okhttp3.ResponseBody>
}