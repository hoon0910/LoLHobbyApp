package com.khoon.lol.info.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatProgressBar(
    modifier: Modifier = Modifier,
    progress: Float,
    color: Color,
    label: String,
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp.value
    val isLocalInspectionMode = LocalInspectionMode.current
    var progressWidth by remember {
        mutableFloatStateOf(
            if (isLocalInspectionMode) {
                screenWidth
            } else {
                0f
            },
        )
    }

    var textWidth by remember { mutableIntStateOf(0) }
    val threshold = 16
    val segmentWidth = progressWidth / 10f
    val filledSegments = (progress * 10).toInt()
    
    // State for animation
    var animatedSegments by remember { mutableStateOf(0) }
    
    // Animation effect
    LaunchedEffect(filledSegments) {
        animatedSegments = 0
        for (i in 0..filledSegments) {
            animatedSegments = i
            kotlinx.coroutines.delay(100) // 100ms delay for each segment
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(24.dp)
            .onSizeChanged { progressWidth = it.width.toFloat() }
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(64.dp),
            )
            .clip(RoundedCornerShape(64.dp)),
    ) {
        // Draw segments
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            repeat(10) { index ->
                val isFilled = index < animatedSegments
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(24.dp)
                        .background(
                            color = if (isFilled) color else MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(64.dp)
                        )
                )
            }
        }

        // Draw label
        if (filledSegments > 0) {
            val isInner = filledSegments * segmentWidth > (textWidth + threshold * 2)
            
            if (isInner) {
                Text(
                    modifier = Modifier
                        .onSizeChanged { textWidth = it.width }
                        .align(Alignment.CenterEnd)
                        .padding(end = (threshold * 2).pxToDp()),
                    text = label,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.surface,
                )
            } else {
                Text(
                    modifier = Modifier
                        .onSizeChanged { textWidth = it.width }
                        .align(Alignment.CenterStart)
                        .padding(
                            start = (filledSegments * segmentWidth).toInt().pxToDp() + threshold.pxToDp(),
                        ),
                    text = label,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

private fun Int.pxToDp() = this.dp 