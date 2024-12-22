package com.ismael.teams.model

interface Chat {
    val jid: String
    val lastMessage: String?
    val lastMessageTime: Long?
    val chatName: String
    val chatPhotoUrl: String?
    val isUnread: Boolean
    val chatType: ChatType
}