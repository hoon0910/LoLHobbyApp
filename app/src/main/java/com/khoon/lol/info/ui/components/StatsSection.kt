package com.khoon.lol.info.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khoon.lol.info.model.ChampionDetail

@Composable
fun StatsSection(
    detail: ChampionDetail?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Stats",
            style = MaterialTheme.typography.titleLarge,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CircularStatItem(
                title = "HP",
                value = detail?.stats?.hp ?: 0f,
                color = Color(0xFF4CAF50),
                modifier = Modifier.weight(1f)
            )

            CircularStatItem(
                title = "MP",
                value = detail?.stats?.mp ?: 0f,
                color = Color(0xFF2196F3),
                modifier = Modifier.weight(1f)
            )

            CircularStatItem(
                title = "Attack Damage",
                value = detail?.stats?.attackdamage ?: 0f,
                color = Color(0xFFE91E63),
                modifier = Modifier.weight(1f)
            )

            CircularStatItem(
                title = "Armor",
                value = detail?.stats?.armor ?: 0f,
                color = Color(0xFFFF9800),
                modifier = Modifier.weight(1f)
            )
        }
    }
} 