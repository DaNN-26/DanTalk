package com.example.dantalk.components.auth

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.example.dantalk.components.auth.signIn.DefaultSignInComponent
import com.example.dantalk.components.auth.signUp.DefaultSignUpComponent
import com.example.network.auth.domain.repository.AuthRepository
import com.example.network.auth.domain.repository.UserRepository
import kotlinx.serialization.Serializable

class DefaultAuthComponent(
    componentContext: ComponentContext,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val navigateToHome: () -> Unit
) : ComponentContext by componentContext, AuthComponent {

    private val navigation = StackNavigation<Config>()

    override val stack = childStack(
        source = navigation,
        initialConfiguration = Config.SignIn,
        serializer = Config.serializer(),
        handleBackButton = true,
        childFactory = ::child
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ) : AuthComponent.Child =
        when(config) {
            is Config.SignIn -> AuthComponent.Child.SignIn(signInComponent(componentContext))
            is Config.SignUp -> AuthComponent.Child.SignUp(signUpComponent(componentContext))
        }

    @OptIn(DelicateDecomposeApi::class)
    private fun signInComponent(componentContext: ComponentContext) =
        DefaultSignInComponent(
            componentContext = componentContext,
            authRepository = authRepository,
            navigateToSignUp = { navigation.push(Config.SignUp) },
            navigateToHome = navigateToHome
        )

    private fun signUpComponent(componentContext: ComponentContext) =
        DefaultSignUpComponent(
            componentContext = componentContext,
            authRepository = authRepository,
            userRepository = userRepository,
            navigateToHome = navigateToHome,
            navigateToSignIn = { navigation.pop() }
        )

    @Serializable
    sealed interface Config {
        @Serializable
        data object SignIn : Config
        @Serializable
        data object SignUp : Config
    }
}