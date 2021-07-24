package com.vanilla.munato.repository.server

import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.vanilla.munato.model.PaintingDownloadData
import java.lang.Exception

class UsersRepository {
    companion object {
        const val LOG_TAG = "UsersRepository"

        const val KEY_STARRED = "starred"
        const val KEY_FAVOURITE = "favourite"
    }

    private val db = Firebase.database

    fun addFavourite(paintingID: String) {
        val userCredential = "kmortyk" // TODO log in
    }

    fun loadFavourites(onPaintingLoaded: (List<String>) -> Unit, onFailure: (DatabaseError) -> Unit) {

    }
}