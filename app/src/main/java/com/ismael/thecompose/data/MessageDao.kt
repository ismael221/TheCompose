package com.ismael.thecompose.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMessage(message: Message)

    @Update
    suspend fun updateMessage(message: Message)

    @Delete
    suspend fun deleteMessage(message: Message)

    @Query("SELECT * FROM messages WHERE senderId = :senderId ORDER BY timestamp DESC")
    fun getAllMessages(senderId: String): Flow<List<Message>>

    @Query("SELECT * FROM messages WHERE senderId = :senderId ORDER BY timestamp DESC LIMIT 1")
    fun getLastMessageStream(senderId: String): Flow<Message>


}