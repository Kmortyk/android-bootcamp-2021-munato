package com.vanilla.munato

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize

class MunatoApp : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(baseContext)
    }
}