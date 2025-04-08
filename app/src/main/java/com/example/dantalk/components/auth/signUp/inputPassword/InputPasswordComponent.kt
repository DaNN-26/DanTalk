package com.example.dantalk.components.auth.signUp.inputPassword

import com.arkivanov.decompose.value.Value
import com.example.mvi.auth.signUp.inputPassword.InputPasswordIntent
import com.example.mvi.auth.signUp.inputPassword.InputPasswordState

interface InputPasswordComponent {
    val state: Value<InputPasswordState>

    fun processIntent(intent: InputPasswordIntent)
}