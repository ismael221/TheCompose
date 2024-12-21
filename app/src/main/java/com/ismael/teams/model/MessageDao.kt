package com.ismael.teams.model

interface MessageDao {
    suspend fun insertMessage(message: Message)
    suspend fun getMessagesForChat(chatId: String): List<Message>
}