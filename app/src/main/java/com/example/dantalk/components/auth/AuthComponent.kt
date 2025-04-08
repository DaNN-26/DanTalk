package com.example.dantalk.components.auth

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.example.dantalk.components.auth.signUp.inputName.InputNameComponent
import com.example.dantalk.components.auth.signUp.inputPassword.InputPasswordComponent
import com.example.dantalk.components.auth.signIn.SignInComponent
import com.example.dantalk.components.auth.signUp.SignUpComponent
import com.example.dantalk.components.auth.signUp.inputEmail.InputEmailComponent

interface AuthComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        class SignIn(val component: SignInComponent) : Child
        class SignUp(val component: SignUpComponent) : Child
    }
}