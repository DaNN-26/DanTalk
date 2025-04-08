package com.example.dantalk.components.auth.signUp

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.example.dantalk.components.auth.signUp.inputEmail.InputEmailComponent
import com.example.dantalk.components.auth.signUp.inputName.InputNameComponent
import com.example.dantalk.components.auth.signUp.inputPassword.InputPasswordComponent

interface SignUpComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        class InputEmail(val component: InputEmailComponent) : Child
        class InputName(val component: InputNameComponent) : Child
        class InputPassword(val component: InputPasswordComponent) : Child
    }
}