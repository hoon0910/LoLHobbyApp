package com.example.lol_manina_app.model

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.lol_manina_app.utils.view.ChampionDetailActivity

@Composable
fun ChampionImage(champion: ChampionEntity) {
    val context = LocalContext.current
    AsyncImage(
        model = champion.imagePath,
        contentDescription = champion.name,
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                val intent = Intent(context, ChampionDetailActivity::class.java).apply {
                    putExtra("champion_name", champion.name)
                    putExtra("champion_image_url", champion.imagePath)
                }
                context.startActivity(intent)
            }
    )
}