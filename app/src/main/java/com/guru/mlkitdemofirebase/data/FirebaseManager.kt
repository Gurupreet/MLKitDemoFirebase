package com.guru.mlkitdemofirebase.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class FirebaseManager {
    companion object {
        private val databaseReferece: DatabaseReference = FirebaseDatabase.getInstance().reference
        private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
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

    fun sendMessage(message: String) {
        val ref = databaseReferece.child("chat")
        val key = ref.push().key ?: ""
        val map = HashMap<String, Any>()

        map["message"] = message
        map["createdAt"] = -System.currentTimeMillis()
        map["username"] = "User"
        map["avatarUrl"] = ""
        map["userId"] = mAuth.uid ?: ""

        ref.child(key).setValue(map).addOnSuccessListener {

        }.addOnFailureListener {
            it.printStackTrace()
        }
    }




}