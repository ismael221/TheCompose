package com.ismael.teams.data.model

import com.ismael.teams.ui.utils.MessageType
import java.util.UUID

data class Message(
    val key: String = UUID.randomUUID().toString(),
    val content: String,
    val to: String,
    val type: MessageType,
    val senderId: String,
    val timestamp: Long,
)
