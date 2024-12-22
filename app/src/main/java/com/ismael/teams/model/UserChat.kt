package com.ismael.teams.model

data class UserChat(
    override val jid: String,
    override val lastMessage: String?,
    override val lastMessageTime: Long?,
    override val chatName: String,
    override val chatPhotoUrl: String?,
    override val isUnread: Boolean,
    override val chatType: ChatType = ChatType.User,
     val lastSeen: Long
) : Chat
