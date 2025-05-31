package com.example.lol_manina_app.di

import com.example.lol_manina_app.data.api.DataDragonAPI
import com.example.lol_manina_app.data.api.LeagueOfLegendAPI
import com.example.lol_manina_app.utils.constant.AppConstant.API_KEY
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://kr.api.riotgames.com/lol/"
    private const val DDRAGON_URL = "https://ddragon.leagueoflegends.com/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val url = original.url.newBuilder()
                    .addQueryParameter("api_key", API_KEY)
                    .build()
                val newRequest = original.newBuilder()
                    .url(url)
                    .header("User-Agent", "MyRiotApp/1.0")
                    .build()
                chain.proceed(newRequest)
            }
            .build()
    }

    @Provides
    @Singleton
    @RiotApiClient
    fun provideRiotApiClient(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @DataDragonClient
    fun provideDataDragonClient(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(DDRAGON_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideRiotApiService(@RiotApiClient retrofit: Retrofit): LeagueOfLegendAPI {
        return retrofit.create(LeagueOfLegendAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideDataDragonApiService(@DataDragonClient retrofit: Retrofit): DataDragonAPI {
        return retrofit.create(DataDragonAPI::class.java)
    }
} 