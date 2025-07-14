package com.example.feature.root.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.example.data.auth.api.AuthRepository
import com.example.data.chat.api.ChatRepository
import com.example.data.storage.api.StorageRepository
import com.example.data.user.api.UserDataStoreRepository
import com.example.data.user.api.UserRepository
import com.example.feature.auth.component.DefaultAuthComponent
import com.example.feature.main.component.DefaultMainComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
    private val userDataStoreRepository: UserDataStoreRepository,
    private val storageRepository: StorageRepository,
) : ComponentContext by componentContext, RootComponent {

    private val navigation = StackNavigation<Config>()

    private suspend fun getInitialConfiguration(): Config {
        val user = userDataStoreRepository.getUserData.first()
        return if (user.id.isNotEmpty()) Config.Main else Config.Auth
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
            userDataStoreRepository = userDataStoreRepository,
            navigateToHome = { navigation.replaceAll(Config.Main) }
        )

    private fun mainComponent(componentContext: ComponentContext) =
        DefaultMainComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            authRepository = authRepository,
            userRepository = userRepository,
            chatRepository = chatRepository,
            userDataStoreRepository = userDataStoreRepository,
            storageRepository = storageRepository,
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