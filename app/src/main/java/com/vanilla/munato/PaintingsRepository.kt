package com.vanilla.munato

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.vanilla.munato.model.*
import java.io.ByteArrayOutputStream


class PaintingsRepository {
    companion object {
        const val LOG_TAG = "PaintingsRepository"

        const val KEY_PAINTING_ID = "paintingID"
        const val KEY_USER = "user"
        const val KEY_NAME = "name"
        const val KEY_CODE = "code"
        const val KEY_STARS = "stars"

        const val STORAGE_IMAGES_DIR = "images"
        const val DOWNLOAD_BANDWIDTH: Long = 1024 * 1024 // 1 MB
    }

    private val db = Firebase.database
    private val storage = Firebase.storage

    // PUBLISH

    fun publishPainting(painting: Painting, onSuccessFunction: () -> Unit) {
        publishPaintingPreview(painting) {
            publishPaintingModel(painting) {
                onSuccessFunction()
            }
        }
    }

    private fun publishPaintingModel(painting: Painting, onSuccessFunction: () -> Unit) {
        val paintingsRef = db.getReference("paintings")
        val childRef = paintingsRef.push()
        val model = painting.model

        childRef.child(KEY_PAINTING_ID).setValue(model.paintingID)
        childRef.child(KEY_USER).setValue(model.user)
        childRef.child(KEY_NAME).setValue(model.name)
        childRef.child(KEY_CODE).setValue(model.code)
        childRef.child(KEY_STARS).setValue(model.stars)

        onSuccessFunction()
    }

    private fun publishPaintingPreview(painting: Painting, onSuccessFunction: () -> Unit) {
        val storageRef = storage.getReference(STORAGE_IMAGES_DIR + "/" + painting.model.paintingID + ".jpg")
        val compressedStream = ByteArrayOutputStream()

        painting.preview.compress(Bitmap.CompressFormat.JPEG, 100, compressedStream)

        storageRef.putBytes(compressedStream.toByteArray())
            .addOnFailureListener {
                Log.e(LOG_TAG, it.toString())
            }
            .addOnSuccessListener {
                onSuccessFunction()
            }
    }

    // DOWNLOAD

    fun loadPaintings(callback: (List<Painting>) -> Unit) {
        val paintingsRef = db.getReference("paintings")

        paintingsRef.addValueEventListener(object : ValueEventListener {
            val resultList = mutableListOf<Painting>()

            override fun onDataChange(snapshot: DataSnapshot) {
                for(paintingSnapshot in snapshot.children) {
                    resultList.add(Painting(
                        loadModel(paintingSnapshot),
                        EmptyPaintingPreview,
                    ))

//                    loadPreview(paintingSnapshot) {
//
//                    }
                }

                callback(resultList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(LOG_TAG, error.toString())
            }
        })
    }

    private fun loadModel(snapshot: DataSnapshot) : PaintingModel {
        if(!snapshot.hasChild(KEY_PAINTING_ID) || !snapshot.hasChild(KEY_USER) ||
            !snapshot.hasChild(KEY_NAME) || !snapshot.hasChild(KEY_CODE) || !snapshot.hasChild(KEY_STARS)) {
            Log.e(LOG_TAG, "snapshot has no necessary fields, id '${snapshot.key}'")
            return EmptyPaintingModel
        }

        val stars = snapshot.child(KEY_STARS).value.toString().toIntOrNull()

        if(stars == null) {
            Log.e(LOG_TAG, "error while converting '${snapshot.child(KEY_STARS).value}' to int.")
            return EmptyPaintingModel
        }

        return PaintingModel(
            snapshot.child(KEY_PAINTING_ID).getValue(String::class.java),
            snapshot.child(KEY_USER).getValue(String::class.java),
            snapshot.child(KEY_NAME).getValue(String::class.java),
            snapshot.child(KEY_CODE).getValue(String::class.java),
            stars,
        )
    }

    private fun loadPreview(snapshot: DataSnapshot, onSuccessFunction: (PaintingPreview) -> Unit) {
        if(!snapshot.hasChild(KEY_PAINTING_ID)) {
            Log.e(LOG_TAG, "can't load preview for painting without paintingID, id '${snapshot.key}'")
            onSuccessFunction(EmptyPaintingPreview)
            return
        }

        val paintingID = snapshot.child(KEY_PAINTING_ID).getValue(String::class.java)
        val storageRef = storage.getReference(STORAGE_IMAGES_DIR)

        val pictureRef = storageRef.child("${paintingID}.jpg")

        pictureRef.getBytes(DOWNLOAD_BANDWIDTH).addOnSuccessListener {
            val preview = BitmapFactory.decodeByteArray(it, 0, it.size)
            onSuccessFunction(preview as PaintingPreview)
        }.addOnFailureListener {
            Log.e(LOG_TAG, "error while downloading preview: $it'")
            onSuccessFunction(EmptyPaintingPreview)
        }
    }

}