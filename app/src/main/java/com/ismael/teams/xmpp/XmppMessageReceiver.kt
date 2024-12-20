package com.ismael.teams.xmpp

import android.util.Log
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.IncomingChatMessageListener
import org.jivesoftware.smack.packet.Message
import org.jxmpp.jid.EntityBareJid

class XmppMessageReceiver: IncomingChatMessageListener {
    override fun newIncomingMessage(from: EntityBareJid?, message: Message?, chat: Chat?) {
        // Handle incoming messages here
        val newMessage = "${from?.asBareJid()} - ${chat} : ${message?.body}"

        println("Mensagem recebida de ${from?.asBareJid()}: ${message?.body}")

        Log.i("MensagemRecebidaPeloListener",newMessage)

    }
}