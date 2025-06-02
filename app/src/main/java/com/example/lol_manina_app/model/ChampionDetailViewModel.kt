package com.example.lol_manina_app.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lol_manina_app.LoLApp.AppPrefUtil
import com.example.lol_manina_app.data.api.DataDragonAPI
import com.example.lol_manina_app.data.db.ChampionDao
import com.example.lol_manina_app.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ChampionDetailViewModel @Inject constructor(
    application: Application,
    private val championDao: ChampionDao,
    private val dataDragonApiService: DataDragonAPI,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val appPrefUtil: AppPrefUtil
) : AndroidViewModel(application) {
    private val _championDetail = MutableStateFlow<ChampionDetail?>(null)
    val championDetail: StateFlow<ChampionDetail?> = _championDetail

    fun loadChampionJsonData(name: String) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                try {
                    // 1. Check champion info in DB
                    val dbResult = championDao.getChampionByName(name)
                    
                    // 2. If exists in DB, check detail info
                    if (dbResult != null) {
                        if (dbResult.detail != null) {
                            // Use detail from DB if available
                            _championDetail.value = dbResult.detail
                            Log.d("khoon", "Using champion detail from DB for $name")
                            return@withContext
                        }
                    }

                    // 3. If not in DB or no detail, fetch from API
                    Log.d("khoon", "Fetching champion detail from API for $name")
                    val response = dataDragonApiService.getChampionDetail(appPrefUtil.getVersion().toString(), name)
                    val detail = response.data[name]
                    
                    if (detail != null) {
                        Log.d("khoon", "Successfully fetched champion detail for $name")
                        // 4. Save to DB
                        val championEntity = if (dbResult != null) {
                            dbResult.copy(detail = detail)
                        } else {
                            // Check if image file exists
                            val imageFile = File(getApplication<Application>().filesDir, "$name.png")
                            ChampionEntity(
                                name = name,
                                imagePath = (if (imageFile.exists()) imageFile.absolutePath else null),
                                detail = detail
                            )
                        }
                        championDao.insert(championEntity)
                        _championDetail.value = detail
                        Log.d("khoon", "Updated champion detail in UI for $name")
                    } else {
                        Log.e("ChampionVM", "Failed to get champion detail for $name")
                    }
                } catch (e: Exception) {
                    Log.e("ChampionVM", "Failed to fetch champion data: ${e.localizedMessage}")
                }
            }
        }
    }
}