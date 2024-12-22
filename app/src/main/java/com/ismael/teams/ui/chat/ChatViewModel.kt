package com.ismael.teams.ui.chat

import android.util.Log
import androidx.activity.result.launch
import androidx.compose.animation.core.copy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ismael.teams.model.Chat
import com.ismael.teams.model.ChatType
import com.ismael.teams.model.InMemoryMessageDao
import com.ismael.teams.model.Message
import com.ismael.teams.model.MessageDao
import com.ismael.teams.model.UserChat
import com.ismael.teams.xmpp.XmppManager
import com.ismael.teams.xmpp.XmppViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jxmpp.jid.impl.JidCreate
import java.util.UUID
import kotlin.collections.get
import kotlin.collections.orEmpty
import kotlin.collections.toMutableMap

class ChatViewModel : ViewModel() {

    private val xmppManager = XmppManager()

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()
    private val messageDao: MessageDao = InMemoryMessageDao() // Use the in-memory implementation

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

    // States for Compose UI
    var messages = mutableListOf<String>()
        private set

    // Expor as mensagens recebidas para a UI
    val incomingMessages: StateFlow<List<org.jivesoftware.smack.packet.Message>> = xmppManager.receivedMessages


    fun connect(server: String, username: String, password: String) {
        xmppManager.connect(server, username, password)
        viewModelScope.launch(Dispatchers.IO) {
            xmppManager.addMessageListener {
                observeIncomingMessages()
            }
        }
    }

    fun disconnect() {
        viewModelScope.launch(Dispatchers.IO) {
            xmppManager.disconnect()
        }
    }


    private fun observeIncomingMessages() {
        viewModelScope.launch {
            incomingMessages.collect { messages ->
                messages.lastOrNull()?.let { message ->
                    val key = message.from // Defina a chave, por exemplo, o remetente
                    println("Recebida no dispatcher: "+message)
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


    init {

        connect("ismael", "ismael221", "Ismuca18@")

        observeIncomingMessages()

    }


}