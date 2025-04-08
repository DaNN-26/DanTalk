package com.example.dantalk.components.main.home

import com.arkivanov.decompose.value.Value
import com.example.mvi.main.home.HomeIntent
import com.example.mvi.main.home.HomeState

interface HomeComponent {
    val state: Value<HomeState>

    fun processIntent(intent: HomeIntent)
}