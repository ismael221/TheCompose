package com.ismael.teams.xmpp

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.jivesoftware.smack.AbstractXMPPConnection
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.SmackException
import org.jivesoftware.smack.XMPPException
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.packet.Stanza
import org.jivesoftware.smack.roster.Roster
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jxmpp.jid.EntityBareJid
import java.io.IOException

class XmppManager {
    private val _incomingMessages = MutableStateFlow<List<String>>(emptyList())
    val incomingMessages = _incomingMessages.asStateFlow()

    private var connection: AbstractXMPPConnection? = null
    private var chatManager: ChatManager? = null
    private val messageListeners = mutableListOf<(org.jivesoftware.smack.packet.Message) -> Unit>()

    fun addMessageListener(listener: (org.jivesoftware.smack.packet.Message) -> Unit) {
        messageListeners.add(listener)
    }

    private fun notifyMessageListeners(message: org.jivesoftware.smack.packet.Message) {
        messageListeners.forEach { it(message) }
    }

    fun getUserName(jid: String): String {
        // Obtém o Roster da conexão
        val roster = Roster.getInstanceFor(connection)

        // Converte o JID para um BareJid
        val entityJid = org.jxmpp.jid.impl.JidCreate.entityBareFrom(jid)

        // Pega o nome associado ao JID
        val entry = roster.getEntry(entityJid)
        return entry?.name ?: jid // Se não houver nome, retorna o JID
    }

    fun connect(server: String, username: String, password: String) {
        try {
            val config = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword(username, password)
                .setXmppDomain(server)
                .setHost("192.168.100.12")
                .setPort(5222)
                .addEnabledSaslMechanism("PLAIN")
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .build()

            connection = XMPPTCPConnection(config)
            connection?.connect()
            connection?.login()


            chatManager = ChatManager.getInstanceFor(connection)

            connection?.addAsyncStanzaListener({ stanza ->
                if (stanza is org.jivesoftware.smack.packet.Message) {
                    val from = stanza.from?.asBareJid()
                    val body = stanza.body


                    if (!body.isNullOrEmpty()) {
                        println("Mensagem recebida diretamente da conexão: De $from - $body")
                        Log.i("ListenerConexao", "Mensagem recebida de $from: $body")

                        // Atualiza o fluxo de mensagens
                        val newMessage = "${getUserName(from.toString())}: $body"
                        _incomingMessages.value = _incomingMessages.value + newMessage
                    }
                }
            }, { stanza -> stanza is org.jivesoftware.smack.packet.Message })



            println("Conectado ao servidor XMPP!")
        } catch (e: SmackException) {
            println("Erro de Smack: ${e.message}")
        } catch (e: IOException) {
            println("Erro de IO: ${e.message}")
        } catch (e: XMPPException) {
            println("Erro de XMPP: ${e.message}")
        } catch (e: InterruptedException) {
            println("Erro de Interrupção: ${e.message}")
        }
    }

    fun disconnect() {
        connection?.disconnect()
        println("Desconectado do servidor XMPP!")
    }

    fun sendMessage(to: EntityBareJid, message: String) {
        try {
            val chat = chatManager?.chatWith(to)
            chat?.send(message)
            println("Mensagem enviada para $to: $message")
        } catch (e: Exception) {
            println("Erro ao enviar mensagem: ${e.message}")
        }
    }

    fun setPresence(presence: Stanza) {
        try {
            connection?.sendStanza(presence)
        } catch (e: Exception) {
            println("Erro ao definir presença: ${e.message}")
        }
    }
}