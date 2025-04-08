package com.example.mvi.main.search

import kotlinx.serialization.Serializable

@Serializable
data class SearchState(
    val query: String = ""
)
