package com.ismael.teams.ui.screens.chat

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ismael.teams.R
import com.ismael.teams.data.model.Chat
import com.ismael.teams.data.model.ChatType
import com.ismael.teams.data.model.Message
import com.ismael.teams.data.model.UserChat
import com.ismael.teams.data.remote.xmpp.XmppManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jivesoftware.smack.packet.Presence
import org.jxmpp.jid.impl.JidCreate
import java.util.UUID
import kotlin.collections.orEmpty
import kotlin.collections.toMutableMap

class ChatViewModel : ViewModel() {

    private val xmppManager = XmppManager

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    private val _messages = mutableMapOf<String, List<Message>>()


    fun loadMessagesForChat(chatId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val messages = _messages[chatId]
                _uiState.update {
                    it.copy(
                        messages = it.messages.toMutableMap().apply {
                            if (messages != null) {
                                put(chatId, messages)
                            }
                        },
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun updateCurrentSelectedChat(chat: Chat) {
        Log.i("Roster",chat.jid)
        getPresence(chat.jid)
        _uiState.update {
            it.copy(
                currentSelectedChat = chat
            )
        }
    }

    private fun addMessageToMap(
        map: MutableMap<String, List<Message>>,
        key: String,
        message: Message
    ) {
        Log.i("Mensagem adicionada", key)
        map.getOrPut(key) { emptyList() }
            .let { it + message }
            .also { map[key] = it }

    }


    fun sendMessage(chatId: String, message: Message) {
        viewModelScope.launch {
            try {
                addMessageToMap(_messages, chatId, message)

                viewModelScope.launch(Dispatchers.IO) {
                    val recipientJid = JidCreate.entityBareFrom(message.to)
                    Log.i("Mensagem", "Sending message to ${message.to}: $message")
                    xmppManager.sendMessage(recipientJid, message.text)

                }

                _uiState.update {
                    it.copy(
                        messages = it.messages.toMutableMap().apply {
                            val currentMessages = get(chatId).orEmpty()
                            put(chatId, currentMessages + message)
                        }
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun getPresence(jid: String){
       val presence =  xmppManager.getUserPresence(jid)
        Log.i("Roster",presence?.mode.toString())
        Log.i("Roster",presence?.type.toString())
        Log.i("Roster",presence?.status.toString())
        _uiState.update {
            it.copy(
                mode = presence?.mode,
                type = presence?.type.toString()
            )
        }
    }

    // States for Compose UI
    var messages = mutableListOf<String>()
        private set

    // Expor as mensagens recebidas para a UI
    val incomingMessages: StateFlow<List<org.jivesoftware.smack.packet.Message>> =
        xmppManager.receivedMessages


    fun setupMessageListener() {
        viewModelScope.launch(Dispatchers.IO) {
            XmppManager.addIncomingMessageListener()
            XmppManager.addMessageListener {
                observeIncomingMessages()
            }
        }
    }

    fun disconnect() {
        viewModelScope.launch(Dispatchers.IO) {
            xmppManager.disconnect()
        }
    }
    private var context: Context? = null

    fun setContext(context: Context) {
        this.context = context
    }


    fun observeIncomingMessages() {
        viewModelScope.launch {
            incomingMessages.collect { messages ->
                messages.lastOrNull()?.let { message ->
                    val key = message.from // Defina a chave, por exemplo, o remetente
                    notifyUser(message.from.toString(), message.body, context!!)
                    println("Recebida no dispatcher: " + message)
                    val mensagem = Message(
                        to = "ismael221@ismael",
                        key = UUID.randomUUID().toString(),
                        text = message.body,
                        senderId = "yasmin@ismael",
                        timestamp = System.currentTimeMillis(),
                    )
                    addMessageToMap(
                        map = _messages, // Seu mapa mutável
                        key = "yasmin@ismael",
                        message = mensagem
                    )
                    val chat = UserChat(
                        jid = "yasmin@ismael",
                        lastMessage = message.body,
                        lastMessageTime = mensagem.timestamp,
                        chatName = "yasmin rodrigues",
                        chatPhotoUrl = "",
                        isUnread = false,
                        chatType = ChatType.User,
                        lastSeen = System.currentTimeMillis()
                    )
                    _uiState.update {
                        it.copy(
                            currentSelectedChat = chat,
                            messages = it.messages.toMutableMap().apply {
                                val currentMessages = get(chat.jid).orEmpty()
                                put(chat.jid, currentMessages + mensagem)
                            }
                        )
                    }
                }
            }
        }
    }
    fun createNotificationChannel() {
        val context = this.context
        context?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "Messages"
                val descriptionText = "Notifications for new messages"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel("messages_channel", name, importance).apply {
                    description = descriptionText
                }
                val notificationManager = it.getSystemService(NotificationManager::class.java)
                notificationManager?.createNotificationChannel(channel)
            }
        }
    }


    private val REQUEST_CODE_PERMISSION = 100

    private fun notifyUser(from: String?, body: String, context: Context) {
        // Verificar se a permissão de notificações foi concedida (Android 13 e superior)
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED) {

            val notificationManager = NotificationManagerCompat.from(context)

            val notification = NotificationCompat.Builder(context, "messages_channel")
                .setSmallIcon(R.drawable.notifications_24px) // Ícone da notificação
                .setContentTitle("Nova mensagem de ${xmppManager.getUserName("yasmin@ismael")}")
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

            notificationManager.notify(System.currentTimeMillis().toInt(), notification)
        } else {
            // Solicitar permissão ao usuário, se necessário
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_CODE_PERMISSION
            )
        }
    }



}