package com.ismael.teams.ui.chat

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.ismael.teams.model.Message

class ChatViewModel: ViewModel() {

    private val _messages = mutableStateListOf<Message>()
    val messages: List<Message> = _messages

    fun sendMessage(to:String, content:String){
        _messages.add(
            Message(
                content = content,
                from = to
            )
        )
    }

    fun loadMessages(messages:  List<Message>){
            messages.forEach { message ->
                _messages.add(message)
            }
    }

//
//    private val _uiState = MutableStateFlow(ReplyUiState())
//    val uiState: StateFlow<ReplyUiState> = _uiState
//
//    init {
//        initializeUIState()
//    }
//
//    private fun initializeUIState() {
//        val mailboxes: Map<MailboxType, List<Email>> =
//            LocalEmailsDataProvider.allEmails.groupBy { it.mailbox }
//        _uiState.value =
//            ReplyUiState(
//                mailboxes = mailboxes,
//                currentSelectedEmail = mailboxes[MailboxType.Inbox]?.get(0)
//                    ?: LocalEmailsDataProvider.defaultEmail
//            )
//    }
//
//    fun updateDetailsScreenStates(email: Email) {
//        _uiState.update {
//            it.copy(
//                currentSelectedEmail = email,
//                isShowingHomepage = false
//            )
//        }
//    }
//
//    fun resetHomeScreenStates() {
//        _uiState.update {
//            it.copy(
//                currentSelectedEmail = it.mailboxes[it.currentMailbox]?.get(0)
//                    ?: LocalEmailsDataProvider.defaultEmail,
//                isShowingHomepage = true
//            )
//        }
//    }
//
//    fun updateCurrentMailbox(mailboxType: MailboxType) {
//        _uiState.update {
//            it.copy(
//                currentMailbox = mailboxType
//            )
//        }
//    }
}