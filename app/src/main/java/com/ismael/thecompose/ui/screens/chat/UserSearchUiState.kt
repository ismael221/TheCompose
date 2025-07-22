package com.ismael.thecompose.ui.screens.chat

import com.ismael.thecompose.model.User

data class UserSearchUiState(
    val suggestions: List<User> = emptyList(),
)