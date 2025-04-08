package com.example.dantalk.components.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.example.dantalk.components.auth.DefaultAuthComponent
import com.example.dantalk.components.main.DefaultMainComponent
import com.example.network.auth.domain.repository.AuthRepository
import com.example.network.auth.domain.repository.UserRepository
import kotlinx.serialization.Serializable

class DefaultRootComponent(
    componentContext: ComponentContext,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ComponentContext by componentContext, RootComponent {

    private val navigation = StackNavigation<Config>()

    override val stack = childStack(
        source = navigation,
            initialConfiguration = Config.Auth,
        serializer = Config.serializer(),
        handleBackButton = false,
        childFactory = ::child
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ) : RootComponent.Child =
        when(config) {
            is Config.Auth -> RootComponent.Child.Auth(authComponent(componentContext))
            is Config.Main -> RootComponent.Child.Main(mainComponent(componentContext))
        }

    @OptIn(DelicateDecomposeApi::class)
    private fun authComponent(componentContext: ComponentContext) =
        DefaultAuthComponent(
            componentContext = componentContext,
            authRepository = authRepository,
            userRepository = userRepository,
            navigateToHome = { navigation.push(Config.Main) }
        )

    private fun mainComponent(componentContext: ComponentContext) =
        DefaultMainComponent(componentContext)

    @Serializable
    sealed interface Config {
        @Serializable
        data object Auth : Config
        @Serializable
        data object Main : Config
    }
}