package com.ismael.thecompose.ui.screens.chat

import com.ismael.thecompose.data.model.Chat
import com.ismael.thecompose.data.model.Message
import com.ismael.thecompose.data.model.User
import org.jivesoftware.smack.packet.Presence
import org.jivesoftware.smack.packet.Presence.Mode
import org.jivesoftware.smackx.chatstates.ChatState

data class ChatUiState(
    val chats: List<Chat> = emptyList(),
    val currentSelectedChat: Chat? = null,
    val lastMessage: Message? = null,
    val lastMessageTimestamp: Long? = null,
    val lastSelectedChat: Chat? = null,
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val unReadMessages: Int = 0,
    val currentLoggedInUser: User? = null,
    val mode: Enum<Mode>? = null,
    val type: String? = null,
    val error: String? = null,
    val chatState: ChatState? = null,
    val presence : Pair<String, Presence?> = Pair("", null),
    val messages: Map<String, List<Message>> = emptyMap() // Added messages map
) {
    val currentChatMessages: List<Message>  = currentSelectedChat?.let { messages[it.jid] } ?: emptyList()
        //get() = currentSelectedChat?.let { messages[it.jid] } ?: emptyList()

}