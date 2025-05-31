package com.example.lol_manina_app.model

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import androidx.compose.ui.platform.LocalDensity

@Composable
fun ChampionDetailScreen(viewModel: ChampionDetailViewModel = hiltViewModel(),
                         name: String, imageUrl: String) {
    val detail = viewModel.championDetail.collectAsState().value
    LaunchedEffect(name) {
        viewModel.loadChampionJsonData(name)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.statusBars.asPaddingValues()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Fixed top section with image and name in a frame
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFF2B2B2B),
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomStart = 20.dp,
                        bottomEnd = 20.dp
                    )
                )
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = name,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
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
            Log.d("khoon", "Detail = ${detail?.info?.difficulty}")
            detail?.let {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(it.title, fontSize = 30.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(it.lore, fontSize = 20.sp)
                }
            } ?: Text("Loading...")

            Text("Difficulty: ${detail?.info?.difficulty?:0}/10", fontSize = 20.sp)
            SegmentedBarGauge(detail?.info?.difficulty ?:0)
            Spacer(modifier = Modifier.height(10.dp))
            Text("Attack: ${detail?.info?.attack?:0}/10", fontSize = 20.sp)
            SegmentedBarGauge(detail?.info?.attack ?:0)
            Spacer(modifier = Modifier.height(10.dp))
            Text("Magic: ${detail?.info?.magic?:0}/10", fontSize = 20.sp)
            SegmentedBarGauge(detail?.info?.magic ?:0)
            Spacer(modifier = Modifier.height(10.dp))
            Text("Defence: ${detail?.info?.defense?:0}/10", fontSize = 20.sp)
            SegmentedBarGauge(detail?.info?.defense ?:0)

            Spacer(modifier = Modifier.height(20.dp))
            detail?.passive?.let { passive ->
                Text("Passive: ${passive.name}", fontSize = 24.sp, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(passive.description, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

@Composable
fun SegmentedBarGauge(difficulty: Int, max: Int = 10) {

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 8.dp, start = 16.dp)) {
        Row {
            repeat(max) { index ->
                Box(
                    modifier = Modifier
                        .size(24.dp, 40.dp)
                        .padding(1.dp)
                        .background(
                            color = if (index < difficulty) Color.Red else Color.Blue,
                            shape = RoundedCornerShape(5.dp)
                        )
                )
            }
        }
    }
}