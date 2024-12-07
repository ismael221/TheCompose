package com.ismael.teams.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ismael.teams.R
import com.ismael.teams.data.DataSource
import com.ismael.teams.model.ChatPreview
import kotlinx.coroutines.launch


@Composable
fun ChatList(
    postList: List<ChatPreview>,
    modifier: Modifier = Modifier
){
    LazyColumn {
        item {
            Spacer(
                modifier.height(90.dp)
            )
        }
        items(
            items = postList,
            key = {it.key}
        ) { post ->
            ChatCard(
                chatPreview = post,
                modifier = modifier
            )
            HorizontalDivider(
                modifier = Modifier
                    .padding(start = 64.dp, end = 0.dp)
                    .fillMaxWidth(),
                thickness = 1.dp
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
    chatPreview: ChatPreview,
    modifier: Modifier = Modifier,
){
    Row(
        modifier = modifier
            .combinedClickable(
                onClick = {},
                onLongClick = {}
            )
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        UserIcon(
            modifier = Modifier
                .padding(8.dp),
            painter = painterResource(chatPreview.userImage),
            contentDescription = null
        )
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(
                text = stringResource(chatPreview.username),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = stringResource(chatPreview.lastMessage),
                style = MaterialTheme.typography.bodySmall,
            )
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

@Composable
fun UserIcon(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String?
){
  Box(
      modifier = modifier
  ){
    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = Modifier
            .size(40.dp)
            .clip(MaterialTheme.shapes.extraLarge),
        contentScale = ContentScale.Crop
    )
  }
}

@Composable
fun ChatMessageBottomAppBar(
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(
            colors = IconButtonColors(
                contentColor = Color.White,
                containerColor = Color.Magenta,
                disabledContainerColor = Color.Red,
                disabledContentColor = Color.Red
            ),
            modifier = Modifier
                .padding(top = 8.dp),
            onClick = { /*TODO*/ }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        }
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = {
                Text(
                    text = stringResource(R.string.type_message),
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.mood_24px),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                )
            },
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),
        )
        Icon(
            painter = painterResource(R.drawable.photo_camera_24px),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 8.dp, end = 2.dp)
                .size(30.dp)
        )
        Icon(
            painter = painterResource(R.drawable.mic_24px),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 8.dp, end = 2.dp)
                .size(30.dp)
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
){
    val sheetState = rememberModalBottomSheetState()

    if (isVisible) {
            ModalBottomSheet(
                onDismissRequest = { onDismiss() },
                sheetState = sheetState
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
                        ){
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

                    ){
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

                        ){
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
                }
            }
        }

}

@Composable
fun ChatBubble(
    modifier: Modifier = Modifier
){

}

@Preview(showBackground = true)
@Composable
private fun TeamsChatScreenPreview(){
    ChatBubble()
}