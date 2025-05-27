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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@Composable
fun ChampionDetailScreen(viewModel: ChampionDetailViewModel = viewModel(),
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
            style = MaterialTheme.typography.headlineMedium
        )
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





    }
}

@Composable
fun SegmentedBarGauge(difficulty: Int, max: Int = 10) {

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {
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