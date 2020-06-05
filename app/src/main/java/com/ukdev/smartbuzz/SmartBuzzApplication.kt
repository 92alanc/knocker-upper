package com.ukdev.smartbuzz

import android.app.Application
import com.ukdev.smartbuzz.di.getModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("unused")
class SmartBuzzApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SmartBuzzApplication)
            modules(getModules())
        }
    }

}