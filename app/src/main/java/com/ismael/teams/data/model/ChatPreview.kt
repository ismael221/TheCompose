package com.ismael.teams.data.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import java.util.UUID


data class ChatPreview(
    @StringRes val username: Int,
    @DrawableRes val userImage: Int,
    @StringRes val lastMessage: Int,
    val lastMessageTime: Long?,
    val isUnread: Boolean,
    val key: String = UUID.randomUUID().toString()
)