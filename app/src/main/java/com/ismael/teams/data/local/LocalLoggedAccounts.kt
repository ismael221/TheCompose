package com.ismael.teams.data.local

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
}