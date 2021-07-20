package com.vanilla.munato

import android.graphics.Bitmap
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.vanilla.munato.model.PaintingModel
import com.vanilla.munato.model.PaintingPreview
import java.io.ByteArrayOutputStream

class PaintingsRepository {
    private val db = Firebase.database
    private val storage = Firebase.storage

    fun publishPainting(paintingPreview: PaintingPreview, onSuccessFunction: () -> Unit) {
        // get container refs
        val paintingsRef = db.getReference("paintings")
        val storageRef = storage.getReference("images/" + paintingPreview.model.paintingID + ".jpg")

        val childRef = paintingsRef.push()

        val baos = ByteArrayOutputStream()
        paintingPreview.preview.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = storageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            Log.e("PaintingsRepository", it.toString())
            throw it
        }.addOnSuccessListener { taskSnapshot ->
            val model = paintingPreview.model

            childRef.child("paintingID").setValue(model.paintingID)
            childRef.child("user").setValue(model.user)
            childRef.child("name").setValue(model.name)
            childRef.child("code").setValue(model.code)
            childRef.child("stars").setValue(model.stars)

            onSuccessFunction()
        }
    }

    fun requestPaintings(fromNodeID: String?) : List<PaintingPreview> {
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
                Log.e("PaintingsRepository", error.toString())
            }
        })

        return listOf()
    }
}