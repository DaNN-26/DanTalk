package com.example.dantalk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import com.example.dantalk.components.root.DefaultRootComponent
import com.example.dantalk.ui.theme.DanTalkTheme
import com.example.dantalk.ui.view.Root
import com.example.network.auth.domain.repository.AuthRepository
import com.example.network.auth.domain.repository.UserRepository
import org.koin.android.ext.android.getKoin
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.androidx.compose.get
import org.koin.compose.getKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rootComponent = DefaultRootComponent(
            componentContext = defaultComponentContext(),
            authRepository = getKoin().get<AuthRepository>(),
            userRepository = getKoin().get<UserRepository>()
        )
        enableEdgeToEdge()
        setContent {
            DanTalkTheme {
                KoinAndroidContext {
                    Root(rootComponent)
                }
            }
        }
    }
}