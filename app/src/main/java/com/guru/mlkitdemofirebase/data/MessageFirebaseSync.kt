package com.guru.mlkitdemofirebase.data

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.guru.mlkitdemofirebase.data.model.Chat
import com.guru.mlkitdemofirebase.utill.FirebaseResponseCompletionHandler
import com.guru.mlkitdemofirebase.utill.FirebaseObserverType

class MessageFirebaseSync {
    private var databaseRef = FirebaseDatabase.getInstance().reference.child("chat").child(FirebaseManager.mAuth.uid!!).orderByChild("createdAt")
    private var childEventListener: ChildEventListener? = null

    fun startMessageFirebaseSync(completionHandler: FirebaseResponseCompletionHandler) {
        childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, p1: String?) {
                try {
                    val chat: Chat? = snapshot?.getValue(Chat::class.java)
                    completionHandler.onSuccess(chat, FirebaseObserverType.CHILD_ADDED)
                } catch (e: Exception) {
                    completionHandler.onFailure("")
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, p1: String?) {
                try {
                    val chat: Chat? = snapshot?.getValue(Chat::class.java)
                    completionHandler.onSuccess(chat, FirebaseObserverType.CHILD_CHANGED)
                } catch (e: Exception) {
                    completionHandler.onFailure("")
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                try {
                    val chat: Chat? = snapshot?.getValue(Chat::class.java)
                    completionHandler.onSuccess(chat, FirebaseObserverType.CHILD_REMOVED)
                } catch (e: Exception) {
                    completionHandler.onFailure("")
                }
            }

            override fun onCancelled(snapshot: DatabaseError) {}

            override fun onChildMoved(snapshot: DataSnapshot, p1: String?) {}
        }
        databaseRef.addChildEventListener(childEventListener!!)
    }

    fun stopListener() {
        childEventListener.let {
            databaseRef.removeEventListener(childEventListener!!)
            childEventListener = null
        }

    }
}