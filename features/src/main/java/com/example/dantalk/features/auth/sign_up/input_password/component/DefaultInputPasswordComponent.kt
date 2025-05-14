package com.example.dantalk.features.auth.sign_up.input_password.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.core.datastore.domain.UserDataStore
import com.example.core.firebase.auth.domain.repository.AuthRepository
import com.example.core.firebase.firestore.user.domain.repository.UserRepository
import com.example.dantalk.features.auth.sign_up.input_password.store.InputPasswordStore
import com.example.dantalk.features.auth.sign_up.input_password.store.InputPasswordStoreFactory
import com.example.domain.userdata.model.UserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultInputPasswordComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val userDataStore: UserDataStore,
    private val currentUserData: UserData,
    private val navigateToHome: () -> Unit,
    private val navigateBack: () -> Unit
) : ComponentContext by componentContext, InputPasswordComponent {

    private val store = instanceKeeper.getStore {
        InputPasswordStoreFactory(
            factory = storeFactory,
            authRepository = authRepository,
            userRepository = userRepository,
            userDataStore = userDataStore,
            currentUserData = currentUserData
        ).create()
    }

    private val scope = CoroutineScope(Dispatchers.Main)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state = store.stateFlow

    override fun onIntent(intent: InputPasswordStore.Intent) {
        store.accept(intent)
    }

    init {
        scope.launch {
            store.labels.collect { label ->
                when (label) {
                    is InputPasswordStore.Label.NavigateToHome -> navigateToHome()
                    is InputPasswordStore.Label.NavigateBack -> navigateBack()
                }
            }
        }
    }
}