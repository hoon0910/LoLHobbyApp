package com.example.lol_manina_app.data.api

import com.example.lol_manina_app.model.ChampionDataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DataDragonAPI {

    @GET("cdn/{version}/data/en_US/champion.json")
    suspend fun getChampionData(@Path("version") version: String): Response<ChampionDataResponse>

    @GET("api/versions.json")
    suspend fun getVersions(): Response<List<String>>
}