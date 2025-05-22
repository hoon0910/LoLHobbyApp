package com.example.lol_manina_app.utils.view

import MiniSeriesTypeConverter
import androidx.room.Room.databaseBuilder
import com.example.lol_manina_app.LoLApp
import com.example.lol_manina_app.Repository.AppRepository
import com.example.lol_manina_app.data.api.LeagueOfLegendAPI
import com.example.lol_manina_app.data.db.AppDatabase
import com.example.lol_manina_app.model.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    single {
        Retrofit.Builder()
            .baseUrl("https://kr.api.riotgames.com/lol/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LeagueOfLegendAPI::class.java)

    }
    single {
        databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "lol-app.db")
            .addTypeConverter(MiniSeriesTypeConverter(LoLApp.getGson()))
            .build()
    }
    single { get<AppDatabase>().LoLDao() }
}


val viewModelModule = module {
    //viewModel { MainViewModel(get()) }
    //viewModel { SearchViewModel(get()) }
    //viewModel { SpectatorViewModel(get()) }
}

val repositoryModule = module {
    single { AppRepository(get(),get()) }
}