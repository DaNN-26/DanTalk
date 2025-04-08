package com.example.mvi.main.home

import kotlinx.serialization.Serializable

@Serializable
data class HomeState(
    val chats: List<String> = emptyList()
)
