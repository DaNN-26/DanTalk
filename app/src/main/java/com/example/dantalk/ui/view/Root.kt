package com.example.dantalk.ui.view

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.value.getValue
import com.example.dantalk.components.root.RootComponent
import com.example.dantalk.ui.view.auth.Auth
import com.example.dantalk.ui.view.main.Main

@Composable
fun Root(
    component: RootComponent
) {
    val stack = component.stack

    Children(
        stack = stack,
        animation = stackAnimation(fade() + scale())
    ) { child ->
        when(val instance = child.instance) {
            is RootComponent.Child.Auth -> Auth(instance.component)
            is RootComponent.Child.Main -> Main(instance.component)
        }
    }
}