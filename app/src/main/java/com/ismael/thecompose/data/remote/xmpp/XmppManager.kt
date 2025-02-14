package com.ismael.thecompose.data.remote.xmpp

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.jivesoftware.smack.AbstractXMPPConnection
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.ReconnectionManager
import org.jivesoftware.smack.SmackException
import org.jivesoftware.smack.XMPPException
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.packet.Presence
import org.jivesoftware.smack.packet.PresenceBuilder
import org.jivesoftware.smack.packet.Stanza
import org.jivesoftware.smack.packet.StanzaBuilder
import org.jivesoftware.smack.roster.PresenceEventListener
import org.jivesoftware.smack.roster.Roster
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.carbons.CarbonManager
import org.jivesoftware.smackx.carbons.packet.CarbonExtension
import org.jivesoftware.smackx.chatstates.ChatState
import org.jivesoftware.smackx.chatstates.ChatStateManager
import org.jivesoftware.smackx.filetransfer.FileTransferManager
import org.jivesoftware.smackx.iqlast.LastActivityManager
import org.jivesoftware.smackx.jingle.JingleManager
import org.jivesoftware.smackx.jingle_filetransfer.JingleFileTransferManager
import org.jivesoftware.smackx.ping.android.ServerPingWithAlarmManager
import org.jivesoftware.smackx.search.UserSearchManager
import org.jxmpp.jid.BareJid
import org.jxmpp.jid.EntityBareJid
import org.jxmpp.jid.FullJid
import org.jxmpp.jid.Jid
import org.jxmpp.jid.impl.JidCreate
import java.io.IOException
import java.util.Date
import java.util.concurrent.ConcurrentHashMap

object XmppManager {

    private val _receivedMessages =
        MutableStateFlow<List<Message>>(emptyList())
    val receivedMessages = _receivedMessages.asStateFlow()

    private val _incomingMessages = MutableStateFlow<List<String>>(emptyList())
    val incomingMessages = _incomingMessages.asStateFlow()

    private var carbonManager: CarbonManager? = null


    private var connection: AbstractXMPPConnection? = null
    private var chatManager: ChatManager? = null
    private val messageListeners = mutableListOf<(Message) -> Unit>()

    private val _presenceUpdates = MutableStateFlow<Pair<String, Presence?>>(Pair("", null))
    val presenceUpdates = _presenceUpdates.asStateFlow()

    private val lastActivityMap = ConcurrentHashMap<String, Long>()

    private val _chatStates = mutableMapOf<String, ChatState>()

    private val _chatStateUpdates = MutableStateFlow<Pair<String, ChatState?>>(Pair("", null))
    val chatStateUpdates = _chatStateUpdates.asStateFlow()

    fun addMessageListener(listener: (Message) -> Unit) {
        messageListeners.add(listener)
    }

    fun addIncomingMessageListener() {
        setupMessageListener()
    }

    private fun notifyMessageListeners(message: Message) {
        messageListeners.forEach { it(message) }
    }

    fun getUserName(jid: String): String {
        return try {
            val entityJid = JidCreate.entityBareFrom(jid)
            val roster = Roster.getInstanceFor(connection)
            val entry = roster?.getEntry(entityJid)
            entry?.name ?: jid // Use entry?.name to get the display name
        } catch (e: Exception) {
            Log.e("XmppManager", "Error getting user name for JID: $jid", e)
            jid
        }
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

            carbonManager = CarbonManager.getInstanceFor(connection)
            carbonManager?.isSupportedByServer
            if (carbonManager?.isSupportedByServer == true) {
                carbonManager?.enableCarbons()
                Log.i("CarbonManager", "Message Carbons enabled")
            } else {
                Log.i("CarbonManager", "Message Carbons not supported by the server")
            }

            val reconnectionManager = ReconnectionManager.getInstanceFor(connection)
            reconnectionManager.enableAutomaticReconnection()
            ReconnectionManager.setEnabledPerDefault(true)
            val ping = ServerPingWithAlarmManager.getInstanceFor(connection)

            ping.isEnabled = true

            chatManager = ChatManager.getInstanceFor(connection)
            val presence = PresenceBuilder
                .buildPresence()
            presence.setStatus("Teste")
            presence.setMode(Presence.Mode.dnd)

            setPresence(presence.build())

            setupMessageListener()
            rosterPresenceListener()

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

    fun getChatStateManager(): ChatStateManager {
        return ChatStateManager.getInstance(connection)
    }

    fun getFileTransferManager(): FileTransferManager {
        return FileTransferManager.getInstanceFor(connection)
    }

    fun getJigleManager(): JingleManager {
        return JingleManager.getInstanceFor(connection)
    }

    fun getJigleFileManager(): JingleFileTransferManager {
        return JingleFileTransferManager.getInstanceFor(connection)
    }

    fun getRoster(): Roster {
        return Roster.getInstanceFor(connection)
    }


    private fun updateLatestChatState(
        map: MutableMap<String, ChatState>,
        key: String,
        chatState: ChatState
    ) {
        Log.i("Add Chat State", "Adding state: $chatState for user: $key")
        map[key] = chatState
        val latestChatState: ChatState? = map[key]
        println("Latest chat state: $latestChatState")
    }


    private fun setupMessageListener() {
        connection?.addAsyncStanzaListener({ stanza ->
            if (stanza is Message) {
                val carbonCopy =
                    stanza.getExtension("urn:xmpp:carbons:2") as? CarbonExtension

                val actualMessage =
                    carbonCopy?.forwarded?.forwardedStanza as? Message
                        ?: stanza

                val from = actualMessage.from?.asBareJid()
                val body = actualMessage.body

                // Atualiza a lista de mensagens recebidas
                _receivedMessages.update { currentMessages ->
                    println("xmpp $currentMessages")
                    println("xmpp $actualMessage")
                    currentMessages + actualMessage // Adiciona a nova mensagem à lista existente
                }

                if (!body.isNullOrEmpty()) {
                    println("Mensagem recebida: De $from - $body")
                    Log.i("ListenerConexao", "Mensagem recebida de $from: $body")

                    // Atualiza o fluxo de mensagens
                    val newMessage = "${getUserName(from.toString())}: $body"
                    _incomingMessages.value += newMessage
                }
            }
        }, { stanza -> stanza is Message })
    }


    fun rosterPresenceListener() {
        val roster = Roster.getInstanceFor(connection)
        roster?.addPresenceEventListener(presenceListener)
    }


    fun disconnect() {
        connection?.disconnect()
        println("Desconectado do servidor XMPP!")
    }

    fun sendMessage(to: EntityBareJid, message: String) {
        try {
            //   val presence = Presence(Presence.Type.subscribe)
            val presence = StanzaBuilder.buildPresence().ofType(Presence.Type.subscribe).build()
            presence.to = to
            println(presence)
            connection?.sendStanza(presence)
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
            println("Presença definida: $presence")
        } catch (e: Exception) {
            println("Erro ao definir presença: ${e.message}")
        }
    }

    fun getUserPresence(jid: String): Presence? {
        return try {
            val entityJid = JidCreate.entityBareFrom(jid)
            val roster = Roster.getInstanceFor(connection)
            roster?.getPresence(entityJid)
        } catch (e: Exception) {
            Log.e("XmppManager", "Error getting presence for JID: $jid", e)
            null
        }
    }

    fun getUserActivity(jid: String): String {
        val activity = LastActivityManager.getInstanceFor(connection)
        val lastActivity = activity.getLastActivity(JidCreate.entityBareFrom(jid))

        return lastActivity.lastActivity.toString()
    }

    fun getSearchManager(): UserSearchManager {
        return UserSearchManager(connection)
    }

    private val presenceListener = object : PresenceEventListener {


        override fun presenceAvailable(address: FullJid?, availablePresence: Presence?) {
            Log.i(
                "XmppManager",
                "Presence available for $address: ${availablePresence?.type} - ${availablePresence?.status}"
            )
            _presenceUpdates.value = Pair(address.toString(), availablePresence)
            updateLastActivity(address.toString())
        }

        override fun presenceUnavailable(address: FullJid?, unavailablePresence: Presence?) {
            Log.i("XmppManager", "Presence unavailable for $address: ${unavailablePresence?.type}")
            _presenceUpdates.value = Pair(address.toString(), unavailablePresence)
            updateLastActivity(address.toString())
        }

        override fun presenceError(address: Jid?, errorPresence: Presence?) {
            Log.e("XmppManager", "Presence error for $address: ${errorPresence?.type}")
            _presenceUpdates.value = Pair(address.toString(), errorPresence)
            updateLastActivity(address.toString())
        }

        override fun presenceSubscribed(address: BareJid?, subscribedPresence: Presence?) {
            TODO("Not yet implemented")
        }

        override fun presenceUnsubscribed(address: BareJid?, unsubscribedPresence: Presence?) {
            TODO("Not yet implemented")
        }

    }

    private fun updateLastActivity(jid: String) {
        lastActivityMap[jid] = Date().time
    }

    fun getLastActivity(jid: String): Long? {
        return lastActivityMap[jid]
    }

    fun sendChatState(to: String, chatState: ChatState) {
        try {
            val chat = chatManager?.chatWith(JidCreate.entityBareFrom(to))
            ChatStateManager.getInstance(connection).setCurrentState(chatState, chat)
            println("Chat state $chatState enviado para $to")
        } catch (e: Exception) {
            println("Erro ao enviar chat state: ${e.message}")
        }
    }

    fun updateStatus(status: String, lastPresence: Presence.Mode) {
        val presence = PresenceBuilder.buildPresence()
        presence.setStatus(status)
        presence.setMode(lastPresence)
        try {
            connection?.sendStanza(presence.build())
        } catch (e: Exception) {
            println("Erro ao atualizar status: ${e.message}")
        }
    }


}