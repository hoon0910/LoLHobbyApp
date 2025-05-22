package com.example.lol_manina_app.utils.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.lol_manina_app.model.ChampionDetailScreen

class ChampionDetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get data from Intent
        val name = intent.getStringExtra("champion_name") ?: "Unknown"
        val imageUrl = intent.getStringExtra("champion_image_url") ?: ""

        setContent {
            ChampionDetailScreen(name = name, imageUrl = imageUrl)
        }
    }
}
