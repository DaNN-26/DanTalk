package com.example.dantalk.features.main.component

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.example.dantalk.features.main.chat.component.ChatComponent
import com.example.dantalk.features.main.home.component.HomeComponent
import com.example.dantalk.features.main.people.component.PeopleComponent
import com.example.dantalk.features.main.profile.component.ProfileComponent
import com.example.dantalk.features.main.search.component.SearchComponent

interface MainComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        class Home(val component: HomeComponent) : Child
        class Search(val component: SearchComponent) : Child
        class Profile(val component: ProfileComponent) : Child
        class People(val component: PeopleComponent) : Child
        class Chat(val component: ChatComponent) : Child
    }
}