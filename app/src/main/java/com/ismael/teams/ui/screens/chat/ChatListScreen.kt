package com.ismael.teams.ui.screens.chat

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ismael.teams.R
import com.ismael.teams.data.model.Chat
import com.ismael.teams.data.model.ChatType
import com.ismael.teams.data.model.NavigationRoutes

@Composable
fun ChatList(
    postList: List<Chat>,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val chatListState = rememberLazyListState()

    LazyColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
        state = chatListState

    ) {
        item {
            Spacer(
                modifier.height(70.dp)
            )
        }
        items(
            items = postList,
            key = { it.jid }
        ) { post ->

            ChatCard(
                chatPreview = post,
                navController = navController,
                modifier = modifier
            )

            HorizontalDivider(
                modifier = Modifier
                    .padding(start = 70.dp, end = 0.dp)
                    .fillMaxWidth(),
                thickness = 0.8.dp
            )
        }
        item {
            Spacer(
                modifier.height(90.dp)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatCard(
    chatPreview: Chat,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .combinedClickable(
                onClick = {
                    Log.i("ChatCard", "Clicked on chat with id: ${chatPreview.jid}")
                    navController.navigate("${NavigationRoutes.ChatWithUser.substringBefore("/{chatId}")}/${chatPreview.jid}")
                },
                onLongClick = {}
            )
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp, start = 8.dp, end = 8.dp),

        ) {

        Row(
            modifier = Modifier
        ) {
            if (chatPreview.isUnread) {

                Text(
                    text = ".",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF945EF6),
                    fontSize = 30.sp,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                )

            } else {
                Spacer(
                    modifier = Modifier
                        .width(8.dp)
                )
            }
            if (chatPreview.chatType == ChatType.User) {
                UserIconWithStatus(
                    status = "available",
                    modifier = Modifier,
                    userProfile = R.drawable.yasmin
                )
            } else {
            }

        }

        Column(
            modifier = Modifier
                .padding(end = 24.dp)
        ) {
            Text(
                text = chatPreview.chatName,
                style = MaterialTheme.typography.titleMedium,
            )
            chatPreview.lastMessage?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
        Spacer(
            modifier = Modifier
                .width(90.dp)
        )
        Text(
            text = "10:00 AM",
            style = MaterialTheme.typography.bodyMedium,
        )

    }
}


@Composable
fun NewChatFloatingActionButton(
    onclick: () -> Unit,
    containerColor: Color,
    elevation: FloatingActionButtonElevation,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onclick,
        containerColor = containerColor,
        elevation = elevation,

        ) {
        Icon(
            painter = painterResource(R.drawable.edit_square_24px),
            contentDescription = null
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ChatFilterBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState()


    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = { onDismiss() },
            sheetState = sheetState,
        ) {
            Column(
                modifier = modifier
            ) {
                Row(
                    modifier = Modifier
                        .combinedClickable(
                            onClick = {},
                            onLongClick = {}
                        )
                        .fillMaxWidth(),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.mark_chat_unread_24px),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(4.dp)
                    )
                    Text(
                        text = "Unread",
                        modifier = Modifier
                            .padding(8.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .combinedClickable(
                            onClick = {},
                            onLongClick = {}
                        )
                        .fillMaxWidth(),

                    ) {
                    Icon(
                        painter = painterResource(R.drawable.meeting_room_24px),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(4.dp)
                    )
                    Text(
                        text = "Meeting",
                        modifier = Modifier
                            .padding(8.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .combinedClickable(
                            onClick = {},
                            onLongClick = {}
                        )
                        .fillMaxWidth(),

                    ) {
                    Icon(
                        painter = painterResource(R.drawable.volume_off_24px),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(4.dp)
                    )
                    Text(
                        text = "Muted",
                        modifier = Modifier
                            .padding(8.dp)
                    )
                }
                Spacer(
                    modifier = Modifier
                        .height(60.dp)
                )
            }
        }
    }

}