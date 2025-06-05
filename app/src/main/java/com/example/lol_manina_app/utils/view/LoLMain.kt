package com.example.lol_manina_app.utils.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.lol_manina_app.model.ChampionEntity
import com.example.lol_manina_app.model.ChampionViewModel
import com.example.lol_manina_app.model.SummonerViewModel
import com.example.lol_manina_app.navigation.NavGraph
import com.example.lol_manina_app.ui.components.ChampionImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoLMain : ComponentActivity() {

    companion object { 
        
        const val TAG = "MyTag" 

    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MainCompose()
        }
    }
}

@Composable
fun MainCompose() {
    val navController = rememberNavController()
    Log.d("khoon", "called MainCompose")

    NavGraph(
        navController = navController,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                bottom = WindowInsets.navigationBars.getBottom(LocalDensity.current).dp
            )
    )
}

@Composable
fun ChampionListScreen(
    viewModel: ChampionViewModel = hiltViewModel(),
    onChampionClick: (String, String) -> Unit
) {
    val champions by viewModel.allChampions.observeAsState(emptyList())
    var searchQuery by remember { mutableStateOf("") }
    var showOnlyFavorites by remember { mutableStateOf(false) }

    val filteredList = champions
        .filter { it.name.contains(searchQuery, ignoreCase = true) }
        .filter { !showOnlyFavorites || it.isFavorite }

    Log.d("khoon", "initScreen")

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                bottom = WindowInsets.navigationBars.getBottom(LocalDensity.current).dp
            )
    ) {
        val (searchRow, content) = createRefs()
        
        Row(
            modifier = Modifier
                .constrainAs(searchRow) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Enter search a Champion") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear search",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Only Favorite")
                Spacer(modifier = Modifier.width(8.dp))
                Switch(checked = showOnlyFavorites, onCheckedChange = { showOnlyFavorites = it })
            }
        }

        Box(
            modifier = Modifier
                .constrainAs(content) {
                    top.linkTo(searchRow.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            when {
                champions.isEmpty() && searchQuery.isBlank() -> {
                    Text("No data")
                }
                filteredList.isEmpty() -> {
                    Text("No result data")
                }
                else -> {
                    ChampIconList(
                        filteredList = filteredList,
                        onFavoriteClick = { viewModel.toggleFavorite(it) },
                        onChampionClick = onChampionClick
                    )
                }
            }
        }
    }
}

@Composable
fun ChampIconList(
    filteredList: List<ChampionEntity>,
    modifier: Modifier = Modifier,
    onFavoriteClick: (ChampionEntity) -> Unit,
    onChampionClick: (String, String) -> Unit
) {
    val bottomPadding = WindowInsets.navigationBars.getBottom(LocalDensity.current).dp + 32.dp

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(bottom = bottomPadding)
    ) {
        items(filteredList.chunked(4)) { rowItems ->
            LazyRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(rowItems) { champion ->
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .padding(8.dp)
                    ) {
                        ChampionImage(
                            champion = champion,
                            onChampionClick = onChampionClick
                        )
                        IconButton(
                            onClick = { onFavoriteClick(champion) },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(4.dp)
                                .size(24.dp)
                        ) {
                            Icon(
                                imageVector = if (champion.isFavorite) Icons.Default.Star
                                else Icons.Default.StarBorder,
                                tint = Color.Yellow,
                                contentDescription = "Favorite"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchScreen(viewModel: SummonerViewModel = hiltViewModel(), modifier: Modifier) {
    var input by remember { mutableStateOf(TextFieldValue("")) }
    var result by remember { mutableStateOf("") }

    val context = LocalContext.current
    Column(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()) {
        TextField(
            value = input,
            onValueChange = { input = it },
            label = { Text(text = "Enter search keyword") },
            modifier = modifier.fillMaxWidth()
        )

        Spacer(modifier = modifier.height(16.dp))

        Button(
            onClick = { viewModel.fetchSummonerInfo(input.text)
                        Toast.makeText(context, input.text , Toast.LENGTH_SHORT).show()
                      },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Search")
        }

        Spacer(modifier = modifier.height(32.dp))

        if (result.isNotEmpty()) {

        }
    }


    fun chunkByMaxWidth(items: List<String>, maxWidth: Int, itemWidth: Int): List<List<String>> {
        val chunks = mutableListOf<MutableList<String>>()
        var currentChunk = mutableListOf<String>()
        var currentWidth = 0

        for (item in items) {
            val itemSize = itemWidth * item.length  // Estimating width by length

            if (currentWidth + itemSize <= maxWidth) {
                currentChunk.add(item)
                currentWidth += itemSize
            } else {
                chunks.add(currentChunk)
                currentChunk = mutableListOf(item)
                currentWidth = itemSize
            }
        }

        if (currentChunk.isNotEmpty()) {
            chunks.add(currentChunk)
        }

        return chunks
    }

}