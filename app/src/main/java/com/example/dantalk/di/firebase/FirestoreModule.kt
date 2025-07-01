package com.example.dantalk.di.firebase

import com.example.core.firebase.firestore.chat.data.repository.ChatRepositoryImpl
import com.example.core.firebase.firestore.chat.domain.repository.ChatRepository
import com.example.core.firebase.firestore.user.data.repository.UserRepositoryImpl
import com.example.core.firebase.firestore.user.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module

val FirestoreModule = module {
    single<FirebaseFirestore> { FirebaseFirestore.getInstance() }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<ChatRepository> { ChatRepositoryImpl(get()) }
}