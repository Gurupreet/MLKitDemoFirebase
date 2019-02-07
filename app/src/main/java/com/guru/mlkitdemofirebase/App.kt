package com.guru.mlkitdemofirebase

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}