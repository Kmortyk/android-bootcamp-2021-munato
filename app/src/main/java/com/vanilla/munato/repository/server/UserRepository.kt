package com.vanilla.munato.repository.server

import com.google.firebase.auth.FirebaseAuth
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

    fun removeFavourite(paintingID: String) {
        userRef().child(KEY_FAVOURITE).child(paintingID).removeValue()
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

    fun removeStarred(paintingID: String) {
        userRef().child(KEY_STARRED).child(paintingID).removeValue()
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

    fun authorized() = FirebaseAuth.getInstance().currentUser != null

    fun userName() : String {
        val user = FirebaseAuth.getInstance().currentUser

        return user?.displayName ?: "<err user>"
    }

    fun userCredential() : String {
        val user = FirebaseAuth.getInstance().currentUser

        return user?.uid ?: "<err uid>"
    }

    private fun userRef(): DatabaseReference {
        val usersRef = db.getReference("users")

        return usersRef.child(userCredential())
    }
}