package com.example.dantalk.components.main.search

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.example.mvi.main.search.SearchIntent
import com.example.mvi.main.search.SearchState

class DefaultSearchComponent(
    componentContext: ComponentContext,
    private val navigateBack: () -> Unit
) : ComponentContext by componentContext, SearchComponent {

    private val _state = MutableValue(
        stateKeeper.consume(SEARCH_COMPONENT, SearchState.serializer()) ?: SearchState()
    )

    override val state = _state

    override fun processIntent(intent: SearchIntent) {
        when(intent) {
            is SearchIntent.OnQueryChange -> _state.update { it.copy(query = intent.query) }
            is SearchIntent.NavigateBack -> navigateBack()
        }
    }

    companion object {
        const val SEARCH_COMPONENT = "SEARCH_COMPONENT"
    }
}