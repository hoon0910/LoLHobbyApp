package com.khoon.lol.info.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.res.stringResource
import com.khoon.lol.info.R

@Composable
fun ChampionListWithFab(
    listState: LazyListState,
    onScrollToTop: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val showFab by remember(listState) {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0
        }
    }

    Scaffold(
        floatingActionButton = {
            if (showFab) {
                FloatingActionButton(onClick = onScrollToTop) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = stringResource(R.string.cd_scroll_to_top)
                    )
                }
            }
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}