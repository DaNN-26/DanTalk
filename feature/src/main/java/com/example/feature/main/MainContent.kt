package com.example.feature.main

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.example.feature.main.chat.ChatContent
import com.example.feature.main.component.MainComponent
import com.example.feature.main.home.HomeContent
import com.example.feature.main.people.PeopleContent
import com.example.feature.main.profile.ProfileContent
import com.example.feature.main.search.SearchContent

@Composable
fun MainContent(
    component: MainComponent,
) {
    val stack = component.stack

    Children(
        stack = stack,
        animation = stackAnimation(fade() + scale())
    ) { child ->
        when (val instance = child.instance) {
            is MainComponent.Child.Home -> HomeContent(instance.component)
            is MainComponent.Child.Search -> SearchContent(instance.component)
            is MainComponent.Child.Profile -> ProfileContent(instance.component)
            is MainComponent.Child.People -> PeopleContent(instance.component)
            is MainComponent.Child.Chat -> ChatContent(instance.component)
        }
    }
}