package com.example.dantalk.features.root.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.example.core.datastore.domain.UserDataStore
import com.example.core.firebase.auth.domain.repository.AuthRepository
import com.example.core.firebase.firestore.chat.domain.repository.ChatRepository
import com.example.core.firebase.firestore.user.domain.repository.UserRepository
import com.example.dantalk.features.auth.component.DefaultAuthComponent
import com.example.dantalk.features.main.component.DefaultMainComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

class DefaultRootComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository,
    private val userDataStore: UserDataStore,
) : ComponentContext by componentContext, RootComponent {

    private val navigation = StackNavigation<Config>()

    private suspend fun getInitialConfiguration(): Config {
        val user = userDataStore.getUserData.first()
        return if (user.userId.isNotEmpty()) Config.Main else Config.Auth
    }

    override val stack = childStack(
        source = navigation,
        initialConfiguration = runBlocking(Dispatchers.IO) { getInitialConfiguration() },
        serializer = Config.serializer(),
        handleBackButton = false,
        childFactory = ::child
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext,
    ): RootComponent.Child =
        when (config) {
            is Config.Auth -> RootComponent.Child.Auth(authComponent(componentContext))
            is Config.Main -> RootComponent.Child.Main(mainComponent(componentContext))
        }

    private fun authComponent(componentContext: ComponentContext) =
        DefaultAuthComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            authRepository = authRepository,
            userRepository = userRepository,
            userDataStore = userDataStore,
            navigateToHome = { navigation.replaceAll(Config.Main) }
        )

    private fun mainComponent(componentContext: ComponentContext) =
        DefaultMainComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            authRepository = authRepository,
            userRepository = userRepository,
            chatRepository = chatRepository,
            userDataStore = userDataStore,
            navigateToAuth = { navigation.replaceAll(Config.Auth) }
        )

    @Serializable
    sealed interface Config {
        @Serializable
        data object Auth : Config

        @Serializable
        data object Main : Config
    }
}