package com.ismael.teams.ui.chat

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ismael.teams.R
import com.ismael.teams.data.DataSource
import com.ismael.teams.model.Chat
import com.ismael.teams.model.Message
import com.ismael.teams.model.NavigationRoutes
import com.ismael.teams.ui.SideNavBarItems
import com.ismael.teams.ui.TeamsBottomNavigationBar
import com.ismael.teams.ui.TeamsTopAppBar
import com.ismael.teams.ui.TopBarDropdownMenu
import com.ismael.teams.ui.UserDetails
import com.ismael.teams.ui.utils.TeamsScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.text.substringBefore


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
            UserIcon(
                modifier = Modifier,
                painter = painterResource(R.drawable.yasmin),
                contentDescription = null,
                onclick = {}
            )

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

@Composable
fun UserIcon(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String?,
    onclick: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier
                .size(40.dp)
                .clip(MaterialTheme.shapes.extraLarge)
                .clickable {
                    onclick()
                },
            contentScale = ContentScale.Crop
        )
    }
}


@Composable
fun ChatMessageBottomAppBar(
    uiState: ChatUiState,
    onSendClick: (Message) -> Unit,
    modifier: Modifier = Modifier
) {
    var content by remember { mutableStateOf("") }

    NavigationBar(
        content = {
            Row(
                modifier = modifier
                    .padding(2.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    colors = IconButtonColors(
                        contentColor = Color.White,
                        containerColor = Color(0xFF7D4DD2),
                        disabledContainerColor = Color(0xFF7D4DD2),
                        disabledContentColor = Color(0xFF7D4DD2)
                    ),
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .size(25.dp),
                    onClick = { /*TODO*/ }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier
                    )
                }
                OutlinedTextField(
                    value = content,
                    shape = MaterialTheme.shapes.large,
                    onValueChange = { content = it },
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
                        )
                    },
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .wrapContentSize()
                        .weight(1f)
                )
                if (content == "") {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.photo_camera_24px),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(start = 2.dp)
                                .size(30.dp)
                        )
                    }
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.mic_24px),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 2.dp)
                                .size(30.dp)
                        )
                    }

                } else {
                    IconButton(
                        onClick = {
                            Log.i("Botão de envio clicado",content)
                            uiState.currentSelectedChat?.jid?.let {
                                Message(
                                    text = content,
                                    senderId = "ismael221@ismael",
                                    timestamp = System.currentTimeMillis(),
                                    to = it
                                )
                            }?.let {
                                onSendClick(
                                    it
                                )
                            }
                            content = ""
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.send_24px),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 2.dp)
                                .size(30.dp)
                        )
                    }
                }
            }
        }
    )
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

@Composable
fun ChatBubble(
    message: String,
    isUserMessage: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isUserMessage) Color(0xFF7D4DD2) else Color.DarkGray
    val textColor = Color.White

    Row(
        modifier = modifier
            .padding(
                start = if (isUserMessage) 40.dp else 0.dp,
                end = if (!isUserMessage) 40.dp else 0.dp
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            horizontalAlignment = if (isUserMessage) Alignment.End else Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .background(
                        backgroundColor,
                        RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = message,
                    color = textColor,
                    modifier = Modifier
                        .wrapContentSize(),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserChatTopBar(
    navController: NavController,
    modifier: Modifier = Modifier,
    chat: Chat
) {
    TopAppBar(
        modifier = modifier,
        title = {

            chat.lastMessage?.let {
                UserDetails(
                    userName = chat.chatName,
                    secondaryText = it,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

        },
        navigationIcon = {
            IconButton(
                onClick = {
                    navController.navigateUp()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        actions = {

            IconButton(
                onClick = {}
            ) {
                Icon(
                    painter = painterResource(R.drawable.videocam_24px),
                    contentDescription = null
                )
            }

            IconButton(
                onClick = {}
            ) {
                Icon(
                    painter = painterResource(R.drawable.call_filled_24px),
                    contentDescription = null
                )
            }

            IconButton(
                onClick = {}
            ) {
                Icon(
                    painter = painterResource(R.drawable.info_24px),
                    contentDescription = null
                )
            }

        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatWithUser(
    navController: NavController,
    onSendClick: (Message) -> Unit,
    chatUiState: ChatUiState,
    chat: Chat,
    viewModel: ChatViewModel = viewModel(),
    selected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(chat.jid) {
        viewModel.loadMessagesForChat(chat.jid)
        selected(chat.jid)
    }
    Scaffold(
        topBar = {
            UserChatTopBar(
                chat = chat,
                navController = navController
            )
        },
        bottomBar = {
            ChatMessageBottomAppBar(
                onSendClick = onSendClick,
                uiState = chatUiState
            )
        },

        ) { innerPadding ->

        ChatMessages(
            messages = chatUiState.currentChatMessages,
            modifier = modifier
                .padding(innerPadding)
        )
    }
}

@Composable
fun ChatMessages(
    messages: List<Message>,
    modifier: Modifier = Modifier
) {
    val myUser = "ismael221@ismael"
    val chatListState = rememberLazyListState()

    LaunchedEffect(messages) {
        chatListState.scrollToItem(chatListState.layoutInfo.totalItemsCount)
    }
    Log.i("ChatMessages", "Messages received: ${messages.size}")
    LazyColumn(
        state = chatListState,
        modifier = modifier
    ) {
        item {
            Spacer(
                modifier.height(90.dp)
            )
        }
        items(
            items = messages,
            key = { it.key }
        ) { message ->
            ChatBubble(
                message = message.text,
                isUserMessage = message.senderId == myUser,
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)

            )
        }
        item {
            Spacer(
                modifier.height(90.dp)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatScreen(
    navController: NavController,
    scope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    modifier: Modifier = Modifier
) {

    var expanded by remember { mutableStateOf(false) }

    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SideNavBarItems()
        }
    ) {
        var showBottomSheet by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TeamsTopAppBar(
                    currentScreen = TeamsScreen.ChatList,
                    onFilterClick = { showBottomSheet = true },
                    scrollBehavior = topAppBarScrollBehavior,
                    onSearchBarClick = {
                        navController.navigate(TeamsScreen.SearchBarList.name)
                    },
                    onUserIconClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    },
                    onDropdownMenuClick = {
                        expanded = !expanded
                    }

                )
            },
            bottomBar = {
                TeamsBottomNavigationBar(
                    currentScreen = TeamsScreen.ChatList,
                    navController = navController
                )
            },
            floatingActionButton = {
                NewChatFloatingActionButton(
                    onclick = { },
                    containerColor = MaterialTheme.colorScheme.primary,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                )
            },

            modifier = modifier

        ) {
            ChatList(
                postList = DataSource().loadChats(),
                navController = navController,
            )
            if (showBottomSheet) {
                ChatFilterBottomSheet(
                    isVisible = showBottomSheet,
                    onDismiss = { showBottomSheet = false }
                )
            }

            TopBarDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = !expanded },
                currentScreen = TeamsScreen.ChatList,
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun TeamsChatScreenPreview() {
    val navController: NavHostController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    MaterialTheme(
        darkColorScheme()
    ) {
//        ChatWithUser(
//            navController = navController
//        )

    }
}