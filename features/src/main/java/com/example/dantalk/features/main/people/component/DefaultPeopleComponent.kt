package com.example.dantalk.features.main.people.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.core.datastore.domain.UserDataStore
import com.example.core.firebase.firestore.user.domain.repository.UserRepository
import com.example.dantalk.features.main.people.store.PeopleStore
import com.example.dantalk.features.main.people.store.PeopleStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

class DefaultPeopleComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val userRepository: UserRepository,
    private val userDataStore: UserDataStore,
    private val navigateBack: () -> Unit,
) : PeopleComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        PeopleStoreFactory(
            factory = storeFactory,
            userRepository = userRepository,
            userDataStore = userDataStore
        ).create()
    }

    private val scope = CoroutineScope(Dispatchers.Main)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state = store.stateFlow

    override fun onIntent(intent: PeopleStore.Intent) {
        store.accept(intent)
    }

    init {
        scope.launch {
            store.labels.collect { label ->
                when (label) {
                    is PeopleStore.Label.NavigateBack -> navigateBack()
                }
            }
        }
    }
}