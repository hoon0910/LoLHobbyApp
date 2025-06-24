package com.example.lol_manina_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BaseHeaderLayout(
    leftContent: @Composable RowScope.() -> Unit,
    showOnlyFavorites: Boolean,
    onToggleFavorites: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.White)
            .padding(horizontal = 8.dp)
    ) {
        // The left side content is passed in as a slot
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            leftContent()
        }
        
        // The right side content is common
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Only Favorite", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.width(8.dp))
            Switch(checked = showOnlyFavorites, onCheckedChange = { onToggleFavorites() })
        }
    }
} 