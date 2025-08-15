package com.app.watchtime

import android.app.Application
import com.app.watchtime.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WatchTimeApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Koin DI
        startKoin {
            androidContext(this@WatchTimeApplication)
            modules(appModules)
        }
    }
}
