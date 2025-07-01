package com.example.feature.main.home.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.data.auth.api.AuthRepository
import com.example.data.chat.api.ChatRepository
import com.example.data.user.api.UserDataStoreRepository
import com.example.data.user.api.UserRepository
import com.example.feature.main.home.store.HomeStore
import com.example.feature.main.home.store.HomeStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

class DefaultHomeComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val authRepository: AuthRepository,
    private val chatRepository: ChatRepository,
    private val userDataStoreRepository: UserDataStoreRepository,
    private val navigateToSearch: () -> Unit,
    private val navigateToProfile: () -> Unit,
    private val navigateToPeople: () -> Unit,
    private val navigateToAuth: () -> Unit,
) : ComponentContext by componentContext, HomeComponent {

    private val store = instanceKeeper.getStore {
        HomeStoreFactory(
            factory = storeFactory,
            authRepository = authRepository,
            chatRepository = chatRepository,
            userDataStoreRepository = userDataStoreRepository
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