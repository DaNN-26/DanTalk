package com.example.dantalk.ui.view.auth.signUp

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.example.dantalk.components.auth.signUp.SignUpComponent
import com.example.dantalk.ui.view.auth.signUp.inputEmail.InputEmail
import com.example.dantalk.ui.view.auth.signUp.inputName.InputName
import com.example.dantalk.ui.view.auth.signUp.inputPassword.InputPassword

@Composable
fun SignUp(
    component: SignUpComponent
) {
    val stack = component.stack

    Children(
        stack = stack,
        animation = stackAnimation(slide())
    ) { child ->
        when(val instance = child.instance) {
            is SignUpComponent.Child.InputEmail -> InputEmail(instance.component)
            is SignUpComponent.Child.InputName -> InputName(instance.component)
            is SignUpComponent.Child.InputPassword -> InputPassword(instance.component)
        }
    }
}