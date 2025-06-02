package com.example.lol_manina_app.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "champion_table")
data class ChampionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    val imagePath: String? = null,
    val detail: ChampionDetail? = null, // default = null
    val isFavorite: Boolean = false, // default = false
)