package com.khoon.lol.info.di

import com.khoon.lol.info.data.api.DataDragonAPI
import com.khoon.lol.info.data.api.LeagueOfLegendAPI
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
                val newRequest = original.newBuilder()
                    .header("User-Agent", "MyRiotApp/1.0")
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
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