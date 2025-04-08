package com.example.dantalk

import android.app.Application
import com.example.network.auth.di.AuthModule
import com.example.network.auth.di.FirestoreModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class DanTalkApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(listOf(AuthModule, FirestoreModule))
            androidLogger()
            androidContext(this@DanTalkApplication)
        }
    }
}