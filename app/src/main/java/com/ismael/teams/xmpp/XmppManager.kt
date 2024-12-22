package com.ismael.teams.xmpp

import android.util.Log
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.update
import org.jivesoftware.smack.AbstractXMPPConnection
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.ReconnectionManager
import org.jivesoftware.smack.SmackException
import org.jivesoftware.smack.XMPPException
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.packet.Presence
import org.jivesoftware.smack.packet.Stanza
import org.jivesoftware.smack.roster.Roster
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jxmpp.jid.EntityBareJid
import java.io.IOException

object XmppManager {

    private val _receivedMessages = MutableStateFlow<List<org.jivesoftware.smack.packet.Message>>(emptyList())
    val receivedMessages = _receivedMessages.asStateFlow()

    private val _incomingMessages = MutableStateFlow<List<String>>(emptyList())
    val incomingMessages = _incomingMessages.asStateFlow()

    private var connection: AbstractXMPPConnection? = null
    private var chatManager: ChatManager? = null
    private val messageListeners = mutableListOf<(org.jivesoftware.smack.packet.Message) -> Unit>()

    fun addMessageListener(listener: (org.jivesoftware.smack.packet.Message) -> Unit) {
        messageListeners.add(listener)
    }

    fun addIncomingMessageListener() {
        setupMessageListener()
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

            ReconnectionManager.getInstanceFor(connection).enableAutomaticReconnection()

            chatManager = ChatManager.getInstanceFor(connection)
            val presence = Presence(Presence.Type.available)
            setPresence(presence)

            setupMessageListener()

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

    fun setupMessageListener() {
        connection?.addAsyncStanzaListener({ stanza ->
            if (stanza is org.jivesoftware.smack.packet.Message) {
                val from = stanza.from?.asBareJid()
                val body = stanza.body
                _receivedMessages.update { currentMessages ->
                    println("xmpp "+currentMessages)
                    currentMessages + stanza // Adiciona a nova mensagem à lista existente
                }

                if (!body.isNullOrEmpty()) {
                    println("Mensagem recebida diretamente da conexão: De $from - $body")
                    Log.i("ListenerConexao", "Mensagem recebida de $from: $body")

                    // Atualiza o fluxo de mensagens
                    val newMessage = "${getUserName(from.toString())}: $body"
                    _incomingMessages.value += newMessage
                }
            }
        }, { stanza -> stanza is org.jivesoftware.smack.packet.Message })
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