package com.guru.mlkitdemofirebase.data

interface MLResponseListener {
    fun onSuccess( result : Any, type : String)
    fun onFailure(error : String )
}