package com.khoon.lol.info.data.db

import androidx.room.*
import com.khoon.lol.info.model.ChampionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChampionDao {
    @Query("SELECT * FROM champion_table")
    fun getAllChampions(): Flow<List<ChampionEntity>>

    @Query("SELECT * FROM champion_table WHERE name = :name")
    suspend fun getChampionByName(name: String): ChampionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(champion: ChampionEntity): Long

    @Update
    suspend fun updateChampion(champion: ChampionEntity): Int

    @Delete
    suspend fun deleteChampion(champion: ChampionEntity)

    @Query("DELETE FROM champion_table")
    suspend fun deleteAll()
}