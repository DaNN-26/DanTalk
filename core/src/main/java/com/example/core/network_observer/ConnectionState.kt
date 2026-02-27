package com.example.core.network_observer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext

sealed class ConnectionState {
    object Available : ConnectionState()
    object Unavailable : ConnectionState()
}

@Composable
fun connectionState() : State<ConnectionState> {
    val context = LocalContext.current
    return produceState<ConnectionState>(ConnectionState.Unavailable) {
        context.observeConnectivityAsFlow().collect {
            value = if (it) ConnectionState.Available else ConnectionState.Unavailable
        }
    }
}