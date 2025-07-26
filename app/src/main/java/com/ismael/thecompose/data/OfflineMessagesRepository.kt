package com.ismael.thecompose.data

import kotlinx.coroutines.flow.Flow

class OfflineMessagesRepository(private val messageDao: MessageDao) : MessageRepository {

    override suspend fun insertMessage(message: Message) = messageDao.insertMessage(message)

    override suspend fun updateMessage(message: Message) = messageDao.updateMessage(message)

    override suspend fun deleteMessage(message: Message)  = messageDao.deleteMessage(message)

    override fun getAllMessages(senderId: String): Flow<List<Message>> = messageDao.getAllMessages(senderId)

    override fun getLastMessage(senderId: String): Flow<Message?> = messageDao.getLastMessageStream(senderId)
}