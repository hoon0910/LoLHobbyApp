package com.example.lol_manina_app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.lol_manina_app.model.ChampionEntity

@Composable
fun ChampionImage(
    champion: ChampionEntity,
    onChampionClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val roundedCornerShape = RoundedCornerShape(16.dp)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp)
            .clickable {
                onChampionClick(champion.name, champion.imagePath ?: "")
            },
        shape = roundedCornerShape
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(2.dp)
        ) {
            ChampionImageLoader(
                imagePath = champion.imagePath,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(roundedCornerShape)
            )
            Text(
                text = champion.name,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
} 