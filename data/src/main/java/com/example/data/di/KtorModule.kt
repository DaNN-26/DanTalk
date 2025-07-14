package com.example.data.di

import com.example.data.storage.api.StorageRepository
import com.example.data.storage.impl.StorageRepositoryImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val KtorModule = module {
    single {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }
    single<StorageRepository> {
        StorageRepositoryImpl(
            client = get(),
            context = androidContext())
    }
}