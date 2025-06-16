package com.example.lol_manina_app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lol_manina_app.model.ChampionDetail

@Composable
fun InfoSection(
    detail: ChampionDetail?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Info",
            style = MaterialTheme.typography.titleLarge,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        InfoStatItem(
            title = "Difficulty",
            value = detail?.info?.difficulty ?: 0,
            color = Color(0xFFE91E63),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        InfoStatItem(
            title = "Attack",
            value = detail?.info?.attack ?: 0,
            color = Color(0xFFFF5722),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        InfoStatItem(
            title = "Magic",
            value = detail?.info?.magic ?: 0,
            color = Color(0xFF2196F3),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        InfoStatItem(
            title = "Defence",
            value = detail?.info?.defense ?: 0,
            color = Color(0xFF4CAF50),
            modifier = Modifier.fillMaxWidth()
        )
    }
} 