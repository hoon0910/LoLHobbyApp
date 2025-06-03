package com.example.lol_manina_app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.lol_manina_app.R

@Composable
fun ChampionImageLoader(
    imagePath: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = if (imagePath != null) {
                ImageRequest.Builder(context)
                    .data(imagePath)
                    .crossfade(true)
                    .build()
            } else {
                ImageRequest.Builder(context)
                    .data(R.drawable.no_image)
                    .crossfade(true)
                    .build()
            },
            contentDescription = "Champion Image",
            contentScale = contentScale,
            modifier = Modifier.fillMaxSize()
        )
    }
} 