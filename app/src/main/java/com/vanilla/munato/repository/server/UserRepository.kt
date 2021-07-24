package com.vanilla.munato.repository.server

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserRepository {
    companion object {
        const val LOG_TAG = "UsersRepository"

        const val KEY_STARRED = "starred"
        const val KEY_FAVOURITE = "favourite"
    }

    private val db = Firebase.database

    fun createUser() { // TODO call in login
        val userRef = userRef()

        userRef.child(KEY_STARRED).setValue("")
        userRef.child(KEY_FAVOURITE).setValue("")
    }

    fun addFavourite(paintingID: String) {
        userRef().child(KEY_FAVOURITE).child(paintingID).setValue("")
    }

    fun isFavourite(paintingID: String, onSuccess: (Boolean) -> Unit, onFailure: (DatabaseError) -> Unit) {
        userRef().child(KEY_FAVOURITE).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                onSuccess(snapshot.hasChild(paintingID))
            }

            override fun onCancelled(error: DatabaseError) {
                onFailure(error)
            }
        })
    }

    fun loadFavourites(onFavouritesLoaded: (List<String>) -> Unit, onFailure: (DatabaseError) -> Unit) {
        userRef().child(KEY_FAVOURITE).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val res = mutableListOf<String>()

                for(favouriteSnapshot in snapshot.children) {
                    favouriteSnapshot.key?.let {
                        res.add(it)
                    }
                }

                onFavouritesLoaded(res)
            }

            override fun onCancelled(error: DatabaseError) {
                onFailure(error)
            }
        })
    }

    fun addStarred(paintingID: String) {
        userRef().child(KEY_STARRED).child(paintingID).setValue("")
    }

    fun hasStarred(paintingID: String, onSuccess: (Boolean) -> Unit, onFailure: (DatabaseError) -> Unit) {
        userRef().child(KEY_STARRED).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                onSuccess(snapshot.hasChild(paintingID))
            }

            override fun onCancelled(error: DatabaseError) {
                onFailure(error)
            }
        })
    }

    private fun userCredential() = "kmortyk" // TODO add sign up and log in

    private fun userRef(): DatabaseReference {
        val usersRef = db.getReference("users")

        return usersRef.child(userCredential())
    }
}