package com.example.dantalk.components.auth.signUp

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.example.dantalk.components.auth.signUp.inputEmail.DefaultInputEmailComponent
import com.example.dantalk.components.auth.signUp.inputName.DefaultInputNameComponent
import com.example.dantalk.components.auth.signUp.inputPassword.DefaultInputPasswordComponent
import com.example.network.auth.domain.model.User
import com.example.network.auth.domain.repository.AuthRepository
import com.example.network.auth.domain.repository.UserRepository
import kotlinx.serialization.Serializable

class DefaultSignUpComponent(
    componentContext: ComponentContext,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val navigateToHome: () -> Unit,
    private val navigateToSignIn: () -> Unit
) : ComponentContext by componentContext, SignUpComponent {

    private val navigation = StackNavigation<Config>()

    override val stack = childStack(
        source = navigation,
        initialConfiguration = Config.InputEmail,
        serializer = Config.serializer(),
        handleBackButton = true,
        childFactory = ::child
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext,
    ) : SignUpComponent.Child =
        when(config) {
            is Config.InputEmail -> SignUpComponent.Child.InputEmail(inputEmailComponent(componentContext))
            is Config.InputName -> SignUpComponent.Child.InputName(inputNameComponent(
                config = Config.InputName(config.user),
                componentContext = componentContext
            ))
            is Config.InputPassword -> SignUpComponent.Child.InputPassword(inputPasswordComponent(
                config = Config.InputPassword(config.user),
                componentContext = componentContext
            ))
        }

    @OptIn(DelicateDecomposeApi::class)
    private fun inputEmailComponent(componentContext: ComponentContext) =
        DefaultInputEmailComponent(
            componentContext = componentContext,
            userRepository = userRepository,
            navigateToInputName = { user -> navigation.push(Config.InputName(user)) },
            navigateBack = navigateToSignIn
        )

    @OptIn(DelicateDecomposeApi::class)
    private fun inputNameComponent(config: Config.InputName, componentContext: ComponentContext) =
        DefaultInputNameComponent(
            componentContext = componentContext,
            navigateToInputPassword = { user -> navigation.push(Config.InputPassword(user)) },
            navigateBack = { navigation.pop() },
            currentUserData = config.user
        )

    private fun inputPasswordComponent(config: Config.InputPassword, componentContext: ComponentContext) =
        DefaultInputPasswordComponent(
            componentContext = componentContext,
            authRepository = authRepository,
            userRepository = userRepository,
            navigateToHome = navigateToHome,
            navigateBack = { navigation.pop() },
            currentUserData = config.user
        )

    @Serializable
    sealed interface Config {
        @Serializable
        data object InputEmail : Config
        @Serializable
        class InputName(val user: User) : Config
        @Serializable
        class InputPassword(val user: User) : Config
    }
}