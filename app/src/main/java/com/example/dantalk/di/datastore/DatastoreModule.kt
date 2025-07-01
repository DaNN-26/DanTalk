package com.example.dantalk.di.datastore

import com.example.datastore.data.UserDataStoreImpl
import com.example.core.datastore.domain.UserDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val DatastoreModule = module {
    single<UserDataStore> { UserDataStoreImpl(androidContext()) }
}