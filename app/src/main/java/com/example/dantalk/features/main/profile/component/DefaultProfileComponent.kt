package com.example.dantalk.features.main.profile.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.core.datastore.domain.UserDataStore
import com.example.core.firebase.firestore.user.domain.repository.UserRepository
import com.example.dantalk.features.main.profile.store.ProfileStore
import com.example.dantalk.features.main.profile.store.ProfileStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

class DefaultProfileComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val userRepository: UserRepository,
    private val userDataStore: UserDataStore,
    private val navigateBack: () -> Unit
) : ComponentContext by componentContext, ProfileComponent {

    private val store = instanceKeeper.getStore {
        ProfileStoreFactory(
            factory = storeFactory,
            userRepository = userRepository,
            userDataStore = userDataStore
        ).create()
    }

    private val scope = CoroutineScope(Dispatchers.Main)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state = store.stateFlow

    override fun onIntent(intent: ProfileStore.Intent) {
        store.accept(intent)
    }

    init {
        scope.launch {
            store.labels.collect { label ->
                when (label) {
                    is ProfileStore.Label.NavigateBack -> navigateBack()
                }
            }
        }
    }
}