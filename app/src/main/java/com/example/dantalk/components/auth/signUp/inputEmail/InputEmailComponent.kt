package com.example.dantalk.components.auth.signUp.inputEmail

import com.arkivanov.decompose.value.Value
import com.example.mvi.auth.signUp.inputEmail.InputEmailIntent
import com.example.mvi.auth.signUp.inputEmail.InputEmailState

interface InputEmailComponent {
    val state: Value<InputEmailState>

    fun processIntent(intent: InputEmailIntent)
}