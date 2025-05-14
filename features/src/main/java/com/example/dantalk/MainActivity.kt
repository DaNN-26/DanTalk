package com.example.dantalk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.example.core.datastore.domain.UserDataStore
import com.example.core.design.theme.DanTalkTheme
import com.example.core.firebase.auth.domain.repository.AuthRepository
import com.example.core.firebase.firestore.chat.domain.repository.ChatRepository
import com.example.core.firebase.firestore.user.domain.repository.UserRepository
import com.example.dantalk.features.root.RootContent
import com.example.dantalk.features.root.component.DefaultRootComponent
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
            userDataStore = getKoin().get<UserDataStore>()
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