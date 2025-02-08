package com.ismael.thecompose.ui.screens.chat

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ismael.thecompose.data.local.LocalAccountsDataProvider
import com.ismael.thecompose.data.local.LocalChatsDataProvider
import com.ismael.thecompose.data.local.LocalLoggedAccounts
import com.ismael.thecompose.data.local.LocalLoggedAccounts._messages
import com.ismael.thecompose.data.model.Chat
import com.ismael.thecompose.data.model.ChatType
import com.ismael.thecompose.data.model.Message
import com.ismael.thecompose.data.model.UserChat
import com.ismael.thecompose.data.remote.xmpp.XmppManager
import com.ismael.thecompose.data.repository.NotificationRepository
import com.ismael.thecompose.ui.utils.MessageType
import com.ismael.thecompose.ui.utils.addMessageToMap
import com.ismael.thecompose.ui.utils.media.getFileFromUri
import com.ismael.thecompose.ui.utils.removeAfterSlash
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jivesoftware.smack.packet.Presence
import org.jivesoftware.smackx.chatstates.ChatState
import org.jxmpp.jid.impl.JidCreate
import java.io.File
import java.util.UUID


class ChatViewModel : ViewModel() {

    private val xmppManager = XmppManager

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    private val _presenceUpdates = MutableStateFlow<Pair<String, Presence?>>(Pair("", null))
    val presenceUpdates = _presenceUpdates.asStateFlow()

    private val currentLoggedInUser = LocalLoggedAccounts.account

    var messages = mutableListOf<String>()
        private set

    private val incomingMessages: StateFlow<List<org.jivesoftware.smack.packet.Message>> =
        xmppManager.receivedMessages

    private val _chatStatesFlow = MutableStateFlow<Map<String, ChatState>>(emptyMap())
    private val chatStatesFlow: StateFlow<Map<String, ChatState>> = _chatStatesFlow

    private val notificationRepository = NotificationRepository()

    private var context: Context? = null

    fun setContext(context: Context) {
        this.context = context
    }


    private fun initializeUiState() {
        val chats = LocalChatsDataProvider.chats.sortedByDescending { it.lastMessageTime }
        Log.i("LoadedMessages", chats.toString())

        _uiState.value =
            ChatUiState(
                chats = chats,
                currentLoggedInUser = currentLoggedInUser,
                unReadMessages = LocalChatsDataProvider.chats.filter { it.isUnread }.size,
                lastSelectedChat = if (chats.isNotEmpty()) chats[0] else null
            )

    }


    fun loadMessagesForChat(chatId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val messages = _messages[chatId]
                _uiState.update {
                    it.copy(
                        currentSelectedChat = LocalChatsDataProvider.chats.find { chat -> chat.jid == chatId },
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


    fun sendMessage(chatId: String, message: Message) {
        viewModelScope.launch {
            try {
                addMessageToMap(_messages, chatId, message)

                viewModelScope.launch(Dispatchers.IO) {
                    val recipientJid = JidCreate.entityBareFrom(message.to)
                    Log.i("Mensagem", "Sending message to ${message.to}: $message")
                    xmppManager.sendMessage(recipientJid, message.content)

                }
                val itemToUpdate = LocalChatsDataProvider.chats.find { it.jid == chatId }
                itemToUpdate?.lastMessage = "You: ${message.content}"
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


    fun observeIncomingMessages() {
        viewModelScope.launch {
            incomingMessages.collect { messages ->
                messages.lastOrNull()?.let { message ->
                    if (message.body != null) {
                        processIncomingMessage(message)
                    } else {
                        println("Received message with null body: $message")
                    }
                }
            }
        }
    }

    private fun processIncomingMessage(message: org.jivesoftware.smack.packet.Message) {
        println("Recebida no dispatcher: $message")
        println("Body: " + message.body)
        var existingChat: Chat? = null
        val senderJid = removeAfterSlash(message.from.toString())
        if (senderJid == currentLoggedInUser.jid) {
            existingChat =
                LocalChatsDataProvider.chats.find { it.jid == removeAfterSlash(message.to.toString()) }
        } else {
            existingChat = LocalChatsDataProvider.chats.find { it.jid == senderJid }
        }

        if (currentLoggedInUser.jid == senderJid) {
            handleOutgoingMessage(existingChat, message)
        } else {
            handleIncomingMessage(existingChat, message)
        }
    }

    private fun createMessage(
        message: org.jivesoftware.smack.packet.Message,
        senderJid: String,
        receiverJid: String
    ): Message {
        return Message(
            to = removeAfterSlash(receiverJid),
            key = UUID.randomUUID().toString(),
            content = message.body,
            senderId = removeAfterSlash(senderJid),
            type = MessageType.Text,
            timestamp = System.currentTimeMillis(),
        )
    }

    private fun handleOutgoingMessage(
        existingChat: UserChat?,
        mensagem: org.jivesoftware.smack.packet.Message
    ) {

        if (existingChat == null) {
            createNewChat(mensagem, removeAfterSlash(mensagem.to.toString()))
        }
        updateUIState(mensagem)
    }

    private fun handleIncomingMessage(
        existingChat: UserChat?,
        mensagem: org.jivesoftware.smack.packet.Message
    ) {
        val jid =
            if (currentLoggedInUser.jid == mensagem.from.toString()) mensagem.to.toString() else mensagem.from.toString()

        if (existingChat == null) {
            createNewChat(mensagem, jid)
        } else {
            updateExistingChat(existingChat, mensagem)
        }
        notificationRepository.notifyUser(mensagem.from.toString(), mensagem.body, context!!)
        updateUIState(mensagem)
    }

    private fun createNewChat(message: org.jivesoftware.smack.packet.Message, senderJid: String) {
        val newChat = UserChat(
            jid = removeAfterSlash(senderJid),
            lastMessage = message.body.toString(),
            lastMessageTime = System.currentTimeMillis(),
            chatName = LocalAccountsDataProvider.accounts.find {
                it.jid == removeAfterSlash(
                    senderJid
                )
            }?.displayName.toString(),
            chatPhotoUrl = "",
            isUnread = true,
            chatType = ChatType.User,
            lastSeen = 0
        )
        LocalChatsDataProvider.chats.add(newChat)
        updateChatList()
        println("Chat adicionado")
    }

    private fun updateExistingChat(
        existingChat: UserChat,
        mensagem: org.jivesoftware.smack.packet.Message
    ) {
        existingChat.lastMessage = mensagem.body.toString()
        existingChat.lastMessageTime = System.currentTimeMillis()
        existingChat.isUnread = true
        updateChatList()
    }

    private fun updateChatList() {
        _uiState.update {
            it.copy(
                unReadMessages = LocalChatsDataProvider.chats.count { chat -> chat.isUnread },
                chats = LocalChatsDataProvider.chats.sortedByDescending { it.lastMessageTime }
            )
        }
    }

    private fun updateUIState(message: org.jivesoftware.smack.packet.Message) {

        val key =
            if (currentLoggedInUser.jid == removeAfterSlash(message.from.toString())) removeAfterSlash(
                message.to.toString()
            ) else removeAfterSlash(message.from.toString())
        val mensagem = createMessage(message, message.from.toString(), message.to.toString())
        addMessageToMap(_messages, key, mensagem)

        if (_uiState.value.currentSelectedChat?.jid == key || _uiState.value.currentSelectedChat?.jid == removeAfterSlash(
                message.to.toString()
            )
        ) {
            _uiState.update {
                it.copy(
                    currentSelectedChat = _uiState.value.currentSelectedChat,
                    chatState = _chatStatesFlow.value.get(key),
                    lastMessage = mensagem,
                    unReadMessages = LocalChatsDataProvider.chats.count { chat -> chat.isUnread },
                    messages = it.messages.toMutableMap().apply {
                        val currentMessages = get(_uiState.value.currentSelectedChat?.jid).orEmpty()
                        _uiState.value.currentSelectedChat?.jid?.let { it1 ->
                            put(it1, currentMessages + mensagem)
                        }
                    }
                )
            }
        }
    }

    private fun observePresenceUpdates() {
        XmppManager.presenceUpdates.onEach { presenceUpdate ->

            _presenceUpdates.value = presenceUpdate
        }.launchIn(viewModelScope)
    }

    fun sendImageMessage(image: Message, context: Context) {

        val fileTrasferManger = XmppManager.getFileTransferManager()

        val roster = XmppManager.getRoster()


        val entry = roster.getEntry(JidCreate.entityBareFrom(image.to))


        val presence = roster.getPresence(entry.jid)
        if (presence.isAvailable) {
            val fullJid = presence.from
            val transfer =
                fileTrasferManger.createOutgoingFileTransfer(JidCreate.entityFullFrom(fullJid))
            val file = getFileFromUri(context, Uri.parse(image.content))
            println("Arquivo: $file")
            transfer.sendFile(file, file!!.name)

        }

        addMessageToMap(
            map = _messages,
            key = image.to,
            message = image
        )
        LocalChatsDataProvider.chats.find { it.jid == image.to }?.let {
            updateCurrentSelectedChat(
                chat = it
            )
        }
        _uiState.update {
            it.copy(
                messages = it.messages,
                currentSelectedChat = _uiState.value.currentSelectedChat,
            )
        }
        Log.i("Mensagens", _messages.toString())

    }

    private fun observeFileMessages() {
        val fileTransferManager = XmppManager.getFileTransferManager()
        fileTransferManager.addFileTransferListener { request ->
            val transfer = request.accept()
            var messageFile: Message? = null
            val mime = removeAfterSlash(request.mimeType)
            println("Arquivo recebido de ${request.requestor}: ${request.description} - ${request.fileName} - ${request.mimeType} - ${request.fileSize} - ")
            println(request.mimeType)
            val internalDir = context?.filesDir
            val file = File(internalDir, request.fileName)
            transfer.receiveFile(file)

            if (mime == "image") {
                messageFile = Message(
                    to = LocalLoggedAccounts.account.jid,
                    content = file.toString(),
                    senderId = request.requestor.toString(),
                    type = MessageType.Image,
                    timestamp = System.currentTimeMillis(),
                )
                addMessageToMap(
                    map = _messages,
                    key = removeAfterSlash(request.requestor.toString()),
                    message = messageFile
                )
                loadMessagesForChat(removeAfterSlash(request.requestor.toString()))
                println("Mensagens:${_messages.toString()} ")
                _uiState.update {
                    it.copy(
                        messages = it.messages.toMutableMap().apply {
                            val currentMessages =
                                get(request.requestor.toString()).orEmpty()
                            put(
                                request.requestor.toString(),
                                currentMessages + messageFile
                            )
                        },
                        currentSelectedChat = _uiState.value.currentSelectedChat,
                    )
                }
                Log.i("Arquivo", file.toString())
            }

        }
    }

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

    private fun isInPreview(): Boolean {
        return try {
            Class.forName("androidx.compose.ui.tooling.preview.Preview")
            true
        } catch (e: ClassNotFoundException) {
            false
        }
    }


    init {

        observePresenceUpdates()
        observeChatStates()
        initializeUiState()
        observeFileMessages()

    }


}