package com.ismael.thecompose.data

import com.ismael.thecompose.network.XmppService

interface XmppRepository {
    suspend fun connect(): Unit
}


class NetworkXmppRepository(
    private val xmppService: XmppService

) : XmppRepository {
    val server = "ismael"
    val username = "ismael221"
    val password = "Ismuca18@"
    val config = XmppService.createXmppConfig(server, username, password)

    override suspend fun connect() = xmppService.connect(config)

}