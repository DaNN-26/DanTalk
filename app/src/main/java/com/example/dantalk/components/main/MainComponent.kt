package com.example.dantalk.components.main

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.example.dantalk.components.main.home.HomeComponent
import com.example.dantalk.components.main.profile.ProfileComponent
import com.example.dantalk.components.main.search.SearchComponent

interface MainComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        class Home(val component: HomeComponent) : Child
        class Search(val component: SearchComponent) : Child
        class Profile(val component: ProfileComponent) : Child
    }
}