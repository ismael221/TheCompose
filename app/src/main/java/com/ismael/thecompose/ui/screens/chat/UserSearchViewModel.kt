package com.ismael.thecompose.ui.screens.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ismael.thecompose.data.model.User
import com.ismael.thecompose.data.remote.xmpp.XmppManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.jivesoftware.smackx.search.ReportedData
import org.jivesoftware.smackx.xdata.form.FillableForm
import org.jivesoftware.smackx.xdata.form.Form
import org.jxmpp.jid.impl.JidCreate
import java.util.UUID


class UserSearchViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UserSearchUiState())
    val uiState = _uiState.asStateFlow()

    private val xmppManager = XmppManager

    private fun initializeUiState() {
        val search = xmppManager.getSearchManager()
        val domain = JidCreate.domainBareFrom("search.ismael")
        val searchForm = Form(search.getSearchForm(domain))
        val answerForm: FillableForm = searchForm.fillableForm

        answerForm.setAnswer("search", "*")
        answerForm.setAnswer("Username", true)
        answerForm.setAnswer("Name", true)
        answerForm.setAnswer("Email", true)

        val data: ReportedData = search.getSearchResults(answerForm.dataFormToSubmit, domain)
        Log.i("UserSearchViewModel", "Data: ${data.rows.size}")

        val suggestions = emptyList<User>()
        val updatedSuggestions = suggestions.toMutableList()


        for (row in data.rows) {
            println("Usuário encontrado: " + row.getValues("jid")[0])

            val jid = row.getValues("jid")[0]

            val username = if (row.getValues("Usuário")
                    .isEmpty()
            ) "N/A" else (row.getValues("Usuário")[0] as String)!!
            val name = if (row.getValues("Nome")
                    .isEmpty()
            ) "N/A" else (row.getValues("Nome")[0] as String)!!
            val email = if (row.getValues("Email")
                    .isEmpty()
            ) "N/A" else (row.getValues("Email")[0] as String)!!

            println("---------------------")
            println("Username: $username")
            println("Nome: $name")
            println("Email: $email")
            println("---------------------")


            val user = User(
                jid = jid.toString(),
                username = username,
                displayName = name,
                id = UUID.randomUUID().toString(),
                avatarUrl = "",
            )

            updatedSuggestions.add(user)
        }
        _uiState.value = UserSearchUiState(
            suggestions = updatedSuggestions
        )

    }

    init {
        initializeUiState()
    }
}