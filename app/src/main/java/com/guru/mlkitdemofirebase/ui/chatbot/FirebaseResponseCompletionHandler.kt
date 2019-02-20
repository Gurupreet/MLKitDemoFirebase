package com.guru.mlkitdemofirebase.ui.chatbot

import com.guru.mlkitdemofirebase.utill.FirebaseObserverType

interface FirebaseResponseCompletionHandler {
    fun onSuccess(any: Any?, observerType: FirebaseObserverType)
    fun onFailure(error: String)
}