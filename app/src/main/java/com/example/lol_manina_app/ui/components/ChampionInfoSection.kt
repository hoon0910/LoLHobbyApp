package com.example.lol_manina_app.ui.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lol_manina_app.model.ChampionDetail

@Composable
fun ChampionInfoSection(
    detail: ChampionDetail?,
    name: String,
    modifier: Modifier = Modifier
) {
    detail?.let {
        Log.d("khoon", "Rendering champion detail for $name")
        Column(modifier = modifier.padding(16.dp)) {
            Text(it.title, fontSize = 30.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Text(it.lore, fontSize = 20.sp)
        }
    } ?: run {
        Log.d("khoon", "Waiting for champion detail for $name")
        Text("Loading...")
    }
} 