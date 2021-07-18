package com.vanilla.munato

import android.util.Log
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.vanilla.munato.model.PaintingModel

class PaintingsRepository {
    private val db = Firebase.database

    fun publishPainting(paintingModel: PaintingModel) {
        // get container refs
        val paintingsRef = db.getReference("paintings")
        val childRef = paintingsRef.push()

        childRef.child("paintingID").setValue(paintingModel.paintingID)
        childRef.child("user").setValue(paintingModel.user)
        childRef.child("name").setValue(paintingModel.name)
        childRef.child("code").setValue(paintingModel.code)
        childRef.child("stars").setValue(paintingModel.stars)
    }

    fun requestPaintings(fromNodeID: String?) : List<PaintingModel> {
        val paintingsRef = db.getReference("paintings")

        paintingsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(paintingSnapshot in snapshot.children) {
                    val str = paintingSnapshot.value.toString()
                    val paintingModel = Gson().fromJson(str, PaintingModel::class.java)

                    // Log.d("a", paintingModel.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("PaintingsRepository", error.toString())
            }
        })

        return listOf()
    }
}