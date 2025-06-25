package com.khoon.lol.info.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "summoner")
data class SummonerEntity(
    @PrimaryKey val id: String,

    @ColumnInfo(name = "summoner_name")
    val name: String,

    val level: Int,

    //@Ignore
    //val tempField: String = "" // will not be saved
)