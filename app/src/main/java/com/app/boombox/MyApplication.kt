package com.app.boombox

import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTree())
    }
}