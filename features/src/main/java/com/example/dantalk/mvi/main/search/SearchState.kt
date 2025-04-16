package com.example.dantalk.mvi.main.search

import kotlinx.serialization.Serializable

@Serializable
data class SearchState(
    val query: String = ""
)
