package com.example.lol_manina_app.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "search")
data class SearchEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "summoner_name")
    val summonerName: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long // e.g., System.currentTimeMillis()
)