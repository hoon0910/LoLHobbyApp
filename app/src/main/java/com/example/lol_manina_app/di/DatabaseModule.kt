package com.example.lol_manina_app.di

import android.content.Context
import com.example.lol_manina_app.data.db.ChampionDatabase
import com.example.lol_manina_app.data.db.ChampionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideChampionDatabase(
        @ApplicationContext context: Context
    ): ChampionDatabase {
        return ChampionDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideChampionDao(database: ChampionDatabase): ChampionDao {
        return database.championDao()
    }
} 