package com.khoon.lol.info.di

import android.content.Context
import android.content.SharedPreferences
import com.khoon.lol.info.LoLApp.AppPrefUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences("app_pref", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAppPrefUtil(
        prefs: SharedPreferences
    ): AppPrefUtil {
        return AppPrefUtil(prefs)
    }
} 