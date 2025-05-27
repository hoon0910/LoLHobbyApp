package com.example.lol_manina_app.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lol_manina_app.LoLApp.Companion.pref
import com.example.lol_manina_app.data.api.NetworkManager
import com.example.lol_manina_app.data.db.ChampionDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChampionDetailViewModel (application: Application) : AndroidViewModel(application){
    private val championDao = ChampionDatabase.getDatabase(application).championDao()
    private val _championDetail = MutableStateFlow<ChampionDetail?>(null)
    val championDetail: StateFlow<ChampionDetail?> = _championDetail


    fun  loadChampionJsonData(name: String) {

        viewModelScope.launch {
            val dbResult = championDao.getChampionByName(name)

            if (dbResult == null) {
                Log.d("ChampionVM", "Champion '$name' not found in DB. Skipping update.")
                return@launch
            }
            if (dbResult.detail == null) {
                Log.d("khoon", "This &$name champ does not have json data")

                try {
                    val response = NetworkManager.dataDragonApiService.
                    getChampionDetail(pref.getVersion().toString(), name)
                    val detail = response.data[name]
                    Log.d("khoon", "Difficulty ${detail?.info?.difficulty}")

                    val updated = dbResult.copy(detail = detail)
                    championDao.insert(updated)
                    _championDetail.value = detail

                } catch (e: Exception) {
                    Log.e("ChampionVM", "Failed to fetch from network: ${e.localizedMessage}")
                }
            } else _championDetail.value= dbResult.detail

        }
    }

}