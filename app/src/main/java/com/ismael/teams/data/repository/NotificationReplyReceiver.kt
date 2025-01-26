package com.ismael.teams.data.repository

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import com.ismael.teams.R
import com.ismael.teams.data.local.LocalLoggedAccounts
import com.ismael.teams.data.local.LocalLoggedAccounts._messages
import com.ismael.teams.data.model.Message
import com.ismael.teams.data.remote.xmpp.XmppManager
import com.ismael.teams.ui.utils.MessageType
import com.ismael.teams.ui.utils.addMessageToMap
import com.ismael.teams.ui.utils.removeAfterSlash
import org.jxmpp.jid.impl.JidCreate

class NotificationReplyReceiver : BroadcastReceiver() {

    private val xmppManager: XmppManager = XmppManager

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val replyText = RemoteInput.getResultsFromIntent(intent)?.getCharSequence("key_text_reply").toString()
        val sender = intent.getStringExtra("sender")
        val notificationId = intent.getIntExtra("notification_id", -1)


        xmppManager.sendMessage(JidCreate.entityBareFrom(removeAfterSlash(sender.toString())), replyText)

        val message = Message(
            to = removeAfterSlash(sender.toString()),
            content = replyText,
            senderId = LocalLoggedAccounts.account.jid,
            type = MessageType.Text,
            timestamp = System.currentTimeMillis()
        )

        addMessageToMap(_messages, removeAfterSlash(sender.toString()), message)
        val previousMessages = LocalLoggedAccounts.notifications.getOrPut(removeAfterSlash(sender.toString())) { mutableListOf() }


        val newMessage = NotificationCompat.MessagingStyle.Message(
            replyText,
            System.currentTimeMillis(),
            "Você"  // Adicionando a sua resposta
        )
        previousMessages.add(newMessage)


        val messagingStyle = NotificationCompat.MessagingStyle("Você")


        previousMessages.forEach { message ->
            messagingStyle.addMessage(message)
        }


        val updatedNotification = NotificationCompat.Builder(context, "messages_channel")
            .setSmallIcon(R.drawable.notifications_24px)
            .setStyle(messagingStyle)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build() // Chama o `build()` para finalizar a construção

        // Exibir a notificação atualizada
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(notificationId, updatedNotification)
    }
}
