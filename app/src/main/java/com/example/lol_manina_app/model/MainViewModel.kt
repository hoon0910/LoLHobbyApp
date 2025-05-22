package com.example.lol_manina_app.model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _resultText = MutableStateFlow("")
    val resultText = _resultText.asStateFlow()

    fun onSearchTextChange(newText: String) {
        _searchText.value = newText
    }

    fun onSearchClick() {
        // You can implement search logic here
        _resultText.value = "You searched: ${_searchText.value}"
    }
}
