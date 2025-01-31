package com.ismael.thecompose.data.model

data class GroupChat(
    override val jid: String,
    override val chatName: String,
    val members: List<User>,
    override val lastMessage: String?,
    override val lastMessageTime: Long?,
    override val chatPhotoUrl: String?,
    override val isUnread: Boolean,
    override val chatType: ChatType = ChatType.Group
) : Chat
