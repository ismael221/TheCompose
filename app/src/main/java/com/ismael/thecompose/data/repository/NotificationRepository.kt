package com.ismael.thecompose.data.repository

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import androidx.core.content.ContextCompat
import com.ismael.thecompose.R
import com.ismael.thecompose.data.local.LocalLoggedAccounts
import com.ismael.thecompose.ui.utils.removeAfterSlash

class NotificationRepository {

    private var context: Context? = null

    fun setContext(context: Context) {
        this.context = context
    }

    private val REQUEST_CODE_PERMISSION = 100

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

    fun notifyUser(from: String?, body: String, context: Context) {
        createNotificationChannel()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                showNotification(from, body, context)
            } else {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_CODE_PERMISSION
                )
            }
        } else {
            showNotification(from, body, context)
        }
    }

    @SuppressLint("MissingPermission")
    fun showNotification(from: String?, body: String, context: Context) {
        val notificationManager = NotificationManagerCompat.from(context)

        val sender = from ?: "unknown_sender"
        val notificationId = sender.hashCode()

        val newMessage = NotificationCompat.MessagingStyle.Message(
            body,
            System.currentTimeMillis(),
            removeAfterSlash(sender)
        )

        LocalLoggedAccounts.notifications.getOrPut(removeAfterSlash(sender.toString())) { mutableListOf() }.add(newMessage)

        val existingNotification = notificationManager.activeNotifications
            .firstOrNull { it.id == notificationId }

        val messagingStyle = if (existingNotification != null) {
            val existingStyle = NotificationCompat.MessagingStyle.extractMessagingStyleFromNotification(
                existingNotification.notification
            )
            existingStyle?.addMessage(newMessage) ?: NotificationCompat.MessagingStyle("You").addMessage(newMessage)
        } else {
            NotificationCompat.MessagingStyle("You").addMessage(newMessage)
        }

        // Configurar a ação de resposta direta
        val replyLabel = "Digite sua resposta"
        val remoteInput = RemoteInput.Builder("key_text_reply")
            .setLabel(replyLabel)
            .build()

        val replyIntent = Intent(context, NotificationReplyReceiver::class.java).apply {
            putExtra("notification_id", notificationId)
            putExtra("sender", sender)
        }

        val replyPendingIntent: PendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            replyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        val action = NotificationCompat.Action.Builder(
            R.drawable.send_24px,
            "Responder",
            replyPendingIntent
        )
            .addRemoteInput(remoteInput)
            .build()

        val notification = NotificationCompat.Builder(context, "messages_channel")
            .setSmallIcon(R.drawable.notifications_24px)
            .setStyle(messagingStyle)
            .addAction(action) // Adiciona a ação de resposta
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId, notification)
    }
}