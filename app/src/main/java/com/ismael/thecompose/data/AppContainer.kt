package com.ismael.thecompose.data

interface AppContainer{
    val xmppRepository : XmppRepository
}

class DefaultAppContainer: AppContainer {

    override val xmppRepository: XmppRepository
        get() = TODO("Not yet implemented")
}