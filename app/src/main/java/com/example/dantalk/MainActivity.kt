package com.example.dantalk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.example.core.design.theme.DanTalkTheme
import com.example.data.auth.api.AuthRepository
import com.example.data.chat.api.ChatRepository
import com.example.data.user.api.UserDataStoreRepository
import com.example.data.user.api.UserRepository
import com.example.feature.main.chat.ChatContent
import com.example.feature.main.chat.component.ChatComponent
import com.example.feature.main.chat.component.DefaultChatComponent
import com.example.feature.main.chat.store.ChatStoreFactory
import com.example.feature.root.RootContent
import com.example.feature.root.component.DefaultRootComponent
import org.koin.android.ext.android.getKoin
import org.koin.androidx.compose.KoinAndroidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rootComponent = DefaultRootComponent(
            componentContext = defaultComponentContext(),
            storeFactory = DefaultStoreFactory(),
            authRepository = getKoin().get<AuthRepository>(),
            userRepository = getKoin().get<UserRepository>(),
            chatRepository = getKoin().get<ChatRepository>(),
            userDataStoreRepository = getKoin().get<UserDataStoreRepository>()
        )
        enableEdgeToEdge()
        setContent {
            DanTalkTheme {
                KoinAndroidContext {
                    RootContent(rootComponent)
                }
            }
        }
    }
}