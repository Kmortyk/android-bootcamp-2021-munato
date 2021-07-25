package com.vanilla.munato.repository.localstore

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vanilla.munato.repository.localstore.localpaintings.LocalPaintingsDao

class LocalRepository(applicationContext: Context) {

    private val db: RoomDatabase
    private val localPaintingsDao: LocalPaintingsDao

    init {
        db = Room.databaseBuilder(
            applicationContext,
            LocalStorage::class.java, "munato-db"
        ).build()

        localPaintingsDao = db.localPaintingsDao()
    }

}