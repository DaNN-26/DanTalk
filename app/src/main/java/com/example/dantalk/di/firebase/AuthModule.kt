package com.example.dantalk.di.firebase

import com.example.core.firebase.auth.data.repository.AuthRepositoryImpl
import com.example.core.firebase.auth.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import org.koin.dsl.module

val AuthModule = module {
    single<FirebaseAuth> { FirebaseAuth.getInstance() }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
}