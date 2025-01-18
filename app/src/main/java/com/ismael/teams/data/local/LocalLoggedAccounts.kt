package com.ismael.teams.data.local

import androidx.core.app.NotificationCompat
import com.ismael.teams.data.model.Message
import com.ismael.teams.data.model.User

object LocalLoggedAccounts {
    val account = User(
        id = 1.toString(),
        jid = "yasmin@ismael",
        username = "yasmin",
        displayName = "Yasmin Rodrigues",
        avatarUrl = "",
        status = "dnd",
        lastSeen = System.currentTimeMillis()
    )
    val _messages = mutableMapOf<String, List<Message>>()
    val notifications =  mutableMapOf<String, MutableList<NotificationCompat.MessagingStyle.Message>>()
}