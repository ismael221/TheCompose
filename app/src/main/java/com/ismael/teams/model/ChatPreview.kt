package com.ismael.teams.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import java.util.UUID


data class ChatPreview(
    @StringRes val username: Int,
    @DrawableRes val userImage: Int,
    @StringRes val lastMessage: Int,
    val key: String = UUID.randomUUID().toString()
)