package com.guru.mlkitdemofirebase.data.model

data class Chat(val chatId: String?,
                val userId: String?,
                val username: String?,
                val message: String?,
                val createdAt: Long?,
                val avatarUrl: String?) {
    constructor() : this(null, null, null, null, 0, null)
}
