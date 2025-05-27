package com.example.lol_manina_app.data.db

import androidx.room.TypeConverter
import com.example.lol_manina_app.model.ChampionDetail
import com.google.gson.Gson

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromChampionDetail(detail: ChampionDetail): String {
        return gson.toJson(detail)
    }

    @TypeConverter
    fun toChampionDetail(json: String): ChampionDetail {
        return gson.fromJson(json, ChampionDetail::class.java)
    }
}