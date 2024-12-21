package com.ismael.teams.ui.chat

import android.util.Log
import androidx.activity.result.launch
import androidx.compose.animation.core.copy
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ismael.teams.model.Chat
import com.ismael.teams.model.InMemoryMessageDao
import com.ismael.teams.model.Message
import com.ismael.teams.model.MessageDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.get
import kotlin.collections.orEmpty
import kotlin.collections.toMutableMap

class ChatViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()
    private val messageDao: MessageDao = InMemoryMessageDao() // Use the in-memory implementation

    private  val _messages = mutableMapOf<String,List<Message>>()


    // ... other code ...



    fun loadMessagesForChat(chatId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
             //   val messages = messageDao.getMessagesForChat(chatId) // Fetch messages from data source
                val messages = _messages[chatId]
                addMessageToMap(
                    _messages,
                    "yasmin@ismael",
                    Message(
                        text = "Olá Ismael",
                        to = "ismael221@ismael",
                        senderId = "yasmin@ismael",
                        timestamp = System.currentTimeMillis(),
                    )
                )
                _uiState.update {
                    it.copy(
                        messages = it.messages.toMutableMap().apply {
                            if (messages != null) {
                                put(chatId, messages)
                            }
                        },
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    private fun addMessageToMap(map: MutableMap<String, List<Message>>, key: String, message: Message) {
        Log.i("Mensagem adicionada",key)
        map.getOrPut(key) { emptyList() }
            .let { it + message }
            .also { map[key] = it }

    }



    fun sendMessage(chatId: String, message: Message) {
        viewModelScope.launch {
            try {
                addMessageToMap(_messages,chatId,message)

                _uiState.update {
                    it.copy(
                        messages = it.messages.toMutableMap().apply {
                            val currentMessages = get(chatId).orEmpty()
                            put(chatId, currentMessages + message)
                        }
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    init {
        addMessageToMap(
            _messages,
            "yasmin@ismael",
            Message(
                text = "Olá",
                to = "ismael221@ismael",
                senderId = "yasmin@ismael",
                timestamp = System.currentTimeMillis(),
            )
        )

        Log.i("Mensages", _messages.get("yasmin@ismael").toString())

    }



}