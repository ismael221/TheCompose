package com.ismael.teams.data.local

import com.ismael.teams.data.model.User

object LocalLoggedAccounts {
    val account = User(
        id = 1.toString(),
        jid = "ismael221@ismael",
        username = "ismael221",
        displayName = "Ismael Nunes Campos",
        avatarUrl = "",
        status = "dnd",
        lastSeen = System.currentTimeMillis()
    )
}