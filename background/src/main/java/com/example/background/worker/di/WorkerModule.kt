package com.example.background.worker.di

import androidx.work.WorkManager
import com.example.background.worker.PendingSyncWorker
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val WorkerModule = module {
    single { WorkManager.getInstance(androidContext()) }
    worker { PendingSyncWorker(androidContext(), get(), get(), get()) }
}