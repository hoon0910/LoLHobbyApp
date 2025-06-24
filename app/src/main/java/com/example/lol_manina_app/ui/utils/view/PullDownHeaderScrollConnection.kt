package com.example.lol_manina_app.ui.utils.view

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

class PullDownHeaderScrollConnection(
    private val listState: LazyListState,
    private val onShowHeader: (Boolean) -> Unit
) : NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        if (listState.firstVisibleItemIndex == 0
            && listState.firstVisibleItemScrollOffset == 0 && available.y > 0) {
            onShowHeader(true)
        }
        if (available.y < 0) {
            onShowHeader(false)
        }
        return Offset.Zero
    }
} 