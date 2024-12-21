package com.ismael.teams.model

class InMemoryMessageDao : MessageDao {
    private val messages = mutableMapOf<String, MutableList<Message>>()

    override suspend fun insertMessage(message: Message) {
        val chatId = message.senderId // Assuming senderId is the chatId for now
        val chatMessages = messages[chatId] ?: mutableListOf()
        chatMessages.add(message)
        messages[chatId] = chatMessages
    }

    override suspend fun getMessagesForChat(chatId: String): List<Message> {
        return messages[chatId] ?: emptyList()
    }
}