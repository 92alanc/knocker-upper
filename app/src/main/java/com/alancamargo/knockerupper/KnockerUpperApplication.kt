package com.alancamargo.knockerupper

import android.app.Application
import com.alancamargo.knockerupper.di.getModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("unused")
class KnockerUpperApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KnockerUpperApplication)
            modules(getModules())
        }
    }

}