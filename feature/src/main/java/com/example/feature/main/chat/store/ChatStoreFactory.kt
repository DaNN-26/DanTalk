package com.example.feature.main.chat.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.example.feature.main.chat.store.ChatStore.*

class ChatStoreFactory(
    private val factory: StoreFactory
) {
    fun create(): ChatStore =
        object : ChatStore,
            Store<Intent, State, Nothing> by factory.create<Intent, Nothing, Nothing, State, Nothing>(
                name = "ChatStore",
                initialState = State(),
                executorFactory = coroutineExecutorFactory {  },
            ) {}
}