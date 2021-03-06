package com.vanilla.munato

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize

/* TODO
    - запретить менять опубликованные произведения
    - пресеты для рисования
*  */

class MunatoApp : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(baseContext)

        // TODO dark theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}