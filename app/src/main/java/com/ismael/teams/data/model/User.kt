package com.ismael.teams.data.model

data class User(
    val id: String,
    val jid: String,
    val username: String,
    val displayName: String,
    val avatarUrl: String? = null,
    val status: String? = null,
    val lastSeen: Long? = null
)
