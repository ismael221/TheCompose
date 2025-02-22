package com.ismael.thecompose.data.model

import com.ismael.thecompose.ui.utils.MessageType
import java.util.UUID

data class Message(
    val key: String = UUID.randomUUID().toString(),
    val content: String,
    val to: String,
    val type: MessageType,
    val senderId: String,
    val timestamp: Long,
)
