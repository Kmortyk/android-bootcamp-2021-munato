package com.vanilla.munato

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.vanilla.munato.model.PaintingModel
import com.vanilla.munato.model.Painting
import java.io.ByteArrayOutputStream

class PaintingsRepository {
    companion object {
        const val LOG_TAG = "PaintingsRepository"

        const val KEY_PAINTING_ID = "paintingID"
        const val KEY_USER = "user"
        const val KEY_NAME = "name"
        const val KEY_CODE = "code"
        const val KEY_STARS = "stars"
    }

    private val db = Firebase.database
    private val storage = Firebase.storage

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
        val storageRef = storage.getReference("images/" + painting.model.paintingID + ".jpg")
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

    fun requestPaintings(fromNodeID: String?) : List<Painting> {
        val paintingsRef = db.getReference("paintings")
        val storageRef = storage.getReference("images")

        paintingsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(paintingSnapshot in snapshot.children) {
                    val str = paintingSnapshot.value.toString()
                    val paintingModel = Gson().fromJson(str, PaintingModel::class.java)

                    val pictureRef = storageRef.parent?.child(paintingModel.paintingID + ".jpg")

                    Log.d("a", paintingModel.toString())
                    Log.d("b", pictureRef!!.bucket)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(LOG_TAG, error.toString())
            }
        })

        return listOf()
    }
}