package com.ismael.teams.ui.screens.user

import com.ismael.teams.data.model.User
import org.jivesoftware.smack.packet.Presence.Mode

data class UserUiState(
    val user: User? = null,
    val status: String? = null,
    val mode: Enum<Mode>? = null,
    val type: String? = null,
    val lastSeen: Long? = null,
)
