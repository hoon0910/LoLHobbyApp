package com.example.lol_manina_app.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RiotApiClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DataDragonClient 