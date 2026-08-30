package com.khoon.lol.info.data.repository

import android.content.Context
import android.util.Log
import com.khoon.lol.info.data.api.DataDragonAPI
import com.khoon.lol.info.data.api.LeagueOfLegendAPI
import com.khoon.lol.info.data.db.ChampionDao
import com.khoon.lol.info.di.DispatcherModule
import com.khoon.lol.info.model.ChampionDetail
import com.khoon.lol.info.model.ChampionEntity
import com.khoon.lol.info.R
import com.khoon.lol.info.utils.constant.AppConstant.API_KEY
import com.khoon.lol.info.utils.constant.AppConstant.TAG
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

interface ChampionRepository {
    fun getAllChampions(): Flow<List<ChampionEntity>>
    suspend fun insertChampion(champion: ChampionEntity): ChampionEntity
    suspend fun updateChampion(champion: ChampionEntity): Int
    suspend fun getChampionByName(name: String): ChampionEntity?
    fun observeChampionByName(name: String): Flow<ChampionEntity?>
    suspend fun fetchChampionData()
    suspend fun fetchChampionRotation(): List<String>
    suspend fun saveChampionImage(championName: String, version: String)
    suspend fun getChampionDetail(name: String): ChampionDetail?
    suspend fun fetchRotationChampionImages(): List<Pair<String, String?>>
    /** Map free-rotation champion IDs to name + local image path (DataDragon + DB). */
    suspend fun resolveRotationImages(freeChampionIds: List<Int>): List<Pair<String, String?>>
}

@Singleton
class ChampionRepositoryImpl @Inject constructor(
    private val championDao: ChampionDao,
    private val dataDragonApiService: DataDragonAPI,
    private val riotApiService: LeagueOfLegendAPI,
    @ApplicationContext private val context: Context
) : ChampionRepository {

    override fun getAllChampions(): Flow<List<ChampionEntity>> {
        return championDao.getAllChampions()
    }

    override suspend fun insertChampion(champion: ChampionEntity): ChampionEntity {
        return withContext(DispatcherModule.provideIoDispatcher()) {
            val id = championDao.insert(champion) // get ID.
            val insertedChampion = champion.copy(id = id.toInt()) // Create New Id
            Log.d(TAG, "Inserted champion: ${insertedChampion.name} with ID: ${insertedChampion.id}")
            insertedChampion // return inserted champion Entity
        }
    }

    override suspend fun updateChampion(champion: ChampionEntity): Int {
        return withContext(DispatcherModule.provideIoDispatcher()) {
            // update returns number of rows affected
            val rowsAffected = championDao.updateChampion(champion)
            Log.d(TAG, "Updated champion: ${champion.name} (ID: ${champion.id}) " +
                    "with favorite: ${champion.isFavorite}. Rows affected: $rowsAffected")
            rowsAffected //
        }
    }

    override suspend fun getChampionByName(name: String): ChampionEntity? {
        return withContext(DispatcherModule.provideIoDispatcher()) {
            championDao.getChampionByName(name)
        }
    }

    override fun observeChampionByName(name: String): Flow<ChampionEntity?> {
        return championDao.observeChampionByName(name)
    }

    override suspend fun fetchChampionData() {
        withContext(DispatcherModule.provideIoDispatcher()) {
            try {
                val versionResponse = dataDragonApiService.getVersions()
                if (versionResponse.isSuccessful) {
                    val version = versionResponse.body()?.firstOrNull() ?: "13.9.1"
                    
                    // Fetch champion list
                    val championResponse = dataDragonApiService.getChampionData(version)
                    if (championResponse.isSuccessful) {
                        val champions = championResponse.body()?.data
                        champions?.forEach { (championName) ->
                            // Check if champion already exists in DB
                            val existingChampion = championDao.getChampionByName(championName)
                            
                            // Fetch champion detail
                            try {
                                val detailResponse = dataDragonApiService
                                    .getChampionDetail(version, championName)
                                val championDetail = detailResponse.data[championName]
                                
                                // Fetch image path
                                val imagePath = if (existingChampion == null
                                    || existingChampion.imagePath == null) {
                                    saveImageLocally(context, championName, version)
                                } else {
                                    existingChampion.imagePath
                                }
                                
                                if (championDetail != null) {
                                    // Update existing champion with detail and image
                                    val championToSave = existingChampion?.
                                    copy(
                                        detail = championDetail,
                                        imagePath = imagePath ?: existingChampion.imagePath
                                    )
                                        ?: // Create new champion with detail and image
                                        ChampionEntity(
                                            name = championName,
                                            imagePath = imagePath,
                                            detail = championDetail
                                        )
                                    
                                    if (existingChampion != null) {
                                        championDao.updateChampion(championToSave)
                                        Log.d(TAG, "Updated champion with detail: $championName")
                                    } else {
                                        championDao.insert(championToSave)
                                        Log.d(TAG, "Created new champion with detail: $championName")
                                    }
                                }
                            } catch (e: Exception) {
                                Log.e(TAG, "Failed to fetch detail for $championName: ${e.message}")
                                // If detail fetch fails, still save basic champion info and image
                                if (existingChampion == null) {
                                    val imagePath = saveImageLocally(context, championName, version)
                                    val basicChampion = ChampionEntity(
                                        name = championName,
                                        imagePath = imagePath
                                    )
                                    championDao.insert(basicChampion)
                                    Log.d(TAG, "Created basic champion (no detail): $championName")
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching champion data: ${e.message}")
            }
        }
    }

    override suspend fun fetchChampionRotation(): List<String> {
        Log.d("khoon", "called fetchChampionRotation in cmapionReopsitory")
        return withContext(DispatcherModule.provideIoDispatcher()) {
            val response = riotApiService.getChampionRotation(API_KEY)
            if (response.isSuccessful) {
                Log.d("khoon", "isSuccessful")
                val freeIds = response.body()?.freeChampionIds ?: emptyList()
                // Load all champions from DB and map id to name
                val championList = championDao.getAllChampions().firstOrNull() ?: emptyList()
                val idToName = championList.mapNotNull { entity ->
                    val detailId = entity.detail?.id?.toIntOrNull()
                    if (detailId != null) detailId to entity.name else null
                }.toMap()
                val names = freeIds.mapNotNull { idToName[it] }
                names.ifEmpty {
                    freeIds.map { it.toString() }
                }
            } else {
                Log.d("khoon", "error")
                listOf(context.getString(R.string.error_with_message, response.message()))
            }
        }
    }

    override suspend fun saveChampionImage(championName: String, version: String) {
        withContext(DispatcherModule.provideIoDispatcher()) {
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
        return withContext(DispatcherModule.provideIoDispatcher()) {
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

    override suspend fun fetchRotationChampionImages(): List<Pair<String, String?>> {
        Log.d(TAG, "called fetchRotationChampionImages")

        return withContext(DispatcherModule.provideIoDispatcher()) {
            val response = riotApiService.getChampionRotation(API_KEY)
            Log.d(TAG, "response code: ${response.code()}, message: ${response.message()}")
            if (response.errorBody() != null) {
                Log.d(TAG, "errorBody: ${response.errorBody()?.string()}")
            }
            if (!response.isSuccessful) return@withContext emptyList()

            val freeIds = response.body()?.freeChampionIds ?: emptyList()
            resolveRotationImages(freeIds)
        }
    }

    override suspend fun resolveRotationImages(
        freeChampionIds: List<Int>
    ): List<Pair<String, String?>> {
        return withContext(DispatcherModule.provideIoDispatcher()) {
            if (freeChampionIds.isEmpty()) return@withContext emptyList()

            val version = runCatching {
                dataDragonApiService.getVersions().body()?.firstOrNull()
            }.getOrNull() ?: "13.9.1"

            val result = mutableListOf<Pair<String, String?>>()
            for (id in freeChampionIds) {
                val champion = getChampionIdAndDisplayName(id, version)
                if (champion != null) {
                    // Image/DB key = DataDragon id (LeeSin); UI label = display name (Lee Sin)
                    val imagePath = getChampionImagePath(champion.id, version)
                    result.add(champion.displayName to imagePath)
                    Log.d(TAG, "[RESULT] display=${champion.displayName}, id=${champion.id}, imagePath=$imagePath")
                } else {
                    result.add(id.toString() to null)
                    Log.d(TAG, "[RESULT] name=$id, imagePath=null (not found)")
                }
            }
            result
        }
    }

    // For rotation champions - only check image (championId = DataDragon map key)
    private suspend fun getChampionImagePath(championId: String, version: String): String? {
        val champion = championDao.getChampionByName(championId)
        return if (champion?.imagePath != null) {
            Log.d(TAG, "[DB] id=$championId, imagePath=${champion.imagePath}")
            champion.imagePath
        } else {
            Log.d(TAG, "[DD] id=$championId, version=$version (saving new image)")
            saveChampionImageAndReturnPath(championId, version)
        }
    }

    private data class ChampionIdDisplay(
        val id: String,
        val displayName: String,
    )

    private suspend fun getChampionIdAndDisplayName(id: Int, version: String): ChampionIdDisplay? {
        return try {
            val response = dataDragonApiService.getChampionData(version)
            if (response.isSuccessful) {
                val entry = response.body()?.data?.entries
                    ?.find { it.value.key == id.toString() }
                if (entry != null) {
                    ChampionIdDisplay(id = entry.key, displayName = entry.value.name).also {
                        Log.d(
                            "ChampionRepository",
                            "[getChampionIdAndDisplayName] id=$id, key=${it.id}, name=${it.displayName}"
                        )
                    }
                } else {
                    null
                }
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(
                "ChampionRepository",
                "[getChampionIdAndDisplayName] id=$id, version=$version, error=${e.message}"
            )
            null
        }
    }

    private suspend fun saveChampionImageAndReturnPath(championName: String, version: String): String? {
        return withContext(DispatcherModule.provideIoDispatcher()) {
            val filePath = saveImageLocally(context, championName, version)
            if (filePath != null) {
                val existingChampion = championDao.getChampionByName(championName)
                val championEntity = if (existingChampion == null) {
                    ChampionEntity(name = championName, imagePath = filePath)
                } else {
                    existingChampion.copy(imagePath = filePath)
                }
                if (existingChampion == null) {
                    championDao.insert(championEntity)
                } else {
                    championDao.updateChampion(championEntity)
                }
            }
            filePath
        }
    }
}