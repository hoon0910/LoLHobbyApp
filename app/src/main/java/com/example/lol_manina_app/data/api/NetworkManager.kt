package com.example.lol_manina_app.data.api

// NetworkManager.kt
import com.example.lol_manina_app.utils.constant.AppConstant.API_KEY
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient

object NetworkManager {
    //private const val BASE_URL = "https://REGION.api.riotgames.com/lol/"
    private const val BASE_URL = "https://kr.api.riotgames.com/lol/"
    private const val DDRAGON_URL = "https://ddragon.leagueoflegends.com/"

    // Riot API 클라이언트
    private val riotApiClient: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    private val dataDragonClient: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(DDRAGON_URL)
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(DDRAGON_URL)
        .client(createOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val url = original.url.newBuilder()
                    .addQueryParameter("api_key", API_KEY) // API 키 자동 추가
                    .build()
                val newRequest = original.newBuilder()
                    .url(url)
                    .header("User-Agent", "MyRiotApp/1.0")
                    .build()
                chain.proceed(newRequest)
            }
            .build()
    }

    // Riot API 서비스
    val riotApiService: LeagueOfLegendAPI by lazy {
        riotApiClient.create(LeagueOfLegendAPI::class.java)
    }

    // Data Dragon API 서비스
    val dataDragonApiService: DataDragonAPI by lazy {
        dataDragonClient.create(DataDragonAPI::class.java)
    }
}