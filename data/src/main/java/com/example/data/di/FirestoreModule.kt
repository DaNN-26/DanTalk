package com.example.data.di

import com.example.data.chat.api.ChatRepository
import com.example.data.chat.impl.ChatRepositoryImpl
import com.example.data.user.api.UserRepository
import com.example.data.user.impl.UserRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module

val FirestoreModule = module {
    single<FirebaseFirestore> { FirebaseFirestore.getInstance() }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<ChatRepository> { ChatRepositoryImpl(get()) }
}