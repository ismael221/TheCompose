package com.ismael.teams.data.local

import com.ismael.teams.data.model.ChatType
import com.ismael.teams.data.model.GroupChat
import com.ismael.teams.data.model.UserChat

object LocalChatsDataProvider {

    val chats = listOf(
        UserChat(
            jid = "yasmin@ismael",
            lastMessage = "Olá amor",
            lastMessageTime = System.currentTimeMillis(),
            chatName = "Yasmin Rodrigues",
            isUnread = true,
            lastSeen = System.currentTimeMillis(),
            chatPhotoUrl = ""
        ),
        UserChat(
            jid = "debora@ismael",
            lastMessage = "Olá",
            lastMessageTime = System.currentTimeMillis()- 2,
            chatName = "Debora Nunes",
            isUnread = false,
            lastSeen = System.currentTimeMillis(),
            chatPhotoUrl = ""
        ),
        UserChat(
            jid = "ismael221@ismael",
            lastMessage = "Olá",
            lastMessageTime = System.currentTimeMillis()- 2,
            chatName = "Ismael Nunes Campos",
            isUnread = false,
            lastSeen = System.currentTimeMillis(),
            chatPhotoUrl = ""
        ),
//        GroupChat(
//            jid = "group@ismael",
//            chatName = "Familia",
//            members = LocalAccountsDataProvider.accounts,
//            lastMessage = " MESSAGE",
//            lastMessageTime = 454,
//            chatPhotoUrl = "TODO()",
//            isUnread = false
//        )
    )

}