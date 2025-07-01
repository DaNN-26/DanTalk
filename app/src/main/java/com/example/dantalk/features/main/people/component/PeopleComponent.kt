package com.example.dantalk.features.main.people.component

import com.example.dantalk.features.main.people.store.PeopleStore
import kotlinx.coroutines.flow.StateFlow

interface PeopleComponent {
    val state: StateFlow<PeopleStore.State>

    fun onIntent(intent: PeopleStore.Intent)
}