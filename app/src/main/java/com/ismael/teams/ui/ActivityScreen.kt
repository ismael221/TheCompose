package com.ismael.teams.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ismael.teams.data.DataSource


@Composable
fun ActivityList(
    modifier: Modifier = Modifier
){
    ChatList(
        DataSource().loadChats()
    )
}