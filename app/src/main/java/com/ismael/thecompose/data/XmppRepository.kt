package com.ismael.thecompose.data

import com.ismael.thecompose.network.XmppService

interface XmppRepository {
    suspend fun connect(): Unit
}


class NetworkXmppRepository(
    private val xmppService: XmppService

) : XmppRepository {
    val server = "NTB-33.doalti.corp"
    val username = "ismael"
    val password = "ismael221"
    val config = XmppService.createXmppConfig(server, username, password)

    override suspend fun connect() = xmppService.connect(config)

}