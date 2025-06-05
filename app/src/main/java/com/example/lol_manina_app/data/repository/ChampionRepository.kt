package com.example.lol_manina_app.data.repository

import android.content.Context
import android.util.Log
import com.example.lol_manina_app.data.api.DataDragonAPI
import com.example.lol_manina_app.data.api.LeagueOfLegendAPI
import com.example.lol_manina_app.data.db.ChampionDao
import com.example.lol_manina_app.di.IoDispatcher
import com.example.lol_manina_app.model.ChampionEntity
import com.example.lol_manina_app.model.ChampionDetail
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

interface ChampionRepository {
    fun getAllChampions(): Flow<List<ChampionEntity>>
    suspend fun insertChampion(champion: ChampionEntity)
    suspend fun updateChampion(champion: ChampionEntity)
    suspend fun fetchChampionData()
    suspend fun fetchChampionRotation(): List<String>
    suspend fun saveChampionImage(championName: String, version: String)
    suspend fun getChampionDetail(name: String): ChampionDetail?
}

@Singleton
class ChampionRepositoryImpl @Inject constructor(
    private val championDao: ChampionDao,
    private val dataDragonApiService: DataDragonAPI,
    private val riotApiService: LeagueOfLegendAPI,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context
) : ChampionRepository {

    override fun getAllChampions(): Flow<List<ChampionEntity>> {
        return championDao.getAllChampions()
    }

    override suspend fun insertChampion(champion: ChampionEntity) {
        withContext(ioDispatcher) {
            championDao.insert(champion)
        }
    }

    override suspend fun updateChampion(champion: ChampionEntity) {
        withContext(ioDispatcher) {
            championDao.updateChampion(champion)
        }
    }

    override suspend fun fetchChampionData() {
        withContext(ioDispatcher) {
            val versionResponse = dataDragonApiService.getVersions()
            if (versionResponse.isSuccessful) {
                val version = versionResponse.body()?.firstOrNull() ?: "13.9.1"
                val championResponse = dataDragonApiService.getChampionData(version)
                if (championResponse.isSuccessful) {
                    val champions = championResponse.body()?.data
                    champions?.forEach { (_, info) ->
                        // Check if champion already exists in DB before saving
                        val existingChampion = championDao.getChampionByName(info.id)
                        if (existingChampion == null) {
                            saveChampionImage(info.id, version)
                        }
                    }
                }
            }
        }
    }

    override suspend fun fetchChampionRotation(): List<String> {
        return withContext(ioDispatcher) {
            val response = riotApiService.getChampionRotation()
            if (response.isSuccessful) {
                response.body()?.freeChampionIds?.map { id ->
                    // TODO: Implement champion ID to name mapping
                    id.toString()
                } ?: listOf("No champions found")
            } else {
                listOf("Error: ${response.message()}")
            }
        }
    }

    override suspend fun saveChampionImage(championName: String, version: String) {
        withContext(ioDispatcher) {
            // Check if champion exists
            val existingChampion = championDao.getChampionByName(championName)
            
            // Check if image exists
            val imageFile = File(context.filesDir, "$championName.png")
            val shouldSaveImage = existingChampion == null || !imageFile.exists()
            
            if (shouldSaveImage) {
                // Get image from server if champion is new or image doesn't exist
                val filePath = saveImageLocally(context, championName, version)

                filePath?.let {
                    val championEntity = if (existingChampion == null) {
                        ChampionEntity(
                            name = championName,
                            imagePath = it
                        )
                    } else {
                        existingChampion.copy(imagePath = it)
                    }

                    if (existingChampion == null) {
                        championDao.insert(championEntity)
                        Log.d("khoon", "New champion image saved: $championName")
                    } else {
                        championDao.updateChampion(championEntity)
                        Log.d("khoon", "Missing champion image restored: $championName")
                    }
                }
            } else {
                Log.d("khoon", "Champion image already exists: $championName")
            }
        }
    }

    private suspend fun saveImageLocally(context: Context, championName: String, version: String): String? {
        Log.d("khoon", "SaveImageLocally fn is called")
        Log.d("khoon", "SaveImageLocally:: champion: $championName, version: $version")

        return try {
            val response = dataDragonApiService.getChampionImage(version, championName)
            if (!response.isSuccessful) {
                Log.d("khoon", "response Failed")
                return null
            }
            Log.d("khoon", "response is SuccessFul")

            val file = File(context.filesDir, "$championName.png")
            response.body()?.byteStream()?.use { inputStream ->
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

    override suspend fun getChampionDetail(name: String): ChampionDetail? {
        return withContext(ioDispatcher) {
            // 1. Check champion info in DB
            val dbResult = championDao.getChampionByName(name)
            
            // 2. If exists in DB, check detail info
            if (dbResult != null && dbResult.detail != null) {
                return@withContext dbResult.detail
            }

            // 3. If not in DB or no detail, fetch from API
            try {
                val versionResponse = dataDragonApiService.getVersions()
                val version = versionResponse.body()?.firstOrNull() ?: "13.9.1"
                val response = dataDragonApiService.getChampionDetail(version, name)
                val detail = response.data[name]
                
                if (detail != null) {
                    // 4. Save to DB
                    val championEntity = if (dbResult != null) {
                        dbResult.copy(detail = detail)
                    } else {
                        ChampionEntity(
                            name = name,
                            imagePath = null,
                            detail = detail
                        )
                    }
                    championDao.insert(championEntity)
                    detail
                } else null
            } catch (e: Exception) {
                Log.e("ChampionRepository", "Failed to fetch champion detail: ${e.message}")
                null
            }
        }
    }
}