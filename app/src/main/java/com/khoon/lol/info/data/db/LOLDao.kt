package com.khoon.lol.info.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.khoon.lol.info.model.ProfileEntity
import com.khoon.lol.info.model.SearchEntity
import com.khoon.lol.info.model.SummonerEntity

@Dao
interface LoLDao {
    @Query("SELECT * FROM Summoner")
    fun getSummoner(): LiveData<List<SummonerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSummoner(summonerEntity: SummonerEntity)

    @Update
    suspend fun updateSummoner(summonerEntity: SummonerEntity)

    @Delete
    suspend fun deleteSummoner(summonerEntity: SummonerEntity)

    @Query("DELETE FROM Summoner")
    suspend fun deleteSummonerAll()

    //Search

    @Query("SELECT * FROM Search")
    fun getSearch(): LiveData<List<SearchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(searchEntity: SearchEntity)

    @Delete
    suspend fun deleteSearch(searchEntity: SearchEntity)

    @Query("DELETE FROM Search")
    suspend fun deleteSearchAll()

    @Query("SELECT * FROM profile")
    fun getProfile(): LiveData<ProfileEntity>

    @Insert
    suspend fun insertProfile(profileEntity: ProfileEntity)

    @Update
    suspend fun updateProfile(profileEntity: ProfileEntity)

    @Query("DELETE FROM profile")
    suspend fun deleteProfile()


}