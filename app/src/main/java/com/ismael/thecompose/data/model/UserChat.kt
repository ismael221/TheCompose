package com.ismael.thecompose.data.model

data class UserChat(
    override val jid: String,
    override var lastMessage: String?,
    override var lastMessageTime: Long?,
    override val chatName: String,
    override val chatPhotoUrl: String?,
    override var isUnread: Boolean,
    override val chatType: ChatType = ChatType.User,
    val lastSeen: Long
) : Chat
