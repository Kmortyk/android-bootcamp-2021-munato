package com.vanilla.munato.repository.localstore

import android.content.Context
import android.util.Log
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
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

class LocalRepository(applicationContext: Context) {

    private val db: RoomDatabase
    private val localPaintingsDao: LocalPaintingsDao

    init {
        db = Room.databaseBuilder(
            applicationContext,
            LocalStorage::class.java, "munato-db.sqlite"
        ).build()

        localPaintingsDao = db.localPaintingsDao()
    }

    fun savePainting(user: String, code: String, preview: PaintingPreview) {
        GlobalScope.launch {
            val entity = LocalPaintingsEntity(0, user, code, PaintingPreviewMethods.compressForPublish(preview))
            localPaintingsDao.insert(entity)
        }
    }

    // loads preview from database
    fun loadPaintings(onLoad: (List<PaintingPublishData>) -> Unit) {
        GlobalScope.launch {
            val res = mutableListOf<PaintingPublishData>()

            for(record in localPaintingsDao.getAll()) {
                // Log.d("LocalRepository", "load painting " + record.uid)
                Log.d("LocalRepository", "load painting " + record.uid)

                // TODO timestamp as a name

                val date = Date(Timestamp(record.timestamp).time)

                val data = PaintingPublishData(
                    PaintingModel(record.paintingID, record.user, date.toString(), record.code, 0),
                    PaintingPreviewMethods.fromBytes(record.preview)
                )

                res.add(data)
            }

            onLoad(res)
        }
    }
}