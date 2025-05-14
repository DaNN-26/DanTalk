package com.example.dantalk.features.main.home.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.example.core.datastore.domain.UserDataStore
import com.example.core.firebase.auth.domain.repository.AuthRepository
import com.example.core.firebase.firestore.chat.domain.repository.ChatRepository
import com.example.core.firebase.firestore.user.domain.repository.UserRepository
import com.example.dantalk.features.main.home.store.HomeStore.Intent
import com.example.dantalk.features.main.home.store.HomeStore.Label
import com.example.dantalk.features.main.home.store.HomeStore.State
import com.example.dantalk.features.main.profile.store.ProfileStoreFactory.Action
import com.example.domain.chat.model.Chat
import com.example.domain.userdata.model.UserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeStoreFactory(
    private val factory: StoreFactory,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository,
    private val userDataStore: UserDataStore,
) {
    private sealed interface Action {
        class GetChats(val chats: List<Chat>) : Action
        class SetUser(val user: UserData) : Action
        class UpdateLoading(val isLoading: Boolean) : Action
    }

    private sealed interface Msg {
        class GetChats(val chats: List<Chat>) : Msg
        class SetUser(val user: UserData) : Msg
        class UpdateLoading(val isLoading: Boolean) : Msg
    }

    fun create(): HomeStore =
        object : HomeStore,
            Store<Intent, State, Label> by factory.create<Intent, Action, Msg, State, Label>(
                name = "HomeStore",
                initialState = State(),
                bootstrapper = coroutineBootstrapper {
                    dispatch(Action.UpdateLoading(true))
                    launch(Dispatchers.IO) {
                        userDataStore.getUserData.collect { user ->
                            withContext(Dispatchers.Main) { dispatch(Action.SetUser(user)) }
                            val userRef = userRepository.getUserReference(user.userId)
                            val chats = chatRepository.getChats(userRef)
                            withContext(Dispatchers.Main) {
                                dispatch(Action.GetChats(chats))
                                dispatch(Action.UpdateLoading(false))
                            }
                        }
                    }
                },
                executorFactory = coroutineExecutorFactory {
                    onAction<Action.GetChats> { dispatch(Msg.GetChats(it.chats)) }
                    onAction<Action.SetUser> { dispatch(Msg.SetUser(it.user)) }
                    onAction<Action.UpdateLoading> { dispatch(Msg.UpdateLoading(it.isLoading)) }
                    onIntent<Intent.NavigateToSearch> { publish(Label.NavigateToSearch) }
                    onIntent<Intent.NavigateToProfile> { publish(Label.NavigateToProfile) }
                    onIntent<Intent.NavigateToPeople> { publish(Label.NavigateToPeople) }
                    onIntent<Intent.SignOut> {
                        launch {
                            signOut()
                            publish(Label.NavigateToAuth)
                        }
                    }
                },
                reducer = { msg ->
                    when (msg) {
                        is Msg.GetChats -> copy(chats = msg.chats)
                        is Msg.SetUser -> copy(user = msg.user)
                        is Msg.UpdateLoading -> copy(isLoading = msg.isLoading)
                    }
                }
            ) {}

    private suspend fun signOut() = withContext(Dispatchers.IO) {
        authRepository.signOut()
        userDataStore.clearUserData()
    }
}