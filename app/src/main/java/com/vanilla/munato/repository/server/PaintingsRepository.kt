package com.vanilla.munato.repository.server

import android.graphics.Bitmap
import android.net.Uri
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
    }

    private val db = Firebase.database
    private val storage = Firebase.storage

    // PUBLISH

    fun publishPainting(painting: PaintingPublishData, onSuccessFunction: () -> Unit, onFailureFunction: (Exception) -> Unit) {
        publishPaintingPreview(painting,
            onSuccessFunction={
                publishPaintingModel(painting) {
                    onSuccessFunction()
                }
            },
            onFailureFunction={
                onFailureFunction(it)
            })
    }

    private fun publishPaintingModel(painting: PaintingPublishData, onSuccessFunction: () -> Unit) {
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

    private fun publishPaintingPreview(painting: PaintingPublishData, onSuccessFunction: () -> Unit, onFailureFunction: (Exception) -> Unit) {
        val storageRef = storage.getReference(STORAGE_IMAGES_DIR + "/" + painting.model.paintingID + ".jpg")
        val compressedStream = ByteArrayOutputStream()

        painting.preview.compress(Bitmap.CompressFormat.JPEG, 100, compressedStream)

        storageRef.putBytes(compressedStream.toByteArray())
            .addOnSuccessListener {
                onSuccessFunction()
            }
            .addOnFailureListener {
                Log.e(LOG_TAG, it.toString())
                onFailureFunction(it)
            }
    }

    // DOWNLOAD

    fun loadPaintings(onPaintingLoaded: (PaintingDownloadData) -> Unit, onFailure: (DatabaseError) -> Unit) {
        val paintingsRef = db.getReference("paintings")

        paintingsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(paintingSnapshot in snapshot.children) {
                    val model = loadModel(paintingSnapshot)

                    loadPreview(paintingSnapshot) {
                        onPaintingLoaded(PaintingDownloadData(model, it))
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(LOG_TAG, error.toString())
                onFailure(error)
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
            Log.e(LOG_TAG, "stars count is not numeric value ('${snapshot.child(KEY_STARS).value}')")
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

    private fun loadPreview(snapshot: DataSnapshot, onLoadedFunction: (Uri) -> Unit) {
        if(!snapshot.hasChild(KEY_PAINTING_ID)) {
            Log.e(LOG_TAG, "can't load preview for painting without paintingID, id '${snapshot.key}'")
            onLoadedFunction(Uri.EMPTY)
            return
        }

        val paintingID = snapshot.child(KEY_PAINTING_ID).getValue(String::class.java)
        val storageRef = storage.getReference("$STORAGE_IMAGES_DIR/$paintingID.jpg")

        storageRef.downloadUrl
            .addOnSuccessListener {
                Log.d(LOG_TAG, "image exists, uri: $it'")
                onLoadedFunction(it)
            }
            .addOnFailureListener {
                Log.e(LOG_TAG, "error while downloading preview: $it'")
                onLoadedFunction(Uri.EMPTY)
            }
    }

    fun addStar(paintingID: String, onSuccess: () -> Unit, onFailure: (DatabaseError) -> Unit) {
        val paintingsRef = db.getReference("paintings")
        val childRef = paintingsRef.child(paintingID)

        childRef.child(KEY_STARS).runTransaction(object: Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                val value = currentData.value ?: run {
                    Log.e(LOG_TAG, "current data value is null")
                    return Transaction.abort()
                }

                val previous = value.toString().toIntOrNull() ?: run {
                    Log.e(LOG_TAG, "previous data value is not number ${value.toString()}")
                    return Transaction.abort()
                }

                currentData.value = previous + 1

                return Transaction.success(currentData)
            }

            override fun onComplete(error: DatabaseError?, committed: Boolean, currentData: DataSnapshot?) {
                if(error != null) {
                    onFailure(error)
                } else {
                    onSuccess()
                }
            }
        })
    }
}