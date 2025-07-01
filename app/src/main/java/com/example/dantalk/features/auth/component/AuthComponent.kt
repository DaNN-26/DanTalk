package com.example.dantalk.features.auth.component

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.example.dantalk.features.auth.sign_in.component.SignInComponent
import com.example.dantalk.features.auth.sign_up.component.SignUpComponent

interface AuthComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        class SignIn(val component: SignInComponent) : Child
        class SignUp(val component: SignUpComponent) : Child
    }
}