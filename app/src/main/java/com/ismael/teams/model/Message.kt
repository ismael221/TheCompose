package com.ismael.teams.model

import java.util.UUID

data class Message(
    val key: String = UUID.randomUUID().toString(),
    val content: String,
    val from: String
)
