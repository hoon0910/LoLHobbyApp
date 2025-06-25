package com.khoon.lol.info.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RiotApiClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DataDragonClient 