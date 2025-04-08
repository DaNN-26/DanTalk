package com.example.dantalk.components.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.example.dantalk.components.main.home.DefaultHomeComponent
import com.example.dantalk.components.main.profile.DefaultProfileComponent
import com.example.dantalk.components.main.search.DefaultSearchComponent
import kotlinx.serialization.Serializable

class DefaultMainComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext, MainComponent {

    private val navigation = StackNavigation<Config>()

    override val stack = childStack(
        source = navigation,
        initialConfiguration = Config.Home,
        serializer = Config.serializer(),
        handleBackButton = true,
        childFactory = ::child
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ) : MainComponent.Child =
        when(config) {
            is Config.Home -> MainComponent.Child.Home(homeComponent(componentContext))
            is Config.Search -> MainComponent.Child.Search(searchComponent(componentContext))
            is Config.Profile -> MainComponent.Child.Profile(profileComponent(componentContext))
        }

    @OptIn(DelicateDecomposeApi::class)
    private fun homeComponent(componentContext: ComponentContext) =
        DefaultHomeComponent(
            componentContext = componentContext,
            navigateToSearch = { navigation.push(Config.Search) },
            navigateToProfile = { navigation.push(Config.Profile) }
        )

    private fun searchComponent(componentContext: ComponentContext) =
        DefaultSearchComponent(
            componentContext = componentContext,
            navigateBack = { navigation.pop() }
        )

    private fun profileComponent(componentContext: ComponentContext) =
        DefaultProfileComponent(
            componentContext = componentContext,
            navigateBack = { navigation.pop() }
        )

    @Serializable
    sealed interface Config {
        @Serializable
        data object Home : Config
        @Serializable
        data object Search : Config
        @Serializable
        data object Profile : Config
    }
}