package com.ismael.thecompose.data.local

import com.ismael.thecompose.data.model.User

object LocalAccountsDataProvider {

    val accounts = listOf(
        User(
            id = "1",
            jid = "ismael221@ismael",
            username = "ismael221",
            displayName = "Ismael Nunes Campos"
        ),
        User(
            id = "2",
            jid = "yasmin@ismael",
            username = "yasmin",
            displayName = "Yasmin Rodrigues"
        ),
        User(
            id = "3",
            jid = "debora@ismael",
            username = "debora",
            displayName = "Debora Nunes"
        ),
    )
}