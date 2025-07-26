package com.ismael.thecompose.data

import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    suspend fun insertMessage(message: Message)

    suspend fun updateMessage(message: Message)

    suspend fun deleteMessage(message: Message)

    fun getAllMessages(senderId: String): Flow<List<Message>>

    fun getLastMessage(senderId: String): Flow<Message?>

}