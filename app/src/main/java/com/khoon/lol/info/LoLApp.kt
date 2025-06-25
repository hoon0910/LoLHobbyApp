package com.khoon.lol.info

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class LoLApp: Application() {

    companion object {
        lateinit var INSTANCE: LoLApp

    }

    init {
        INSTANCE = this
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("khoon", "application onCreate")
        INSTANCE = this
    }

    class AppPrefUtil @Inject constructor(
        private val prefs: SharedPreferences
    ) {
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