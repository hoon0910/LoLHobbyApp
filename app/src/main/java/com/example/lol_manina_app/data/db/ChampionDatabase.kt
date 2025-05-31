package com.example.lol_manina_app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.lol_manina_app.model.ChampionEntity

@Database(entities = [ChampionEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ChampionDatabase : RoomDatabase() {
    abstract fun championDao(): ChampionDao

    companion object {
        fun getDatabase(context: Context): ChampionDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ChampionDatabase::class.java,
                "champion_database"
            ).build()
        }
    }
}