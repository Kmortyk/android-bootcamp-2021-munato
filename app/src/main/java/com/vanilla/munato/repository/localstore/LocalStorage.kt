package com.vanilla.munato.repository.localstore

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vanilla.munato.repository.localstore.localpaintings.LocalPaintingsDao
import com.vanilla.munato.repository.localstore.localpaintings.LocalPaintingsEntity

@Database(entities = [LocalPaintingsEntity::class], version = 1)
abstract class LocalStorage : RoomDatabase() {
    abstract fun localPaintingsDao(): LocalPaintingsDao
}