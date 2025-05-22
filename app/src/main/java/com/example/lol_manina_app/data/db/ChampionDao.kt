package com.example.lol_manina_app.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.lol_manina_app.model.ChampionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChampionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(champion: ChampionEntity)

    @Query("SELECT * FROM champion_table ORDER BY name ASC")
    fun getAllChampions(): Flow<List<ChampionEntity>>

    @Delete
    suspend fun deleteChampion(champion: ChampionEntity)

    @Query("DELETE FROM champion_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM champion_table WHERE name = :name")
    suspend fun getChampionByName(name: String): ChampionEntity?

    @Update
    suspend fun updateChampion(champion: ChampionEntity)
}