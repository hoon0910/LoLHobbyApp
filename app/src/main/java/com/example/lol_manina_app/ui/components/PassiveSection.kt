package com.example.lol_manina_app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lol_manina_app.model.ChampionDetail

@Composable
fun PassiveSection(
    detail: ChampionDetail?,
    modifier: Modifier = Modifier
) {
    detail?.passive?.let { passive ->
        Column(modifier = modifier) {
            Text(
                "Passive: ${passive.name}",
                fontSize = 24.sp,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                passive.description,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
} 