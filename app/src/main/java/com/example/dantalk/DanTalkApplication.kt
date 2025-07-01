package com.example.dantalk

import android.app.Application
import com.example.dantalk.di.firebase.AuthModule
import com.example.dantalk.di.firebase.FirestoreModule
import com.example.dantalk.di.datastore.DatastoreModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class DanTalkApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(listOf(AuthModule, FirestoreModule, DatastoreModule))
            androidLogger()
            androidContext(this@DanTalkApplication)
        }
    }
}