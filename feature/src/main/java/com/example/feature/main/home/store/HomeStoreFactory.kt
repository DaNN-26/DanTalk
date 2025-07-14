package com.example.feature.main.home.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapperScope
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutorScope
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.example.core.ui.model.UiChat
import com.example.core.ui.model.UiUserData
import com.example.data.auth.api.AuthRepository
import com.example.data.chat.api.ChatRepository
import com.example.data.chat.api.model.Chat
import com.example.data.user.api.UserDataStoreRepository
import com.example.feature.main.home.store.HomeStore.Intent
import com.example.feature.main.home.store.HomeStore.Label
import com.example.feature.main.home.store.HomeStore.State
import com.example.feature.mapper.toUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeStoreFactory(
    private val factory: StoreFactory,
    private val authRepository: AuthRepository,
    private val chatRepository: ChatRepository,
    private val userDataStoreRepository: UserDataStoreRepository,
) {
    private sealed interface Action {
        class GetChats(val chats: List<UiChat>) : Action
        class SetUser(val user: UiUserData) : Action
        class UpdateLoading(val isLoading: Boolean) : Action
    }

    private sealed interface Msg {
        class GetChats(val chats: List<UiChat>) : Msg
        class SetUser(val user: UiUserData) : Msg
        class UpdateLoading(val isLoading: Boolean) : Msg
    }

    fun create(): HomeStore =
        object : HomeStore,
            Store<Intent, State, Label> by factory.create<Intent, Action, Msg, State, Label>(
                name = "HomeStore",
                initialState = State(),
                bootstrapper = coroutineBootstrapper { getData() },
                executorFactory = coroutineExecutorFactory {
                    onAction<Action.GetChats> { dispatch(Msg.GetChats(it.chats)) }
                    onAction<Action.SetUser> { dispatch(Msg.SetUser(it.user)) }
                    onAction<Action.UpdateLoading> { dispatch(Msg.UpdateLoading(it.isLoading)) }
                    onIntent<Intent.NavigateToSearch> { publish(Label.NavigateToSearch) }
                    onIntent<Intent.NavigateToProfile> { publish(Label.NavigateToProfile) }
                    onIntent<Intent.NavigateToPeople> { publish(Label.NavigateToPeople) }
                    onIntent<Intent.OpenChat> { publish(Label.NavigateToChat(it.id)) }
                    onIntent<Intent.SignOut> { signOut() }
                },
                reducer = { msg ->
                    when (msg) {
                        is Msg.GetChats -> copy(chats = msg.chats)
                        is Msg.SetUser -> copy(user = msg.user)
                        is Msg.UpdateLoading -> copy(isLoading = msg.isLoading)
                    }
                }
            ) {}

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun CoroutineBootstrapperScope<Action>.getData() {
        dispatch(Action.UpdateLoading(true))
        launch {
            userDataStoreRepository.getUserData
                .map { it.toUi() }
                .flatMapLatest { user ->
                    dispatch(Action.SetUser(user))
                    getChatsFlow(user.id)
                }
                .collect { chats ->
                    dispatch(Action.GetChats(chats))
                    dispatch(Action.UpdateLoading(false))
                }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun getChatsFlow(userId: String): Flow<List<UiChat>> =
        withContext(Dispatchers.IO) {
            chatRepository.getChats(userId).flatMapLatest { chats ->
                combine(
                    chats.map { chat ->
                        chatRepository.getChatMessages(chat.id)
                            .map { messages ->
                                Chat(
                                    id = chat.id,
                                    users = chat.users
                                ).toUi(userId, messages.toUi(userId))
                            }
                    }
                ) { it.toList() }
            }
        }


    private fun CoroutineExecutorScope<State, Nothing, Nothing, Label>.signOut() {
        launch {
            authRepository.signOut()
            userDataStoreRepository.clearUserData()
            publish(Label.NavigateToAuth)
        }
    }
}