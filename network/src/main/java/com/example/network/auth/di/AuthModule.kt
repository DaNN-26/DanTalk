package com.example.network.auth.di

import com.example.network.auth.data.repository.AuthRepositoryImpl
import com.example.network.auth.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import org.koin.dsl.module

val AuthModule = module {
    single<FirebaseAuth> { FirebaseAuth.getInstance() }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
}