package com.example.dantalk.features.main.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.example.core.datastore.domain.UserDataStore
import com.example.core.firebase.auth.domain.repository.AuthRepository
import com.example.core.firebase.firestore.chat.domain.repository.ChatRepository
import com.example.core.firebase.firestore.user.domain.repository.UserRepository
import com.example.dantalk.features.main.chat.component.DefaultChatComponent
import com.example.dantalk.features.main.home.component.DefaultHomeComponent
import com.example.dantalk.features.main.people.component.DefaultPeopleComponent
import com.example.dantalk.features.main.profile.component.DefaultProfileComponent
import com.example.dantalk.features.main.search.component.DefaultSearchComponent
import kotlinx.serialization.Serializable

class DefaultMainComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository,
    private val userDataStore: UserDataStore,
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
            is Config.Chat -> MainComponent.Child.Chat(chatComponent(componentContext))
        }

    @OptIn(DelicateDecomposeApi::class)
    private fun homeComponent(componentContext: ComponentContext) =
        DefaultHomeComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            authRepository = authRepository,
            userRepository = userRepository,
            chatRepository = chatRepository,
            userDataStore = userDataStore,
            navigateToSearch = { navigation.push(Config.Search) },
            navigateToProfile = { navigation.push(Config.Profile) },
            navigateToPeople = { navigation.push(Config.People) },
            navigateToAuth = navigateToAuth
        )

    private fun searchComponent(componentContext: ComponentContext) =
        DefaultSearchComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            userRepository = userRepository,
            userDataStore = userDataStore,
            navigateBack = { navigation.pop() }
        )

    private fun profileComponent(componentContext: ComponentContext) =
        DefaultProfileComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            userRepository = userRepository,
            userDataStore = userDataStore,
            navigateBack = { navigation.pop() }
        )

    private fun peopleComponent(componentContext: ComponentContext) =
        DefaultPeopleComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            userRepository = userRepository,
            userDataStore = userDataStore,
            navigateBack = { navigation.pop() }
        )

    private fun chatComponent(componentContext: ComponentContext) =
        DefaultChatComponent(
            componentContext = componentContext,
            storeFactory = storeFactory
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
        data object Chat : Config
    }
}