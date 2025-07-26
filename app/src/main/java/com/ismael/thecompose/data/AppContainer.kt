package com.ismael.thecompose.data

import android.content.Context

interface AppContainer{
    val xmppRepository : XmppRepository
    val messageRepository: MessageRepository
}

class DefaultAppContainer(private  val context: Context): AppContainer {

    override val xmppRepository: XmppRepository
        get() = TODO("Not yet implemented")

    override val messageRepository: MessageRepository by lazy {
        OfflineMessagesRepository(TheComposeDatabase.getDatabase(context).messageDao())
    }
}