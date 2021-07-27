package com.vanilla.munato.repository.localstore.localpaintings

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.vanilla.munato.model.PaintingPreview

@Dao
interface LocalPaintingsDao {
    @Query("SELECT * FROM local_paintings")
    fun getAll(): List<LocalPaintingsEntity>

    @Update
    fun update(record: LocalPaintingsEntity)

    @Insert
    fun insert(record: LocalPaintingsEntity)
}