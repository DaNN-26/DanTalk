package com.example.dantalk.components.auth.signIn

import com.arkivanov.decompose.value.Value
import com.example.mvi.auth.signIn.SignInIntent
import com.example.mvi.auth.signIn.SignInState

interface SignInComponent {
   val state: Value<SignInState>

   fun processIntent(intent: SignInIntent)
}