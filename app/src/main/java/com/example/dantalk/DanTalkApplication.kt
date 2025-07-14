package com.example.dantalk

import android.app.Application
import com.example.data.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class DanTalkApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(listOf(AuthModule, FirestoreModule, DatastoreModule, KtorModule))
            androidLogger()
            androidContext(this@DanTalkApplication)
        }
    }
}