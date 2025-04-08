package com.example.dantalk.components.main.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.example.mvi.main.home.HomeIntent
import com.example.mvi.main.home.HomeState

class DefaultHomeComponent(
    componentContext: ComponentContext,
    private val navigateToSearch: () -> Unit,
    private val navigateToProfile: () -> Unit
) : ComponentContext by componentContext, HomeComponent {

    private val _state = MutableValue(
        stateKeeper.consume(HOME_COMPONENT, HomeState.serializer()) ?: HomeState()
    )

    override val state = _state

    override fun processIntent(intent: HomeIntent) {
        when(intent) {
            is HomeIntent.InitializeChats -> {}
            is HomeIntent.NavigateToSearch -> navigateToSearch()
            is HomeIntent.NavigateToProfile -> navigateToProfile()
        }
    }

    companion object {
        const val HOME_COMPONENT = "HOME_COMPONENT"
    }
}