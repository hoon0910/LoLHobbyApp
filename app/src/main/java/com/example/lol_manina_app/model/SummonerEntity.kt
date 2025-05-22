package com.example.lol_manina_app.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
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