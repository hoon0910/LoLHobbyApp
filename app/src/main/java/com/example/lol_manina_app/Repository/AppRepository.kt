package com.example.lol_manina_app.Repository

import com.example.lol_manina_app.LoLApp
import com.example.lol_manina_app.data.api.LeagueOfLegendAPI
import com.example.lol_manina_app.data.db.LoLDao
import com.example.lol_manina_app.model.CurrentGameInfo
import com.example.lol_manina_app.model.LeagueEntryDTO
import com.example.lol_manina_app.model.ProfileEntity
import com.example.lol_manina_app.model.SearchEntity
import com.example.lol_manina_app.model.Summoner
import com.example.lol_manina_app.model.SummonerEntity
import retrofit2.Response

class AppRepository constructor(private val dao: LoLDao, private val api: LeagueOfLegendAPI) {

    suspend fun searchSummoner(name: String, apiKey: String): Response<Summoner> = api.getSummoner(name,apiKey)

    suspend fun searchLeague(summonerId:String?, apiKey: String): Response<Set<LeagueEntryDTO>> = api.getLeague(summonerId,apiKey)

    suspend fun searchSpectator(summonerId:String?, apiKey: String): Response<CurrentGameInfo> = api.getSpectator(summonerId,apiKey)


    // Room Main
    fun getSummoner() = dao.getSummoner()

    suspend fun insertSummoner(summonerEntity: SummonerEntity){
        dao.insertSummoner(summonerEntity)
    }

    suspend fun updateSummoner(summonerEntity: SummonerEntity){
        dao.updateSummoner(summonerEntity)
    }

    suspend fun deleteSummoner(summonerEntity: SummonerEntity){
        dao.deleteSummoner(summonerEntity)
    }

    suspend fun deleteSummonerAll(){
        dao.deleteSummonerAll()
    }

    // Search
    fun getSearch() = dao.getSearch()

    suspend fun insertSearch(searchEntity: SearchEntity){
        dao.insertSearch(searchEntity)
    }

    suspend fun deleteSearch(searchEntity: SearchEntity){
        dao.deleteSearch(searchEntity)
    }

    suspend fun deleteSearchAll(){
        dao.deleteSearchAll()
    }

    // API KEY
    fun getApikey() = LoLApp.pref.getApikey()

    fun setApikey(value: String) {
        LoLApp.pref.setApikey(value)
    }

    fun delApikey() {
        LoLApp.pref.delApikey()
    }

    // Search
    fun getProfile() = dao.getProfile()

    suspend fun insertProfile(profileEntity: ProfileEntity){
        dao.insertProfile(profileEntity)
    }

    suspend fun updateProfile(profileEntity: ProfileEntity){
        dao.updateProfile(profileEntity)
    }

    suspend fun deleteProfile(){
        dao.deleteProfile()
    }

}