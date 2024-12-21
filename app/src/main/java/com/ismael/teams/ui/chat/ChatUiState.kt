package com.ismael.teams.ui.chat

import com.ismael.teams.model.Chat
import com.ismael.teams.model.Message
import com.ismael.teams.model.User

data class ChatUiState(
    val chats: List<Chat> = emptyList(),
    val currentSelectedChat: Chat? = null,
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val messages: Map<String, List<Message>> = emptyMap() // Added messages map
) {
    val currentChatMessages: List<Message>  = currentSelectedChat?.let { messages[it.jid] } ?: emptyList()
        //get() = currentSelectedChat?.let { messages[it.jid] } ?: emptyList()

}