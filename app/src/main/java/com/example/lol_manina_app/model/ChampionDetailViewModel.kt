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
            val dbResult = championDao.getChampionByName(name)

            if (dbResult == null) {
                Log.d("ChampionVM", "Champion '$name' not found in DB. Skipping update.")
                return@launch
            }
            if (dbResult.detail == null) {
                Log.d("khoon", "This &$name champ does not have json data")

                try {
                    val response = dataDragonApiService.getChampionDetail(appPrefUtil.getVersion().toString(), name)
                    val detail = response.data[name]
                    Log.d("khoon", "Difficulty ${detail?.info?.difficulty}")

                    val updated = dbResult.copy(detail = detail)
                    championDao.insert(updated)
                    _championDetail.value = detail

                } catch (e: Exception) {
                    Log.e("ChampionVM", "Failed to fetch from network: ${e.localizedMessage}")
                }
            } else _championDetail.value = dbResult.detail
        }
    }
}