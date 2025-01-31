package com.ismael.thecompose.data.local

import androidx.core.app.NotificationCompat
import com.ismael.thecompose.data.model.Message
import com.ismael.thecompose.data.model.User

object LocalLoggedAccounts {
    val account = User(
        id = 1.toString(),
        jid = "yasmin@ismael",
        username = "yasmin",
        displayName = "Yasmin Gabrielle Rodrigues",
        avatarUrl = "",
        status = "brb",
        lastSeen = System.currentTimeMillis()
    )
    val _messages = mutableMapOf<String, List<Message>>()
    val notifications =  mutableMapOf<String, MutableList<NotificationCompat.MessagingStyle.Message>>()
}