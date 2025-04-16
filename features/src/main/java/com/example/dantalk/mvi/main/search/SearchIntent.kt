package com.example.dantalk.mvi.main.search

sealed class SearchIntent {
    class OnQueryChange(val query: String) : com.example.dantalk.mvi.main.search.SearchIntent()
    data object NavigateBack : com.example.dantalk.mvi.main.search.SearchIntent()
}