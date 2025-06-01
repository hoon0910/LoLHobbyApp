package com.example.lol_manina_app.di

import com.example.lol_manina_app.data.repository.ChampionRepository
import com.example.lol_manina_app.data.repository.ChampionRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindChampionRepository(
        championRepositoryImpl: ChampionRepositoryImpl
    ): ChampionRepository
} 