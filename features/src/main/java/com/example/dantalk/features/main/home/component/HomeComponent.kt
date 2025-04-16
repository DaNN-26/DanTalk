package com.example.dantalk.features.main.home.component

import com.arkivanov.decompose.value.Value
import com.example.dantalk.mvi.main.home.HomeIntent
import com.example.dantalk.mvi.main.home.HomeState

interface HomeComponent {
    val state: Value<HomeState>

    fun processIntent(intent: HomeIntent)
}