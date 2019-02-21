package com.guru.mlkitdemofirebase.utill

import com.guru.mlkitdemofirebase.utill.FirebaseObserverType

interface FirebaseResponseCompletionHandler {
    fun onSuccess(any: Any?, observerType: FirebaseObserverType)
    fun onFailure(error: String)
}