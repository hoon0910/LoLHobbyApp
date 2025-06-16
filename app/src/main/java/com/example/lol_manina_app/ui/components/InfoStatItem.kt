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

@Composable
fun InfoStatItem(
    title: String,
    value: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 20.sp
            )
            Text(
                "$value/10",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 20.sp,
                color = color
            )
        }
        StatProgressBar(
            progress = value / 10f,
            color = color,
            label = ""
        )
    }
} 