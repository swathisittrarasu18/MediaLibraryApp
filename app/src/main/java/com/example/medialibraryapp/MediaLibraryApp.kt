package com.example.medialibraryapp

import android.app.Application
import com.example.data.di.dataModule
import com.example.medialibraryapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MediaLibraryApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Start Koin
        startKoin {
            androidContext(this@MediaLibraryApp)
            modules(listOf(appModule, dataModule))
        }
    }
}