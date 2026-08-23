package com.khoon.lol.info.ui.components

import androidx.compose.foundation.lazy.LazyListState

/**
 * Scroll list back to the first item.
 *
 * Called from a coroutine scope (e.g. coroutineScope.launch { listState.scrollToTop() }).
 */
suspend fun LazyListState.scrollToTop() {
    animateScrollToItem(0)
}

