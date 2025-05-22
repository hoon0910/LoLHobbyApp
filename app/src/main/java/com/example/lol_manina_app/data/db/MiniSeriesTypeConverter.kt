import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class MiniSeriesTypeConverter(private val gson: Gson) {

    data class MiniSeries(
        val wins: Int,
        val losses: Int,
        val target: Int,
        val progress: String // e.g., "WLLW"
    )

    @TypeConverter
    fun fromMiniSeries(series: MiniSeries?): String? {
        return gson.toJson(series)
    }

    @TypeConverter
    fun toMiniSeries(json: String?): MiniSeries? {
        if (json == null) return null
        val type = object : TypeToken<MiniSeries>() {}.type
        return gson.fromJson(json, type)
    }
}