package com.ismael.teams.xmpp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.jxmpp.jid.impl.JidCreate

class XmppViewModel: ViewModel() {

    private val xmppManager = XmppManager()

    // States for Compose UI
    var messages = mutableListOf<String>()
        private set

    // Expor as mensagens recebidas para a UI
    val incomingMessages: StateFlow<List<String>> = xmppManager.incomingMessages

    fun connect(server: String, username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            xmppManager.addMessageListener { message ->
                messages.add("${message.from}: ${message.body}")
            }
            xmppManager.connect(server, username, password)
        }
    }

    fun disconnect() {
        viewModelScope.launch(Dispatchers.IO) {
            xmppManager.disconnect()
        }
    }

    fun sendMessage(to: String, message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val recipientJid = JidCreate.entityBareFrom(to)
            Log.i("Mensagem","Sending message to $recipientJid: $message")
            xmppManager.sendMessage(recipientJid, message)

        }
    }
}