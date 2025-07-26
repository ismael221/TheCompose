package com.ismael.thecompose.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ismael.thecompose.ui.utils.MessageType
import java.util.UUID

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val key: String = UUID.randomUUID().toString(),
    val content: String,
    val to: String,
    val type: MessageType,
    val senderId: String,
    val timestamp: Long,
)