package com.example.feature.main.chat.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapperScope
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutorScope
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.example.core.ui.model.UiChat
import com.example.core.util.toDateString
import com.example.data.chat.api.ChatRepository
import com.example.data.chat.api.model.Message
import com.example.data.user.api.UserDataStoreRepository
import com.example.data.user.api.model.UserData
import com.example.feature.mapper.toUi
import com.example.feature.main.chat.model.MessageListItem
import com.example.feature.main.chat.store.ChatStore.Intent
import com.example.feature.main.chat.store.ChatStore.Label
import com.example.feature.main.chat.store.ChatStore.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatStoreFactory(
    private val factory: StoreFactory,
    private val chatRepository: ChatRepository,
    private val userDataFlow: Flow<UserData>,
    private val chatId: String,
) {
    private sealed interface Action {
        class GetChat(val chat: UiChat) : Action
        class GetMessages(val messages: List<MessageListItem>) : Action
        class SetUser(val user: UserData) : Action
    }

    private sealed interface Msg {
        class GetChat(val chat: UiChat) : Msg
        class GetMessages(val messages: List<MessageListItem>) : Msg
        class OnMessageChange(val message: String) : Msg
        data object ClearMessage : Msg
        class SetUser(val user: UserData) : Msg
    }

    fun create(): ChatStore =
        object : ChatStore,
            Store<Intent, State, Label> by factory.create<Intent, Action, Msg, State, Label>(
                name = "ChatStore",
                initialState = State(),
                bootstrapper = coroutineBootstrapper {
                    launch {
                        userDataFlow.collect { user ->
                            dispatch(Action.SetUser(user))
                            getChat(chatId, user.id)
                        }
                    }
                },
                executorFactory = coroutineExecutorFactory {
                    onAction<Action.GetChat> { dispatch(Msg.GetChat(it.chat)) }
                    onAction<Action.GetMessages> { dispatch(Msg.GetMessages(it.messages)) }
                    onAction<Action.SetUser> { dispatch(Msg.SetUser(it.user)) }
                    onIntent<Intent.OnMessageChange> { dispatch(Msg.OnMessageChange(it.message)) }
                    onIntent<Intent.SendMessage> { sendMessage() }
                    onIntent<Intent.ReadMessage> { readMessage(it.ids) }
                    onIntent<Intent.NavigateBack> { publish(Label.NavigateBack) }
                },
                reducer = { msg ->
                    when (msg) {
                        is Msg.GetChat -> copy(chat = msg.chat)
                        is Msg.GetMessages -> copy(messages = msg.messages)
                        is Msg.OnMessageChange -> copy(currentMessage = msg.message)
                        is Msg.ClearMessage -> copy(currentMessage = "")
                        is Msg.SetUser -> copy(currentUser = msg.user)
                    }
                }
            ) {}

    private suspend fun CoroutineBootstrapperScope<Action>.getChat(id: String, userId: String) =
        withContext(Dispatchers.IO) {
            chatRepository.getChatMessages(id).collect { messages ->
                val chat = chatRepository.getChat(id)
                withContext(Dispatchers.Main) {
                    dispatch(Action.GetChat(chat.toUi(userId)))
                    dispatch(Action.GetMessages(messages.toMessageListItems(userId)))
                }
            }
        }

    private fun CoroutineExecutorScope<State, Msg, Nothing, Nothing>.sendMessage() {
        if (state().currentMessage.isBlank()) return
        launch {
            val message = Message(
                sender = state().currentUser.id,
                message = state().currentMessage.trim()
            )
            dispatch(Msg.ClearMessage)
            withContext(Dispatchers.IO) {
                chatRepository.sendMessage(chatId, message)
            }
        }
    }

    private fun CoroutineExecutorScope<State, Msg, Nothing, Nothing>.readMessage(ids: List<String>) {
        if (state().chat == null) return
        launch(Dispatchers.IO) {
            chatRepository.readMessage(chatId, ids)
        }
    }

    private fun List<Message>.toMessageListItems(currentUserId: String) =
        groupBy { it.sentAt.toDateString() }
            .flatMap { (date, messages) ->
                messages.map {
                    MessageListItem.MessageItem(
                        it.toUi(currentUserId)
                    )
                } + listOf(MessageListItem.DateItem(date))
            }
}