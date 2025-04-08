package com.example.dantalk.components.main.search

import com.arkivanov.decompose.value.Value
import com.example.mvi.main.search.SearchIntent
import com.example.mvi.main.search.SearchState

interface SearchComponent {
    val state: Value<SearchState>

    fun processIntent(intent: SearchIntent)
}