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
        val paintingID = painting.model.paintingID

        if(paintingID == null) {
            Log.e(LOG_TAG, "paintingID is null")
            return
        }

        val paintingsRef = db.getReference("paintings")
        val childRef = paintingsRef.child(paintingID)
        val model = painting.model

        childRef.setValue("")

        childRef.child(KEY_USER).setValue(model.user)
        childRef.child(KEY_NAME).setValue(model.name)
        childRef.child(KEY_CODE).setValue(model.code)
        childRef.child(KEY_STARS).setValue(model.stars)

        onSuccessFunction()
    }

    private fun publishPaintingPreview(painting: PaintingPublishData, onSuccessFunction: () -> Unit, onFailureFunction: (Exception) -> Unit) {
        val storageRef = storage.getReference(STORAGE_IMAGES_DIR + "/" + painting.model.paintingID + ".jpg")
        val preview = PaintingPreviewMethods.compressForPublish(painting.preview)

        storageRef.putBytes(preview)
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

    fun loadPainting(paintingID: String, onSuccess: (PaintingDownloadData) -> Unit, onFailure: (DatabaseError) -> Unit) {
        db.getReference("paintings").child(paintingID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val model = loadModel(snapshot)
                loadPreview(snapshot) {
                    onSuccess(PaintingDownloadData(model, it))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onFailure(error)
            }
        })
    }

    private fun loadModel(snapshot: DataSnapshot) : PaintingModel {
        if(!snapshot.hasChild(KEY_USER) ||
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
            snapshot.key,
            snapshot.child(KEY_USER).getValue(String::class.java),
            snapshot.child(KEY_NAME).getValue(String::class.java),
            snapshot.child(KEY_CODE).getValue(String::class.java),
            stars,
        )
    }

    private fun loadPreview(snapshot: DataSnapshot, onLoadedFunction: (Uri) -> Unit) {
        val paintingID = snapshot.key
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
        changeStarValue(paintingID, { it + 1 }, onSuccess, onFailure)
    }

    fun removeStar(paintingID: String, onSuccess: () -> Unit, onFailure: (DatabaseError) -> Unit) {
        changeStarValue(paintingID, { if(it > 0) { it - 1 } else 0 }, onSuccess, onFailure)
    }

    private fun changeStarValue(paintingID: String, changeValueFun: (Int) -> Int, onSuccess: () -> Unit, onFailure: (DatabaseError) -> Unit) {
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

                currentData.value = changeValueFun(previous)

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