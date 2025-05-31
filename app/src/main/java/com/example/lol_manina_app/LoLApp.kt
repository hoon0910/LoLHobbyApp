package com.example.lol_manina_app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.google.gson.Gson
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LoLApp: Application() {

    companion object{
        lateinit var INSTANCE: LoLApp
        lateinit var pref: AppPrefUtil
        fun getContext(): Context = INSTANCE.applicationContext
        fun getGson(): Gson = Gson()
    }

    init {
        INSTANCE = this
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("khoon", "application onCreate")
        INSTANCE = this
        pref = AppPrefUtil(applicationContext)
    }


    class AppPrefUtil(context: Context) {
        private val prefs: SharedPreferences =
            context.getSharedPreferences("app_pref", MODE_PRIVATE)

        fun getApikey() = prefs.getString("apikey", "")

        fun setApikey(value: String) {
            prefs.edit() { putString("apikey", value) }
        }

        fun delApikey() {
            prefs.edit() { remove("apikey") }
        }

        fun setVersion(value: String, str: String) {
            prefs.edit() { putString("gameversion", value) }
        }

        fun getVersion() = prefs.getString("gameversion", "13.9.1")

    }

}