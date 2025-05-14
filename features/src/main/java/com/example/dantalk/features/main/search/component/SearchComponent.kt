package com.example.dantalk.features.main.search.component

import com.example.dantalk.features.main.search.store.SearchStore
import kotlinx.coroutines.flow.StateFlow

interface SearchComponent {
    val state: StateFlow<SearchStore.State>

    fun onIntent(intent: SearchStore.Intent)
}