package com.khoon.lol.info.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "profile")
data class ProfileEntity(
    @PrimaryKey
    @ColumnInfo(name = "summoner_id")
    val summonerId: String, // Unique Riot summoner ID

    @ColumnInfo(name = "summoner_name")
    val summonerName: String,

    @ColumnInfo(name = "profile_icon_id")
    val profileIconId: Int,

    @ColumnInfo(name = "summoner_level")
    val summonerLevel: Int,

    @ColumnInfo(name = "rank")
    val rank: String? = null,

    @ColumnInfo(name = "tier")
    val tier: String? = null
)