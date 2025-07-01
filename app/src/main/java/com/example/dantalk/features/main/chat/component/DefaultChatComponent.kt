package com.example.dantalk.features.main.chat.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.dantalk.features.main.chat.store.ChatStore
import com.example.dantalk.features.main.chat.store.ChatStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class DefaultChatComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory
) : ChatComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        ChatStoreFactory(
            factory = storeFactory
        ).create()
    }

    private val scope = CoroutineScope(Dispatchers.Main)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state = store.stateFlow

    override fun onIntent(intent: ChatStore.Intent) {
        store.accept(intent)
    }
}