package com.thumbtack.thumbprint

import android.app.Application

object MaterialTestApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Needed by Material Design
        setTheme(R.style.ThumbprintTheme)
    }

    override fun onTerminate() {
    }
}
