package com.vanilla.munato.repository.localstore

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vanilla.munato.model.PaintingModel
import com.vanilla.munato.model.PaintingPreview
import com.vanilla.munato.model.PaintingPreviewMethods
import com.vanilla.munato.model.PaintingPublishData
import com.vanilla.munato.repository.localstore.localpaintings.LocalPaintingsDao
import com.vanilla.munato.repository.localstore.localpaintings.LocalPaintingsEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


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

    fun savePainting(code: String, preview: PaintingPreview) {
        GlobalScope.launch {
            val entity = LocalPaintingsEntity(0, code, PaintingPreviewMethods.toBase64(preview))
            localPaintingsDao.insert(entity)
        }
    }

    // loads preview from database
    fun loadPaintings(onLoad: (List<PaintingPublishData>) -> Unit) {
        GlobalScope.launch {
            val res = mutableListOf<PaintingPublishData>()

            for(record in localPaintingsDao.getAll()) {
                val data = PaintingPublishData(
                    PaintingModel(record.paintingID, "", "", record.code, 0),
                    PaintingPreviewMethods.fromBase64(record.paintingID)
                )

                res.add(data)
            }

            onLoad(res)
        }
    }
}