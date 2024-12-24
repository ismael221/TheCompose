package com.ismael.teams.data.repository

import com.ismael.teams.data.model.Chat
import com.ismael.teams.data.model.UserChat

class DataSource {
    fun loadChats(): List<Chat> {
        return listOf(
            UserChat(
                jid = "yasmin@ismael",
                lastMessage = "Olá amor",
                lastMessageTime = System.currentTimeMillis(),
                chatName = "Yasmin Rodrigues",
                isUnread = true,
                lastSeen = System.currentTimeMillis(),
                chatPhotoUrl = ""
            ),
            UserChat(
                jid = "debora@ismael",
                lastMessage = "Olá",
                lastMessageTime = System.currentTimeMillis(),
                chatName = "Debora Nunes",
                isUnread = false,
                lastSeen = System.currentTimeMillis(),
                chatPhotoUrl = ""
            )

        )

    }
}