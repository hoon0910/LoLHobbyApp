package com.khoon.lol.info.data.db

import androidx.room.TypeConverter
import com.khoon.lol.info.model.ChampionDetail
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