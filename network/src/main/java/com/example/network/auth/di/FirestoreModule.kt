package com.example.network.auth.di

import com.example.network.auth.data.repository.UserRepositoryImpl
import com.example.network.auth.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module

val FirestoreModule = module {
    single<FirebaseFirestore> { FirebaseFirestore.getInstance() }
    single<UserRepository> { UserRepositoryImpl(get()) }
}