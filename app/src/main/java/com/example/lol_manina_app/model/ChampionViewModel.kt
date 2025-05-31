package com.example.lol_manina_app.model

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.lol_manina_app.LoLApp.Companion.pref
import com.example.lol_manina_app.data.api.DataDragonAPI
import com.example.lol_manina_app.data.api.LeagueOfLegendAPI
import com.example.lol_manina_app.data.db.ChampionDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import kotlin.collections.component1
import kotlin.collections.component2

@HiltViewModel
class ChampionViewModel @Inject constructor(
    application: Application,
    private val championDao: ChampionDao,
    private val dataDragonApiService: DataDragonAPI,
    private val riotApiService: LeagueOfLegendAPI
) : AndroidViewModel(application) {
    private val _result = MutableStateFlow<String>("")
    private val _championList = MutableStateFlow<List<String>>(emptyList())
    private val _championMap = MutableStateFlow<Map<Int, String>>(emptyMap())
    val allChampions: LiveData<List<ChampionEntity>> = championDao.getAllChampions().asLiveData()

    init {
        fetchChampionData()
    }

    private fun fetchChampionData() {
        viewModelScope.launch {
            try {
                // 최신 버전 가져오기
                val versionResponse = dataDragonApiService.getVersions()
                if (versionResponse.isSuccessful) {
                    val version = versionResponse.body()?.firstOrNull() ?: "13.9.1"
                    val preVer = pref.getVersion()
                    Log.d("khoon", preVer.toString())
                    pref.setVersion(version, "13.9.1")

                    if (!preVer.equals(version)) {
                        val championResponse = dataDragonApiService.getChampionData(version)
                        if (championResponse.isSuccessful) {
                            val champions = championResponse.body()?.data
                            champions?.forEach { (_, info) ->
                                saveChampionImage(info.id, version)
                            }
                        }
                    } else {
                        Log.d("khoon", "version is same as before")
                    }
                } else {
                    Log.d("khoon", _result.value)
                }
            } catch (e: Exception) {
                println("Error fetching champion data: ${e.message}")
                Log.e("khoon", e.message.toString())
            }
        }
    }

    fun addOrUpdateChampion(id: Int, name: String) {
        Log.d("khoon", "addOrUpdateChampion : " + name)
        _championMap.value = _championMap.value.toMutableMap().apply {
            this[id] = name
        }
    }

    fun fetchChampionRotation() {
        viewModelScope.launch {
            try {
                val response = riotApiService.getChampionRotation()
                if (response.isSuccessful) {
                    val rotation = response.body()
                    val champions = rotation?.freeChampionIds?.map { id ->
                        _championMap.value[id]
                    } ?: listOf("No champions found")

                    _championList.value = champions as List<String>
                    Log.d("khoon", "fetchChampionRotation Success :  " + _championList.value)
                } else {
                    _championList.value = listOf("Error: ${response.message()}")
                    Log.d("khoon", "fetchChampionRotation error")
                }
            } catch (e: Exception) {
                _championList.value = listOf("Network error: ${e.message}")
                Log.d("khoon", "fetchChampionRotation exception")
            }
        }
    }

    fun saveChampionImage(championName: String, version: String) {
        Log.d("khoon", "called saveChampionImage Fn Champ name: $championName")
        viewModelScope.launch(Dispatchers.IO) {
            val imageUrl = "https://ddragon.leagueoflegends.com/cdn/$version/img/champion/$championName.png"
            val filePath = saveImageLocally(getApplication(), imageUrl, championName)

            filePath?.let {
                val championEntity = ChampionEntity(
                    name = championName,
                    imagePath = it
                )
                championDao.insert(championEntity)
                Log.d("khoon", "insert  ${championName}")
            }
        }
    }

    fun saveImageLocally(context: Context, imageUrl: String, fileName: String): String? {
        Log.d("khoon", "SaveImageLocally fn is called")
        Log.d("khoon", "SaveImageLocally:: image $imageUrl")

        return try {
            val client = OkHttpClient()
            val request = Request.Builder().url(imageUrl).build()
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                Log.d("khoon", "response Failed")
                return null
            }
            Log.d("khoon", "response is SuccessFul")

            val file = File(context.filesDir, "$fileName.png")
            response.body?.byteStream()?.use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            file.absolutePath
        } catch (e: IOException) {
            Log.d("khoon", "IOException ${e.message}")
            e.printStackTrace()
            null
        }
    }

    fun toggleFavorite(champion: ChampionEntity) = viewModelScope.launch {
        val updatedChampion = champion.copy(isFavorite = !champion.isFavorite)
        championDao.updateChampion(updatedChampion)
    }
}