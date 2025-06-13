package com.example.lol_manina_app.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.lol_manina_app.R
import com.example.lol_manina_app.model.ChampionDetailViewModel
import com.example.lol_manina_app.ui.components.FavoriteButton
import com.example.lol_manina_app.ui.components.StatProgressBar

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ChampionDetailScreen(
    viewModel: ChampionDetailViewModel = hiltViewModel(),
    name: String,
    imageUrl: String?,
    favorite: Boolean,
    animatedVisibilityScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope
) {
    val championEntity = viewModel.championEntity.collectAsState().value
    val detail = championEntity?.detail

    LaunchedEffect(name) {
        viewModel.loadChampionJsonData(name)
    }

    Column(
        modifier = Modifier
           .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Fixed top section with image and name in a frame
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(
                        bottomStart = 20.dp,
                        bottomEnd = 20.dp
                    )
                )
                .padding(vertical = 24.dp),
        ) {
            with(sharedTransitionScope) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .sharedElement(
                            rememberSharedContentState(key = "champion_${name}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                        .sharedBounds(
                            rememberSharedContentState(key = "champion_bounds_${name}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                ) {
                    if (imageUrl != null) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = name,
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    } else {
                        AsyncImage(
                            model = R.drawable.no_image,
                            contentDescription = name,
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                }
            }
            
            // Favorite button
            with(sharedTransitionScope) {
                Box(modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .sharedElement(
                        rememberSharedContentState(key = "favorite_button_${name}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .sharedBounds(
                        rememberSharedContentState(key = "favorite_button_bounds_${name}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                ) {
                    FavoriteButton(
                        isFavorite = championEntity?.isFavorite ?: favorite,
                        onClick = {
                            championEntity?.let { entity ->
                                viewModel.toggleFavorite(entity)
                            }
                        },
                        modifier = Modifier.size(70.dp).padding(4.dp),
                        iconSize = 60.dp
                    )
                }
            }
        }

        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = WindowInsets.navigationBars.getBottom(LocalDensity.current).dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Stats section at the top
            Text("Stats", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Difficulty",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 20.sp
                    )
                    Text(
                        "${detail?.info?.difficulty ?: 0}/10",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 20.sp,
                        color = Color(0xFFE91E63)
                    )
                }
                StatProgressBar(
                    progress = (detail?.info?.difficulty ?: 0) / 10f,
                    color = Color(0xFFE91E63),
                    label = ""
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Attack",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 20.sp
                    )
                    Text(
                        "${detail?.info?.attack ?: 0}/10",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 20.sp,
                        color = Color(0xFFF44336)
                    )
                }
                StatProgressBar(
                    progress = (detail?.info?.attack ?: 0) / 10f,
                    color = Color(0xFFF44336),
                    label = ""
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Magic",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 20.sp
                    )
                    Text(
                        "${detail?.info?.magic ?: 0}/10",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 20.sp,
                        color = Color(0xFF2196F3)
                    )
                }
                StatProgressBar(
                    progress = (detail?.info?.magic ?: 0) / 10f,
                    color = Color(0xFF2196F3),
                    label = ""
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Defence",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 20.sp
                    )
                    Text(
                        "${detail?.info?.defense ?: 0}/10",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 20.sp,
                        color = Color(0xFF4CAF50)
                    )
                }
                StatProgressBar(
                    progress = (detail?.info?.defense ?: 0) / 10f,
                    color = Color(0xFF4CAF50),
                    label = ""
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            
            detail?.let {
                Log.d("khoon", "Rendering champion detail for $name")
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(it.title, fontSize = 30.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(it.lore, fontSize = 20.sp)
                }
            } ?: run {
                Log.d("khoon", "Waiting for champion detail for $name")
                Text("Loading...")
            }

            Spacer(modifier = Modifier.height(20.dp))
            detail?.passive?.let { passive ->
                Text("Passive: ${passive.name}", fontSize = 24.sp, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(passive.description, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}