package com.example.feature.main.home.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.example.data.auth.api.AuthRepository
import com.example.data.chat.api.ChatRepository
import com.example.data.chat.api.model.Chat
import com.example.data.user.api.UserDataStoreRepository
import com.example.data.user.api.UserRepository
import com.example.data.user.api.model.UserData
import com.example.feature.main.home.store.HomeStore.Intent
import com.example.feature.main.home.store.HomeStore.Label
import com.example.feature.main.home.store.HomeStore.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeStoreFactory(
    private val factory: StoreFactory,
    private val authRepository: AuthRepository,
    private val chatRepository: ChatRepository,
    private val userDataStoreRepository: UserDataStoreRepository,
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
                        userDataStoreRepository.getUserData.collect { user ->
                            withContext(Dispatchers.Main) { dispatch(Action.SetUser(user)) }
                            val chats = chatRepository.getChats(user)
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
        userDataStoreRepository.clearUserData()
    }
}