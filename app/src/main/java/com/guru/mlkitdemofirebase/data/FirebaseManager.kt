package com.guru.mlkitdemofirebase.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class FirebaseManager {
    companion object {
        private val databaseReferece: DatabaseReference = FirebaseDatabase.getInstance().reference
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        private val INSTANCE : FirebaseManager? = null
        fun get() : FirebaseManager =
                INSTANCE ?: synchronized(this) {
                    INSTANCE
                            ?: FirebaseManager()
                }
    }

    fun createUserAuth() {
        if (mAuth.currentUser == null) {
            mAuth.signInAnonymously().addOnFailureListener {
                it.printStackTrace()
            }
        }
    }

    fun sendMessage(message: String, username: String) {
        mAuth.uid?.let { userId ->
            val ref = databaseReferece.child("chat").child(userId)
            val key = ref.push().key ?: ""
            val map = HashMap<String, Any>()

            map["message"] = message
            map["createdAt"] = System.currentTimeMillis()
            map["username"] = username
            map["avatarUrl"] = ""
            map["userId"] = userId

            ref.child(key).setValue(map)
        }
    }

    fun deleteAllChat() {
        mAuth.uid?.let { userId ->
            databaseReferece.child("chat").child(userId).setValue(null)
        }
    }

}