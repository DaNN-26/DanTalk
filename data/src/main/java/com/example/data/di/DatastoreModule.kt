package com.example.data.di

import com.example.data.user.api.UserDataStoreRepository
import com.example.data.user.impl.UserDataStoreRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val DatastoreModule = module {
    single<UserDataStoreRepository> { UserDataStoreRepositoryImpl(androidContext()) }
}