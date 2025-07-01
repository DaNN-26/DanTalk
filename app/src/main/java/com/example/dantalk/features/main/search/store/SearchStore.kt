package com.example.dantalk.features.main.search.store

import com.arkivanov.mvikotlin.core.store.Store
import com.example.dantalk.features.main.search.store.SearchStore.Intent
import com.example.dantalk.features.main.search.store.SearchStore.Label
import com.example.dantalk.features.main.search.store.SearchStore.State
import com.example.domain.model.userdata.UserData

interface SearchStore : Store<Intent, State, Label> {

    sealed interface Intent {
        class OnQueryChange(val query: String) : Intent
        data object NavigateBack : Intent
    }

    data class State(
        val query: String = "",
        val usersByQuery: List<UserData> = emptyList(),
        val isLoading: Boolean = false
    )

    sealed interface Label {
        data object NavigateBack : Label
    }
}