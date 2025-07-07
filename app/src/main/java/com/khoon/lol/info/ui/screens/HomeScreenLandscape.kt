package com.khoon.lol.info.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.khoon.lol.info.R

@Composable
fun HomeScreenLandscape(onImageClick: () -> Unit, onSummonerClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.azir_champion_page),
            contentDescription = "Splash",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clickable { onImageClick() }
        )
        Image(
            painter = painterResource(id = R.drawable.ekko_summoner_page),
            contentDescription = "Go to Summoner Page",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clickable { onSummonerClick() }
        )
    }
} 