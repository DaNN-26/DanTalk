package com.example.core.firebase.di

import com.example.core.firebase.firestore.user.data.repository.UserRepositoryImpl
import com.example.core.firebase.firestore.user.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module

val FirestoreModule = module {
    single<FirebaseFirestore> { FirebaseFirestore.getInstance() }
    single<UserRepository> { UserRepositoryImpl(get()) }
}