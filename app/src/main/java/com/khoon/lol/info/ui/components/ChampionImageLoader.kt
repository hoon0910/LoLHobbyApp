package com.khoon.lol.info.ui.components

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.khoon.lol.info.R

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ChampionImageLoader(
    imagePath: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    name: String? = null,
    animatedVisibilityScope: AnimatedContentScope? = null,
    sharedTransitionScope: SharedTransitionScope? = null
) {
    val context = LocalContext.current

    if (name != null && animatedVisibilityScope != null
        && sharedTransitionScope != null) {
        with(sharedTransitionScope) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .sharedElement(
                        rememberSharedContentState(key = "champion_${name}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .sharedBounds(
                        rememberSharedContentState(key = "champion_bounds_${name}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    SubcomposeAsyncImage(
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
                        modifier = Modifier.fillMaxSize(),
                        loading = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                        }
                    )
                }
            }
        }
    } else
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            SubcomposeAsyncImage(
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
                modifier = Modifier.fillMaxSize(),
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            )
        }
    }