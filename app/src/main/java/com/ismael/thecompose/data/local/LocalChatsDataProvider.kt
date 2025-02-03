package com.ismael.thecompose.data.local

import com.ismael.thecompose.data.model.UserChat
import kotlin.random.Random

object LocalChatsDataProvider {

    var chats = mutableListOf<UserChat>(
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

fun generateRandomUserChats(): List<UserChat> {
    val names = listOf("Ismael Nunes Campos", "João Silva", "Maria Oliveira", "Pedro Souza", "Ana Lima", "Lucas Costa", "Fernanda Alves", "Bruno Pereira", "Juliana Mendes", "Ricardo Santos", "Beatriz Rocha", "Diego Martins", "Amanda Fernandes", "Gustavo Carvalho", "Camila Borges")
    val messages = listOf("Olá", "Oi", "Bom dia", "Como vai?", "Tudo certo?", "Até logo", "Obrigado!", "De nada", "Sim", "Não", "Talvez", "Vamos sim", "Depois falamos", "Pode ser", "Entendi")

    return List(15) { index ->
        val timestamp = System.currentTimeMillis() - Random.nextLong(1000, 1000000)
        UserChat(
            jid = "user$index@randomdomain.com",
            lastMessage = messages.random(),
            lastMessageTime = timestamp,
            chatName = names[index],
            isUnread = Random.nextBoolean(),
            lastSeen = timestamp - Random.nextLong(1000, 50000),
            chatPhotoUrl = "https://randomuser.me/api/portraits/thumb/men/${index}.jpg"
        )
    }
}