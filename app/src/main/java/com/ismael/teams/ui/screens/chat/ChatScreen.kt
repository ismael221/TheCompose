package com.ismael.teams.ui.screens.chat

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ismael.teams.R
import com.ismael.teams.data.local.LocalLoggedAccounts
import com.ismael.teams.data.model.Chat
import com.ismael.teams.data.model.GroupChat
import com.ismael.teams.data.model.Message
import com.ismael.teams.data.model.NavigationRoutes
import com.ismael.teams.data.model.UserChat
import com.ismael.teams.data.remote.xmpp.XmppManager.sendChatState
import com.ismael.teams.ui.components.SideNavBarItems
import com.ismael.teams.ui.components.TeamsBottomNavigationBar
import com.ismael.teams.ui.components.TeamsTopAppBar
import com.ismael.teams.ui.components.TheComposeNavigationRail
import com.ismael.teams.ui.components.TopBarDropdownMenu
import com.ismael.teams.ui.components.UserDetails
import com.ismael.teams.ui.screens.TeamsScreen
import com.ismael.teams.ui.screens.user.UserUiState
import com.ismael.teams.ui.utils.TheComposeNavigationType
import com.ismael.teams.ui.utils.toFormattedDateString
import com.ismael.teams.ui.utils.toLocalDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jivesoftware.smack.packet.Presence
import org.jivesoftware.smackx.chatstates.ChatState
import java.time.format.DateTimeFormatter


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
fun UserIconWithStatus(
    status: String,
    @DrawableRes userProfile: Int,
    modifier: Modifier = Modifier
) {
    BadgedBox(
        badge = {
            UserStatusBadge(
                status = status,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = -6.dp, y = 25.dp)
            )
        },
        modifier = modifier
    ) {
        UserIcon(
            modifier = modifier,
            painter = painterResource(userProfile),
            contentDescription = null,
            onclick = {}
        )
    }
}

@Composable
fun UserStatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(color = Color.Black, shape = Shapes().small),
    ) {
        Image(
            when (status) {
                "available" -> painterResource(R.drawable.available)
                "dnd" -> painterResource(R.drawable.dnd)
                "away" -> painterResource(R.drawable.away)
                "xa" -> painterResource(R.drawable.away)
                else -> {
                    painterResource(R.drawable.offline)
                }
            },
            contentDescription = null,
            modifier = Modifier
                .size(12.dp)
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
                    onValueChange = {
                        content = it
                        if (content.isNotBlank() && content.isNotEmpty()) {
                            sendChatState(
                                to = uiState.currentSelectedChat?.jid.toString(),
                                ChatState.composing
                            )
                        } else {
                            sendChatState(
                                to = uiState.currentSelectedChat?.jid.toString(),
                                ChatState.paused
                            )
                        }
                    },
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
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            sendChatState(
                                to = uiState.currentSelectedChat?.jid.toString(),
                                ChatState.active
                            )
                        },
                        onSend = {
                            sendChatState(
                                to = uiState.currentSelectedChat?.jid.toString(),
                                ChatState.gone
                            )
                            uiState.currentSelectedChat?.jid?.let {
                                Message(
                                    text = content,
                                    senderId = LocalLoggedAccounts.account.jid,
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

                    ),
                    modifier = Modifier
                        .focusable()
                        .onKeyEvent {

                            //TODO add a counter when I delete it decreases, so I compare it with the last value
                            if (it.key == Key.Backspace || it.key == Key.Delete) {
                                sendChatState(
                                    to = uiState.currentSelectedChat?.jid.toString(),
                                    ChatState.paused
                                )
                                true
                            } else {
                                false
                            }
                        }
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
                            sendChatState(
                                to = uiState.currentSelectedChat?.jid.toString(),
                                ChatState.gone
                            )
                            Log.i("Botão de envio clicado", content)
                            uiState.currentSelectedChat?.jid?.let {
                                Message(
                                    text = content,
                                    senderId = LocalLoggedAccounts.account.jid,
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

@Composable
fun ChatBubbleAnimation(
    isUserMessage: Boolean,
    states: ChatState,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isUserMessage) Color(0xFF7D4DD2) else Color.DarkGray

    val infiniteTransition = rememberInfiniteTransition(label = "infinite")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(300, 20, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset"
    )
    val offset2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(300, 30, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset"
    )
    val offset3 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(300, 40, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset"
    )

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
                Row(
                    modifier = Modifier
                ) {
                    Image(
                        painter = painterResource(R.drawable.dot_symbol),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(2.dp)
                            .offset(y = if (states == ChatState.composing) offset.dp else 0.dp)
                            .size(10.dp)
                    )
                    Image(
                        painter = painterResource(R.drawable.dot_symbol),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(2.dp)
                            .offset(y = if (states == ChatState.composing) offset2.dp else 0.dp)
                            .size(10.dp)
                    )
                    Image(
                        painter = painterResource(R.drawable.dot_symbol),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(2.dp)
                            .offset(y = if (states == ChatState.composing) offset3.dp else 0.dp)
                            .size(10.dp)
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserChatTopBar(
    navController: NavController,
    chatUiState: ChatUiState,
    modifier: Modifier = Modifier,
    chat: Chat
) {
    TopAppBar(
        modifier = modifier,
        title = {

            when (chat) {
                is UserChat -> {
                    UserDetails(
                        userName = chat.chatName,
                        secondaryText = when (chatUiState.mode) {
                            Presence.Mode.available -> "Online"
                            Presence.Mode.dnd -> "Do not disturb"
                            Presence.Mode.away -> "Away"
                            Presence.Mode.xa -> "Be right back"
                            else -> {
                                "Offline"
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        userStatus = chatUiState.mode.toString(),
                        userProfilePic = R.drawable.yasmin
                    )
                }

                is GroupChat -> {
                    UserDetails(
                        userName = chat.chatName,
                        secondaryText = (chat as? GroupChat)?.members?.size.toString(),
                        modifier = Modifier
                            .fillMaxWidth(),
                        userStatus = "",
                        userProfilePic = 1
                    )
                }
            }


        },
        navigationIcon = {
            IconButton(
                onClick = {
                    navController.navigate(NavigationRoutes.CHAT)
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
    currentLoggedUser: String,
    chat: Chat,
    viewModel: ChatViewModel = viewModel(),
    selected: (String) -> Unit,
    navigationType: TheComposeNavigationType,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(chat.jid) {
        viewModel.loadMessagesForChat(chat.jid)
        selected(chat.jid)
    }
    if (navigationType == TheComposeNavigationType.NAVIGATION_RAIL) {
        Row(
            modifier = modifier
        ) {
            TheComposeNavigationRail(
                currentScreen = TeamsScreen.CHAT,
                navController = navController,
                modifier = modifier
            )
            Scaffold(
                topBar = {
                    UserChatTopBar(
                        chatUiState = chatUiState,
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
                    user = currentLoggedUser,
                    modifier = modifier
                        .padding(innerPadding),
                    states = chatUiState.chatState
                )


            }
        }
    } else {
        Scaffold(
            topBar = {
                UserChatTopBar(
                    chatUiState = chatUiState,
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
                states = chatUiState.chatState,
                messages = chatUiState.currentChatMessages,
                user = currentLoggedUser,
                modifier = modifier
                    .padding(innerPadding)
            )

        }
    }

}

@Composable
fun ChatMessages(
    states: ChatState? = null,
    messages: List<Message>,
    user: String,
    modifier: Modifier = Modifier
) {
    val chatListState = rememberLazyListState()

    LaunchedEffect(messages) {
        chatListState.scrollToItem(chatListState.layoutInfo.totalItemsCount)
    }
    Log.i("ChatMessages", "Messages received: ${messages.size}")
    LazyColumn(
        state = chatListState,
        modifier = modifier
            .padding(8.dp)
    ) {
        itemsIndexed(messages) { index, message ->

            val showDateDivider = if (index == 0) {
                true
            } else {
                val previousMessageDate = messages[index - 1].timestamp.toLocalDate()
                val currentMessageDate = message.timestamp.toLocalDate()
                previousMessageDate != currentMessageDate
            }

            if (showDateDivider) {
                DateDivider(date = message.timestamp.toFormattedDateString())
            }

            val isDifferentSender = if (index == 0) {
                true
            } else {
                messages[index - 1].senderId != message.senderId
            }

            val paddingTop = if (isDifferentSender) 16.dp else 0.dp

            ChatBubble(
                message = message.text,
                isUserMessage = message.senderId == user,
                modifier = Modifier
                    .padding(
                        start = 8.dp,
                        end = 8.dp,
                        top = paddingTop
                    )
            )
        }
        item {
            if (states == ChatState.composing || states == ChatState.paused) {
                val lastMessageDifferentSender = if (messages.isNotEmpty()) {
                    messages.last().senderId == user
                } else {
                    false
                }

                val animationPaddingTop = if (lastMessageDifferentSender) 16.dp else 0.dp

                ChatBubbleAnimation(
                    states = states,
                    isUserMessage = false,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, top = animationPaddingTop)
                )
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CompactChatScreen(
    navController: NavController,
    chatUiState: ChatUiState,
    onStatusClick: (String) -> Unit,
    userUiState: UserUiState,
    scope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    modifier: Modifier = Modifier
) {

    var expanded by remember { mutableStateOf(false) }

    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SideNavBarItems(
                onItemClick = { onStatusClick(it) },
                loggedUser = LocalLoggedAccounts.account,
                userUiState = userUiState,
                navController = navController
            )
        }
    ) {
        var showBottomSheet by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TeamsTopAppBar(
                    currentScreen = TeamsScreen.CHAT,
                    onFilterClick = { showBottomSheet = true },
                    scrollBehavior = topAppBarScrollBehavior,
                    onSearchBarClick = {
                        navController.navigate(TeamsScreen.SERARCHBAR.name)
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
                    currentScreen = TeamsScreen.CHAT,
                    unReadMessages = chatUiState.unReadMessages,
                    navController = navController
                )
            },
            floatingActionButton = {
                NewChatFloatingActionButton(
                    onclick = { navController.navigate(NavigationRoutes.NEWCHAT) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                )
            },

            modifier = modifier

        ) {
            ChatList(
                chats = chatUiState.chats,
                navController = navController,
                showSpacer = true
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
                currentScreen = TeamsScreen.CHAT,
            )
        }
    }

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MediumChatScreen(
    navController: NavController,
    chatUiState: ChatUiState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
    ) {
        TheComposeNavigationRail(
            currentScreen = TeamsScreen.CHAT,
            navController = navController,
            modifier = Modifier
        )

        ChatList(
            chats = chatUiState.chats,
            navController = navController,
            showSpacer = false,
        )


    }

}


@Composable
fun ExpandedChatScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    TheComposeNavigationRail(
        currentScreen = TeamsScreen.CHAT,
        navController = navController,
        modifier = modifier
    )
}




@Composable
fun DateDivider(date: String) {
    Text(
        text = date.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        textAlign = TextAlign.Center
    )
}


@Preview(showBackground = true, widthDp = 700)
@Composable
fun ChatScreenMediumPreview() {
    MaterialTheme {
        Surface {
//            MediumChatScreen(
//                navController = rememberNavController(),
//            )
            DateDivider(System.currentTimeMillis().toFormattedDateString())
        }
    }
}