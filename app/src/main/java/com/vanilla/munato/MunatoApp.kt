package com.vanilla.munato

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize

/* TODO
   - сохранение работ в Room, чтобы не терять их при выходе с экрана или приложения
   - favourites download

   - удаление звёзд и сердечек

   - кнопки edit

   - оптимизация списка картин
*  */

class MunatoApp : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(baseContext)

        // TODO dark theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}