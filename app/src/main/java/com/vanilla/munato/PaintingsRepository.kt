package com.vanilla.munato

import android.util.Log
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.vanilla.munato.model.PaintingModel

class PaintingsRepository {
    val db = Firebase.database

    fun publishPainting(paintingModel: PaintingModel) {
        // get container refs
        val paintingsRef = db.getReference("paintings")
        val childRef = paintingsRef.push()

        // serialize and post
        val serialized = Gson().toJson(paintingModel)
        childRef.setValue(serialized)
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

            override fun onCancelled(error: DatabaseError) { /* none */ }
        })

        return listOf()
    }
}