package com.example.dantalk.features.main.search.component

import com.arkivanov.decompose.value.Value
import com.example.dantalk.mvi.main.search.SearchIntent
import com.example.dantalk.mvi.main.search.SearchState

interface SearchComponent {
    val state: Value<SearchState>

    fun processIntent(intent: SearchIntent)
}