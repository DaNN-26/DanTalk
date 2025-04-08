package com.example.dantalk.components.auth.signUp.inputName

import com.arkivanov.decompose.value.Value
import com.example.mvi.auth.signUp.inputName.InputNameIntent
import com.example.mvi.auth.signUp.inputName.InputNameState

interface InputNameComponent {
    val state: Value<InputNameState>

    fun processIntent(intent: InputNameIntent)
}