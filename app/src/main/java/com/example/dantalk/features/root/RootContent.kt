package com.example.dantalk.features.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.example.dantalk.features.auth.AuthContent
import com.example.dantalk.features.main.MainContent
import com.example.dantalk.features.root.component.RootComponent

@Composable
fun RootContent(
    component: RootComponent
) {
    val stack = component.stack

    Children(
        stack = stack,
        animation = stackAnimation(fade() + scale())
    ) { child ->
        when(val instance = child.instance) {
            is RootComponent.Child.Auth -> AuthContent(instance.component)
            is RootComponent.Child.Main -> MainContent(instance.component)
        }
    }
}