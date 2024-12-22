package com.ismael.teams.data

import androidx.compose.ui.util.trace
import com.ismael.teams.R
import com.ismael.teams.model.Chat
import com.ismael.teams.model.ChatPreview
import com.ismael.teams.model.Message
import com.ismael.teams.model.UserChat

class DataSource {
    fun loadChats(): List<Chat> {
        return listOf(
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
                lastMessageTime = System.currentTimeMillis(),
                chatName = "Debora Nunes",
                isUnread = false,
                lastSeen = System.currentTimeMillis(),
                chatPhotoUrl = ""
            )

        )

    }

//    fun loadMessages(): List<Message>{
//        return listOf(
//            Message(content = "Hello, how are you?", from = "ismael221@ismael"),
//            Message(content = "I'm doing well, thanks!", from = "yasmin@ismael"),
//            Message(content = "Ismael Nunes Campos, sou nascido e criado aqui no Brasil irmão", from = "ismael221@ismael"),
//            Message(content = "Oi amor", from = "yasmin@ismael"),
//            Message(content = "Ismael Nunes Campos, sou nascido e criado aqui no Brasil irmão asldkfjalsdfjçalsdfjasldf asdkfjasldfj asdf alksdfçlasdfjk  askdfçalsdfjçalskdfj asdflkjasdçlfkjasdf asd,fmaç.sdfmasdf asdfasdjfkasjd fasd fasdfjasd flksadjfçlkasdjf çalsdf alskdjfçalskdfj asdfkj ", from = "ismael221@ismael"),
//            Message(content = "kkk", from = "yasmin@ismael"),
//            Message(content = "Desse jeito mesmo", from = "yasmin@ismael"),
//            Message(content = "asdflkjasdçlfkjasdfçlkjasdfçlkjasdçflkjasdfçlkjasdfçlkjasdfçlkajsdfçlkasjd asldkfjalksçdfj aslçdfkj asdlfkjasçldfkj asoiuewproiuwqeproiwque rpoqwieurpoqwieurpoi poiweurpoqwieurpwiqoer nkqw ernqwelkr sad´ffdsoiafdso akdsfasjdfçlasdf poiuasdiasufdpioads fkajsdfçlkasjdf poi", from = "yasmin@ismael"),
//            Message(content = "asdflkjasdçlfkjasdfçlkjasdfçlkjasdçflkjasdfçlkjasdfçlkjasdfçlkajsdfçlkasjd asldkfjalksçdfj aslçdfkj asdlfkjasçldfkj asoiuewproiuwqeproiwque rpoqwieurpoqwieurpoi poiweurpoqwieurpwiqoer nkqw ernqwelkr sad´ffdsoiafdso akdsfasjdfçlasdf poiuasdiasufdpioads fkajsdfçlkasjdf poi dfasdfasdfasdf dsfsadfsadfsadf dsfasdfsad ", from = "ismael221@ismael"),
//            Message(content = "Então é isso ai", from = "yasmin@ismael"),
//            Message(content = "Até mais tarde te vejo lá", from = "yasmin@ismael"),
//            Message(content = "Tchau ate mais tarde", from = "ismael221@ismael"),
//            Message(content = "Hello, how are you?", from = "ismael221@ismael"),
//            Message(content = "I'm doing well, thanks!", from = "yasmin@ismael"),
//            Message(content = "Ismael Nunes Campos, sou nascido e criado aqui no Brasil irmão", from = "ismael221@ismael"),
//            Message(content = "Oi amor", from = "yasmin@ismael"),
//            Message(content = "Ismael Nunes Campos, sou nascido e criado aqui no Brasil irmão asldkfjalsdfjçalsdfjasldf asdkfjasldfj asdf alksdfçlasdfjk  askdfçalsdfjçalskdfj asdflkjasdçlfkjasdf asd,fmaç.sdfmasdf asdfasdjfkasjd fasd fasdfjasd flksadjfçlkasdjf çalsdf alskdjfçalskdfj asdfkj ", from = "ismael221@ismael"),
//            Message(content = "kkk", from = "yasmin@ismael"),
//            Message(content = "Desse jeito mesmo", from = "yasmin@ismael"),
//            Message(content = "asdflkjasdçlfkjasdfçlkjasdfçlkjasdçflkjasdfçlkjasdfçlkjasdfçlkajsdfçlkasjd asldkfjalksçdfj aslçdfkj asdlfkjasçldfkj asoiuewproiuwqeproiwque rpoqwieurpoqwieurpoi poiweurpoqwieurpwiqoer nkqw ernqwelkr sad´ffdsoiafdso akdsfasjdfçlasdf poiuasdiasufdpioads fkajsdfçlkasjdf poi", from = "yasmin@ismael"),
//            Message(content = "asdflkjasdçlfkjasdfçlkjasdfçlkjasdçflkjasdfçlkjasdfçlkjasdfçlkajsdfçlkasjd asldkfjalksçdfj aslçdfkj asdlfkjasçldfkj asoiuewproiuwqeproiwque rpoqwieurpoqwieurpoi poiweurpoqwieurpwiqoer nkqw ernqwelkr sad´ffdsoiafdso akdsfasjdfçlasdf poiuasdiasufdpioads fkajsdfçlkasjdf poi dfasdfasdfasdf dsfsadfsadfsadf dsfasdfsad ", from = "ismael221@ismael"),
//            Message(content = "Então é isso ai", from = "yasmin@ismael"),
//            Message(content = "Até mais tarde te vejo lá", from = "yasmin@ismael"),
//            Message(content = "Tchau ate mais tarde", from = "ismael221@ismael"),
//
//        )
//    }
}