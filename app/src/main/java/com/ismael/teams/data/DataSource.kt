package com.ismael.teams.data

import androidx.compose.ui.util.trace
import com.ismael.teams.R
import com.ismael.teams.model.ChatPreview
import com.ismael.teams.model.Message

class DataSource {
    fun loadChats(): List<ChatPreview> {
        return listOf(
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1, isUnread = true),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = true),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = true),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = true),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = true),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false),
            ChatPreview(R.string.user_name, R.drawable.perfil, R.string.last_message_chat_1,isUnread = false)
        )

    }

    fun loadMessages(): List<Message>{
        return listOf(
            Message(content = "Hello, how are you?", from = "ismael221@ismael"),
            Message(content = "I'm doing well, thanks!", from = "yasmin@ismael"),
            Message(content = "Ismael Nunes Campos, sou nascido e criado aqui no Brasil irmão", from = "ismael221@ismael"),
            Message(content = "Oi amor", from = "yasmin@ismael"),
            Message(content = "Ismael Nunes Campos, sou nascido e criado aqui no Brasil irmão asldkfjalsdfjçalsdfjasldf asdkfjasldfj asdf alksdfçlasdfjk  askdfçalsdfjçalskdfj asdflkjasdçlfkjasdf asd,fmaç.sdfmasdf asdfasdjfkasjd fasd fasdfjasd flksadjfçlkasdjf çalsdf alskdjfçalskdfj asdfkj ", from = "ismael221@ismael"),
            Message(content = "kkk", from = "yasmin@ismael"),
            Message(content = "Desse jeito mesmo", from = "yasmin@ismael"),
            Message(content = "asdflkjasdçlfkjasdfçlkjasdfçlkjasdçflkjasdfçlkjasdfçlkjasdfçlkajsdfçlkasjd asldkfjalksçdfj aslçdfkj asdlfkjasçldfkj asoiuewproiuwqeproiwque rpoqwieurpoqwieurpoi poiweurpoqwieurpwiqoer nkqw ernqwelkr sad´ffdsoiafdso akdsfasjdfçlasdf poiuasdiasufdpioads fkajsdfçlkasjdf poi", from = "yasmin@ismael"),
            Message(content = "asdflkjasdçlfkjasdfçlkjasdfçlkjasdçflkjasdfçlkjasdfçlkjasdfçlkajsdfçlkasjd asldkfjalksçdfj aslçdfkj asdlfkjasçldfkj asoiuewproiuwqeproiwque rpoqwieurpoqwieurpoi poiweurpoqwieurpwiqoer nkqw ernqwelkr sad´ffdsoiafdso akdsfasjdfçlasdf poiuasdiasufdpioads fkajsdfçlkasjdf poi dfasdfasdfasdf dsfsadfsadfsadf dsfasdfsad ", from = "ismael221@ismael"),
            Message(content = "Então é isso ai", from = "yasmin@ismael"),
            Message(content = "Até mais tarde te vejo lá", from = "yasmin@ismael"),
            Message(content = "Tchau ate mais tarde", from = "ismael221@ismael"),
            Message(content = "Hello, how are you?", from = "ismael221@ismael"),
            Message(content = "I'm doing well, thanks!", from = "yasmin@ismael"),
            Message(content = "Ismael Nunes Campos, sou nascido e criado aqui no Brasil irmão", from = "ismael221@ismael"),
            Message(content = "Oi amor", from = "yasmin@ismael"),
            Message(content = "Ismael Nunes Campos, sou nascido e criado aqui no Brasil irmão asldkfjalsdfjçalsdfjasldf asdkfjasldfj asdf alksdfçlasdfjk  askdfçalsdfjçalskdfj asdflkjasdçlfkjasdf asd,fmaç.sdfmasdf asdfasdjfkasjd fasd fasdfjasd flksadjfçlkasdjf çalsdf alskdjfçalskdfj asdfkj ", from = "ismael221@ismael"),
            Message(content = "kkk", from = "yasmin@ismael"),
            Message(content = "Desse jeito mesmo", from = "yasmin@ismael"),
            Message(content = "asdflkjasdçlfkjasdfçlkjasdfçlkjasdçflkjasdfçlkjasdfçlkjasdfçlkajsdfçlkasjd asldkfjalksçdfj aslçdfkj asdlfkjasçldfkj asoiuewproiuwqeproiwque rpoqwieurpoqwieurpoi poiweurpoqwieurpwiqoer nkqw ernqwelkr sad´ffdsoiafdso akdsfasjdfçlasdf poiuasdiasufdpioads fkajsdfçlkasjdf poi", from = "yasmin@ismael"),
            Message(content = "asdflkjasdçlfkjasdfçlkjasdfçlkjasdçflkjasdfçlkjasdfçlkjasdfçlkajsdfçlkasjd asldkfjalksçdfj aslçdfkj asdlfkjasçldfkj asoiuewproiuwqeproiwque rpoqwieurpoqwieurpoi poiweurpoqwieurpwiqoer nkqw ernqwelkr sad´ffdsoiafdso akdsfasjdfçlasdf poiuasdiasufdpioads fkajsdfçlkasjdf poi dfasdfasdfasdf dsfsadfsadfsadf dsfasdfsad ", from = "ismael221@ismael"),
            Message(content = "Então é isso ai", from = "yasmin@ismael"),
            Message(content = "Até mais tarde te vejo lá", from = "yasmin@ismael"),
            Message(content = "Tchau ate mais tarde", from = "ismael221@ismael"),

        )
    }
}