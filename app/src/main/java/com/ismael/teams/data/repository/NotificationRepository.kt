package com.ismael.teams.data.repository

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.ismael.teams.R
import com.ismael.teams.ui.utils.removeAfterSlash

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

        // Garantir que 'from' não é nulo ao calcular o hashCode
        val sender = from ?: "unknown_sender"
        val notificationId = sender.hashCode()

        val newMessage = NotificationCompat.MessagingStyle.Message(
            body,
            System.currentTimeMillis(),
            removeAfterSlash(sender)
        )

        // Recuperar uma notificação existente
        val existingNotification = notificationManager.activeNotifications
            .firstOrNull { it.id == notificationId }

        val messagingStyle = if (existingNotification != null) {
            // Se a notificação já existe, adicionar nova mensagem ao estilo existente
            val existingStyle =
                NotificationCompat.MessagingStyle.extractMessagingStyleFromNotification(
                    existingNotification.notification
                )
            existingStyle?.addMessage(newMessage) ?: NotificationCompat.MessagingStyle("You")
                .addMessage(newMessage)
        } else {
            // Criar um novo estilo de mensagem
            NotificationCompat.MessagingStyle("You").addMessage(newMessage)
        }

        val notification = NotificationCompat.Builder(context, "messages_channel")
            .setSmallIcon(R.drawable.notifications_24px)
            .setStyle(messagingStyle)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId, notification)
    }
}