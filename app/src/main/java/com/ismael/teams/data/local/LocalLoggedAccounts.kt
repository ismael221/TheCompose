package com.ismael.teams.data.local

import androidx.core.app.NotificationCompat
import com.ismael.teams.data.model.Message
import com.ismael.teams.data.model.User

object LocalLoggedAccounts {
    val account = User(
        id = 1.toString(),
        jid = "ismael221@ismael",
        username = "ismael221",
        displayName = "Ismael Campos Rodrigues",
        avatarUrl = "",
        status = "dnd",
        lastSeen = System.currentTimeMillis()
    )
    val _messages = mutableMapOf<String, List<Message>>()
    val notifications =  mutableMapOf<String, MutableList<NotificationCompat.MessagingStyle.Message>>()
}