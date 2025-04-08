package com.example.mvi.main.search

sealed class SearchIntent {
    class OnQueryChange(val query: String) : SearchIntent()
    data object NavigateBack : SearchIntent()
}