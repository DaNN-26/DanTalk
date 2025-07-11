package com.example.feature.main.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.example.data.auth.api.AuthRepository
import com.example.data.chat.api.ChatRepository
import com.example.data.user.api.UserDataStoreRepository
import com.example.data.user.api.UserRepository
import com.example.feature.main.chat.component.DefaultChatComponent
import com.example.feature.main.home.component.DefaultHomeComponent
import com.example.feature.main.people.component.DefaultPeopleComponent
import com.example.feature.main.profile.component.DefaultProfileComponent
import com.example.feature.main.search.component.DefaultSearchComponent
import kotlinx.serialization.Serializable

class DefaultMainComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository,
    private val userDataStoreRepository: UserDataStoreRepository,
    private val navigateToAuth: () -> Unit,
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
        componentContext: ComponentContext,
    ): MainComponent.Child =
        when (config) {
            is Config.Home -> MainComponent.Child.Home(homeComponent(componentContext))
            is Config.Search -> MainComponent.Child.Search(searchComponent(componentContext))
            is Config.Profile -> MainComponent.Child.Profile(profileComponent(componentContext))
            is Config.People -> MainComponent.Child.People(peopleComponent(componentContext))
            is Config.Chat -> MainComponent.Child.Chat(
                chatComponent(
                    componentContext = componentContext,
                    config = config
                )
            )
        }

    @OptIn(DelicateDecomposeApi::class)
    private fun homeComponent(componentContext: ComponentContext) =
        DefaultHomeComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            authRepository = authRepository,
            chatRepository = chatRepository,
            userDataStoreRepository = userDataStoreRepository,
            navigateToSearch = { navigation.push(Config.Search) },
            navigateToProfile = { navigation.push(Config.Profile) },
            navigateToPeople = { navigation.push(Config.People) },
            navigateToAuth = navigateToAuth,
            navigateToChat = { navigation.push(Config.Chat(it)) }
        )

    private fun searchComponent(componentContext: ComponentContext) =
        DefaultSearchComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            userRepository = userRepository,
            userDataStoreRepository = userDataStoreRepository,
            navigateBack = { navigation.pop() }
        )

    private fun profileComponent(componentContext: ComponentContext) =
        DefaultProfileComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            userRepository = userRepository,
            userDataStoreRepository = userDataStoreRepository,
            navigateBack = { navigation.pop() }
        )

    private fun peopleComponent(componentContext: ComponentContext) =
        DefaultPeopleComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            userRepository = userRepository,
            userDataStoreRepository = userDataStoreRepository,
            navigateBack = { navigation.pop() }
        )

    private fun chatComponent(
        componentContext: ComponentContext,
        config: Config.Chat,
    ) =
        DefaultChatComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            chatRepository = chatRepository,
            userDataStoreRepository = userDataStoreRepository,
            chatId = config.id,
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

        @Serializable
        data object People : Config

        @Serializable
        class Chat(val id: String) : Config
    }
}