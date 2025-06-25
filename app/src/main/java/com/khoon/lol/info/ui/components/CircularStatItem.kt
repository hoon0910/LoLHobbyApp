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

private const val MAX_HP_MP = 1000f
private const val MAX_ATTACK_ARMOR = 200f

@Composable
fun CircularStatItem(
    title: String,
    value: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    val maxValue = when (title) {
        "HP", "MP" -> MAX_HP_MP
        else -> MAX_ATTACK_ARMOR
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressBar(
            progress = value / maxValue,
            color = color,
            value = value.toInt().toString(),
            label = title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 20.sp
        )
    }
} 