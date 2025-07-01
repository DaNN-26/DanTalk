package com.example.dantalk.features.main.people.store

import com.arkivanov.mvikotlin.core.store.Store
import com.example.dantalk.features.main.people.store.PeopleStore.*
import com.example.domain.model.userdata.UserData

interface PeopleStore : Store<Intent, State, Label> {

    sealed interface Intent {
        class OnQueryChange(val query: String) : Intent
        data object NavigateBack : Intent
    }

    data class State(
        val query: String = "",
        val usersByQuery: List<UserData> = emptyList(),
        val isLoading: Boolean = false,
    )

    sealed interface Label {
        data object NavigateBack : Label
    }
}