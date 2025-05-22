package com.example.lol_manina_app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lol_manina_app.model.ChampionEntity

@Database(entities = [ChampionEntity::class], version = 1, exportSchema = false)
abstract class ChampionDatabase : RoomDatabase() {
    abstract fun championDao(): ChampionDao

    companion object {
        @Volatile
        private var INSTANCE: ChampionDatabase? = null

        fun getDatabase(context: Context): ChampionDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChampionDatabase::class.java,
                    "champion_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}