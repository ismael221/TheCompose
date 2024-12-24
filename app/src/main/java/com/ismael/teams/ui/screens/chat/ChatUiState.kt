package com.ismael.teams.ui.screens.chat

import com.ismael.teams.data.model.Chat
import com.ismael.teams.data.model.Message
import com.ismael.teams.data.model.User

data class ChatUiState(
    val chats: List<Chat> = emptyList(),
    val currentSelectedChat: Chat? = null,
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val status: String? = null,
    val error: String? = null,
    val messages: Map<String, List<Message>> = emptyMap() // Added messages map
) {
    val currentChatMessages: List<Message>  = currentSelectedChat?.let { messages[it.jid] } ?: emptyList()
        //get() = currentSelectedChat?.let { messages[it.jid] } ?: emptyList()

}