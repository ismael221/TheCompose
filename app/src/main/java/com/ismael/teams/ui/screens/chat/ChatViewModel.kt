package com.ismael.teams.ui.screens.chat

import android.Manifest
import android.annotation.SuppressLint
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
import com.ismael.teams.data.local.LocalAccountsDataProvider
import com.ismael.teams.data.local.LocalChatsDataProvider
import com.ismael.teams.data.local.LocalLoggedAccounts
import com.ismael.teams.data.model.Chat
import com.ismael.teams.data.model.ChatType
import com.ismael.teams.data.model.Message
import com.ismael.teams.data.model.User
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
import org.jivesoftware.smack.packet.PresenceBuilder
import org.jivesoftware.smackx.chatstates.ChatState
import org.jxmpp.jid.impl.JidCreate
import java.time.Instant

import java.util.UUID


class ChatViewModel : ViewModel() {

    private val xmppManager = XmppManager

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    private val _messages = mutableMapOf<String, List<Message>>()

    private val _presenceUpdates = MutableStateFlow<Pair<String, Presence?>>(Pair("", null))
    val presenceUpdates = _presenceUpdates.asStateFlow()

    private val currentLoggedInUser = LocalLoggedAccounts.account

    private fun initializeUiState() {
        val chats = LocalChatsDataProvider.chats.sortedByDescending { it.lastMessageTime }
        Log.i("LoadedMessages", chats.toString())

        _uiState.value =
            ChatUiState(
                chats = chats,
                currentLoggedInUser = currentLoggedInUser,
                unReadMessages = LocalChatsDataProvider.chats.filter { it.isUnread }.size,
                lastSelectedChat = chats[0]
            )

    }


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
                        chatState = _chatStatesFlow.value.get(key = chatId),
                        isLoading = false,
                        lastMessage = messages?.lastOrNull(),
                        lastMessageTimestamp = messages?.lastOrNull()?.timestamp
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun updateCurrentSelectedChat(chat: Chat) {
        Log.i("Roster", chat.jid)
        getPresence(chat.jid)
        val itemToUpdate = LocalChatsDataProvider.chats.find { it.jid == chat.jid }
        itemToUpdate?.isUnread = false
        val index = LocalChatsDataProvider.chats.indexOf(itemToUpdate)
        if (itemToUpdate != null) {
            LocalChatsDataProvider.chats[index] = itemToUpdate
            _uiState.update { it ->
                it.copy(
                    chats = LocalChatsDataProvider.chats.sortedByDescending { it.lastMessageTime }
                )
            }
        }
        _uiState.update {
            it.copy(
                chatState = _chatStatesFlow.value.get(key = chat.jid),
                unReadMessages = LocalChatsDataProvider.chats.filter { chat -> chat.isUnread }.size,
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

    private fun removeAfterSlash(input: String): String {
        return input.substringBefore("/")
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
                val itemToUpdate = LocalChatsDataProvider.chats.find { it.jid == chatId }
                itemToUpdate?.lastMessage = "You: ${message.text}"
                itemToUpdate?.lastMessageTime = System.currentTimeMillis()
                itemToUpdate?.isUnread = false
                val index = LocalChatsDataProvider.chats.indexOf(itemToUpdate)
                if (itemToUpdate != null) {
                    LocalChatsDataProvider.chats[index] = itemToUpdate
                    _uiState.update { it ->
                        it.copy(
                            chats = LocalChatsDataProvider.chats.sortedByDescending { it.lastMessageTime }
                        )
                    }
                }


                loadMessagesForChat(chatId)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }


    fun getPresence(jid: String) {
        val presence = xmppManager.getUserPresence(jid)
        Log.i("Roster", presence?.mode.toString())
        Log.i("Roster", presence?.status.toString())
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
                    if (message.body != null) {
                        var key: String? = null

                        println("Recebida no dispatcher: " + message)
                        println("Body: " + message.body)
                        val mensagem = Message(
                            to = removeAfterSlash(message.to.toString()),
                            key = UUID.randomUUID().toString(),
                            text = message.body,
                            senderId = removeAfterSlash(message.from.toString()),
                            timestamp = System.currentTimeMillis(),
                        )
                        if (currentLoggedInUser.jid == removeAfterSlash(message.from.toString())) {
                            key = message.to.toString()
                        } else {
                            val existingChat =
                                LocalChatsDataProvider.chats.find {
                                    it.jid == removeAfterSlash(
                                        message.from.toString()
                                    )
                                }
                            if (existingChat == null) {
                                println("Usuário vazio")
                                val newChat = UserChat(
                                    jid = removeAfterSlash(message.from.toString()),
                                    lastMessage = message.body.toString(),
                                    lastMessageTime = System.currentTimeMillis(),
                                    chatName = LocalAccountsDataProvider.accounts.find {
                                        it.jid == removeAfterSlash(
                                            message.from.toString()
                                        )
                                    }?.displayName.toString(),
                                    chatPhotoUrl = "",
                                    isUnread = true,
                                    chatType = ChatType.User,
                                    lastSeen = 0
                                )
                                LocalChatsDataProvider.chats.add(newChat)
                                _uiState.update {
                                    it.copy(
                                        unReadMessages = LocalChatsDataProvider.chats.filter { chat -> chat.isUnread }.size,
                                        chats = LocalChatsDataProvider.chats.sortedByDescending { it.lastMessageTime }
                                    )
                                }
                                println("Chat adicionado")
                            } else {
                                val itemToUpdate = LocalChatsDataProvider.chats.find {
                                    it.jid == removeAfterSlash(message.from.toString())
                                }
                                itemToUpdate?.lastMessage = message.body
                                itemToUpdate?.lastMessageTime = System.currentTimeMillis()
                                itemToUpdate?.isUnread = true
                                val index = LocalChatsDataProvider.chats.indexOf(itemToUpdate)
                                if (itemToUpdate != null) {
                                    LocalChatsDataProvider.chats[index] = itemToUpdate
                                    _uiState.update { it ->
                                        it.copy(
                                            chats = LocalChatsDataProvider.chats.sortedByDescending { it.lastMessageTime }
                                        )
                                    }
                                }
                            }
                            notifyUser(message.from.toString(), message.body, context!!)
                            key = message.from.toString()
                        }

                        addMessageToMap(
                            map = _messages, // Seu mapa mutável
                            key = removeAfterSlash(key),
                            message = mensagem
                        )
                        if ((_uiState.value.currentSelectedChat?.jid == removeAfterSlash(message.from.toString())) || (_uiState.value.currentSelectedChat?.jid == removeAfterSlash(
                                message.to.toString()
                            ))
                        ) {
                            _uiState.update {
                                it.copy(
                                    currentSelectedChat = _uiState.value.currentSelectedChat,
                                    chatState = _chatStatesFlow.value.get(key = key),
                                    unReadMessages = LocalChatsDataProvider.chats.filter { chat -> chat.isUnread }.size,
                                    messages = it.messages.toMutableMap().apply {
                                        val currentMessages =
                                            get(_uiState.value.currentSelectedChat?.jid).orEmpty()
                                        _uiState.value.currentSelectedChat?.jid?.let { it1 ->
                                            put(
                                                it1,
                                                currentMessages + mensagem
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    } else {
                        println("Received message with null body: $message")
                    }
                }
            }
        }
    }

    private fun observePresenceUpdates() {
        XmppManager.presenceUpdates.onEach { presenceUpdate ->
            _presenceUpdates.value = presenceUpdate
        }.launchIn(viewModelScope)
    }


    private fun createNotificationChannel() {
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
        createNotificationChannel()
        // Verificar versão do Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 ou superior: Verificar permissão de notificações
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                showNotification(from, body, context)
            } else {
                // Solicitar permissão ao usuário
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_CODE_PERMISSION
                )
            }
        } else {
            // Android 12 ou inferior: Não é necessário verificar permissões
            showNotification(from, body, context)
        }
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(from: String?, body: String, context: Context) {
        val notificationManager = NotificationManagerCompat.from(context)

        val notification = NotificationCompat.Builder(context, "messages_channel")
            .setSmallIcon(R.drawable.notifications_24px) // Ícone da notificação
            .setContentTitle("Nova mensagem de ${removeAfterSlash(xmppManager.getUserName(from.toString()))}")
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private val _chatStatesFlow = MutableStateFlow<Map<String, ChatState>>(emptyMap())
    private val chatStatesFlow: StateFlow<Map<String, ChatState>> = _chatStatesFlow


    private fun observeChatStates() {
        val chatStateManager = XmppManager.getChatStateManager()

        chatStateManager.addChatStateListener { chat, state, message ->
            updateChatState(
                chat.xmppAddressOfChatPartner.toString(),
                state
            )
            println("Chat state changed: Chat: ${chat.xmppAddressOfChatPartner}, State: $state, Message: $message")
        }
    }

    private fun updateChatState(user: String, state: ChatState) {
        val updatedMap = _chatStatesFlow.value.toMutableMap()
        updatedMap[user] = state
        _chatStatesFlow.value = updatedMap

        if (
            _uiState.value.currentSelectedChat?.jid == user
        ) {
            _uiState.update {
                it.copy(
                    chatState = _chatStatesFlow.value.get(key = user),
                    currentSelectedChat = _uiState.value.currentSelectedChat
                )
            }
        }

    }


    init {
        observeChatStates()
        initializeUiState()
    }


}