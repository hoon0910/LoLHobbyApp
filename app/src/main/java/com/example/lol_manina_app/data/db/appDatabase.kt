package com.example.lol_manina_app.data.db


import MiniSeriesTypeConverter
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.lol_manina_app.model.SummonerEntity
import com.example.lol_manina_app.model.SearchEntity
import com.example.lol_manina_app.model.ProfileEntity


@Database(entities = [SummonerEntity::class, SearchEntity::class, ProfileEntity::class],
    version = 1)

@TypeConverters(
    value = [MiniSeriesTypeConverter::class]
)



abstract class AppDatabase: RoomDatabase() {
    abstract fun LoLDao(): LoLDao
}