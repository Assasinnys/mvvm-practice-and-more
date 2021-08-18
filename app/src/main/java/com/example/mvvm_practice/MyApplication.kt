package com.example.mvvm_practice

import android.app.Application
import com.example.mvvm_practice.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Start Koin
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyApplication)
            modules(
                listOf(appModule)
            )
        }
    }
}