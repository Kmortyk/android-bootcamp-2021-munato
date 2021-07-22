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

    @Update // TODO
    fun updatePaintingID(uid: Int, paintingID: String)

    @Insert // TODO
    fun insert(code: String, preview: PaintingPreview)
}