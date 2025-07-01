package com.example.dantalk.features.main.home.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.core.datastore.domain.UserDataStore
import com.example.core.firebase.auth.domain.repository.AuthRepository
import com.example.core.firebase.firestore.chat.domain.repository.ChatRepository
import com.example.core.firebase.firestore.user.domain.repository.UserRepository
import com.example.dantalk.features.main.home.store.HomeStore
import com.example.dantalk.features.main.home.store.HomeStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

class DefaultHomeComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository,
    private val userDataStore: UserDataStore,
    private val navigateToSearch: () -> Unit,
    private val navigateToProfile: () -> Unit,
    private val navigateToPeople: () -> Unit,
    private val navigateToAuth: () -> Unit,
) : ComponentContext by componentContext, HomeComponent {

    private val store = instanceKeeper.getStore {
        HomeStoreFactory(
            factory = storeFactory,
            authRepository = authRepository,
            userRepository = userRepository,
            chatRepository = chatRepository,
            userDataStore = userDataStore
        ).create()
    }

    private val scope = CoroutineScope(Dispatchers.Main)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state = store.stateFlow

    override fun onIntent(intent: HomeStore.Intent) {
        store.accept(intent)
    }

    init {
        scope.launch {
            store.labels.collect { label ->
                when (label) {
                    is HomeStore.Label.NavigateToSearch -> navigateToSearch()
                    is HomeStore.Label.NavigateToProfile -> navigateToProfile()
                    is HomeStore.Label.NavigateToPeople -> navigateToPeople()
                    is HomeStore.Label.NavigateToAuth -> navigateToAuth()
                }
            }
        }
    }
}