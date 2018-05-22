package com.guru.mlkitdemofirebase

import android.app.Application
import android.content.Context

class App : Application() {

    companion object {
        lateinit var appContext: Context

        fun get(): App = appContext as App
    }

    override fun onCreate() {
        super.onCreate()
        //initFirebase()
    }
}