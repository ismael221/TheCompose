package com.ismael.thecompose.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ismael.thecompose.TheComposeApplication
import com.ismael.thecompose.ui.screens.chat.ChatViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
            ChatViewModel(theComposeApplication().container.messageRepository)
        }

    }
}

fun CreationExtras.theComposeApplication(): TheComposeApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as TheComposeApplication)