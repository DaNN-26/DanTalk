package com.example.dantalk.ui.view.main

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.example.dantalk.components.main.MainComponent
import com.example.dantalk.ui.view.main.home.Home
import com.example.dantalk.ui.view.main.profile.Profile
import com.example.dantalk.ui.view.main.search.Search

@Composable
fun Main(
    component: MainComponent
) {
    val stack = component.stack

    Children(
        stack = stack,
        animation = stackAnimation(fade() + scale())
    ) { child ->
        when(val instance = child.instance) {
            is MainComponent.Child.Home -> Home(instance.component)
            is MainComponent.Child.Search -> Search(instance.component)
            is MainComponent.Child.Profile -> Profile(instance.component)
        }
    }
}