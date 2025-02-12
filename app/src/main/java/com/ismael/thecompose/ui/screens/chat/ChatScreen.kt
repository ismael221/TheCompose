package com.ismael.thecompose.ui.screens.chat

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.Navigator
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.ismael.thecompose.R
import com.ismael.thecompose.data.local.LocalChatsDataProvider
import com.ismael.thecompose.data.local.LocalLoggedAccounts
import com.ismael.thecompose.data.local.generateRandomUserChats
import com.ismael.thecompose.data.model.Chat
import com.ismael.thecompose.data.model.GroupChat
import com.ismael.thecompose.data.model.Message
import com.ismael.thecompose.data.model.NavigationRoutes
import com.ismael.thecompose.data.model.UserChat
import com.ismael.thecompose.data.remote.xmpp.XmppManager.sendChatState
import com.ismael.thecompose.ui.components.SideNavBarItems
import com.ismael.thecompose.ui.components.TeamsBottomNavigationBar
import com.ismael.thecompose.ui.components.TeamsTopAppBar
import com.ismael.thecompose.ui.components.TheComposeNavigationRail
import com.ismael.thecompose.ui.components.TopBarDropdownMenu
import com.ismael.thecompose.ui.components.UserDetails
import com.ismael.thecompose.ui.screens.TeamsScreen
import com.ismael.thecompose.ui.screens.user.UserUiState
import com.ismael.thecompose.ui.utils.MessageType
import com.ismael.thecompose.ui.utils.TheComposeNavigationType
import com.ismael.thecompose.ui.utils.createInitialsBitmap
import com.ismael.thecompose.ui.utils.media.createImageUri
import com.ismael.thecompose.ui.utils.toFormattedDateString
import com.ismael.thecompose.ui.utils.toLocalDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jivesoftware.smack.packet.Presence
import org.jivesoftware.smackx.chatstates.ChatState
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt


@Composable
fun UserIcon(
    modifier: Modifier = Modifier,
    imageBitmap: ImageBitmap,
    contentDescription: String?,
    onclick: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        Image(
            bitmap = imageBitmap,
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
    userProfile: ImageBitmap,
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
            imageBitmap = userProfile,
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


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ChatMessageBottomAppBar(
    uiState: ChatUiState,
    onSendClick: (Message) -> Unit,
    onImageCaptured: (Message?) -> Unit,
    replyMessage: Message?,
    onReplyDismiss: () -> Unit,
    onAudioCaptured: (Uri?) -> Unit,
    modifier: Modifier = Modifier
) {
    var content by remember { mutableStateOf("") }
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var audioUri by remember { mutableStateOf<Uri?>(null) }
    var contentType: MessageType? = null
    var message: Message? = null
    var visible by remember { mutableStateOf(false) }

    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    )

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            Log.i("Foto tirada com sucesso", imageUri.toString())
            message = Message(
                content = imageUri.toString(),
                senderId = LocalLoggedAccounts.account.jid,
                timestamp = System.currentTimeMillis(),
                to = uiState.currentSelectedChat?.jid.toString(),
                type = MessageType.Image
            )
            onImageCaptured(message)
            contentType = MessageType.Image
        }
    }

    val takeVideoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo()
    ) { success ->
        if (success) {
            contentType = MessageType.Video
        }
    }

    val recordAudioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            audioUri = uri
            onAudioCaptured(audioUri)
            contentType = MessageType.Audio
        }
    }

    LaunchedEffect(permissionsState) {
        if (!permissionsState.allPermissionsGranted) {
            permissionsState.launchMultiplePermissionRequest()
        }
    }

    Column(
        modifier = modifier
    ) {

        AnimatedVisibility(replyMessage != null) {
            NavigationBar(
                content = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        QuotedMessage(
                            isUserReply = false,
                            quotedText = replyMessage?.content ?: "",
                            user = replyMessage?.senderId ?: "",
                            modifier = Modifier
                                .fillMaxWidth(0.9f)

                        )

                        IconButton(
                            onClick = {
                                onReplyDismiss()
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.close_24px),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(start = 2.dp)
                            )
                        }
                    }
                }
            )
        }

        NavigationBar(
            content = {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(2.dp),
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
                        onClick = { /*TODO OPEN THE OPTIONS TO UPLOAD FILES OR WHATEVER*/ }
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
                            contentType = MessageType.Text
                        },
                        label = {
                            Text(
                                text = stringResource(R.string.type_message),
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { /*TODO ADD A EMOJI LIBRARY*/ }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.mood_24px),
                                    contentDescription = null,
                                    modifier = Modifier
                                )
                            }
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
                                        content = content,
                                        senderId = LocalLoggedAccounts.account.jid,
                                        timestamp = System.currentTimeMillis(),
                                        to = it,
                                        type = contentType!!
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
                    )
                    if (content == "") {
                        IconButton(
                            onClick = {
                                val uri = createImageUri(context)
                                imageUri = uri
                                uri?.let {
                                    takePictureLauncher.launch(it)
                                }

                            }
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
                            onClick = {
                                val intent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)
                                if (intent.resolveActivity(context.packageManager) != null) {
                                    // Há um aplicativo que suporta a ação
                                    recordAudioLauncher.launch(intent)
                                } else {
                                    // Nenhum aplicativo suporta a ação
                                    Toast.makeText(
                                        context,
                                        "Nenhum aplicativo de gravação de áudio encontrado",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
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
                                        content = content,
                                        senderId = LocalLoggedAccounts.account.jid,
                                        timestamp = System.currentTimeMillis(),
                                        to = it,
                                        type = contentType!!
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
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatBubble(
    content: Any,
    isUserMessage: Boolean,
    isQuoted: Boolean,
    userName: String? = null,
    type: MessageType,
    reaction: String,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }
    val backgroundColor = if (isUserMessage) Color(0xFF7D4DD2) else Color.DarkGray
    val textColor = Color.White

    var reaction by remember { mutableStateOf(reaction) }

    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally() + fadeIn(),
        exit = slideOutHorizontally() + fadeOut()
    ) {
        ReactionDialog(
            onDismiss = {
                visible = false
            },
            onReactionSelected = {
                reaction = it
            },
            modifier = modifier
                .padding(start = 16.dp, top = 24.dp)
        )
    }

    Row(
        modifier = modifier
            .combinedClickable(
                onClick = {
                    Log.i("ChatMessages", "Message clicked: ${content}")
                },
                onLongClick = {
                    Log.i("ChatMessages", "Message long clicked: ${content}")
                    visible = true
                }
            )
            .padding(
                start = if (isUserMessage) 40.dp else 0.dp,
                end = if (!isUserMessage) 40.dp else 0.dp
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 2.dp,
                    bottom = if (reaction != "") 16.dp else 2.dp,
                    start = 8.dp,
                    end = 8.dp
                ),
            horizontalAlignment = if (isUserMessage) Alignment.End else Alignment.Start
        ) {

            Box(
                modifier = Modifier
                    .background(
                        backgroundColor,
                        RoundedCornerShape(16.dp)
                    )
                    .padding(if (isQuoted) 16.dp else 0.dp)
            ) {

                Column(
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    if (isQuoted) {
                        QuotedMessage(
                            !isUserMessage,
                            user = userName ?: "",
                            quotedText = "This is a quoted message that you are replying to"
                        )
                    }
                    when (type) {
                        MessageType.Audio -> {

                        }

                        MessageType.Video -> {

                        }

                        MessageType.Text -> {
                            Text(
                                text = content.toString(),
                                color = textColor,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .wrapContentSize(),
                            )
                        }

                        MessageType.Sticker -> {

                        }

                        MessageType.Image -> {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    ImageRequest.Builder(LocalContext.current)
                                        .data(content)
                                        .build()
                                ),
                                contentDescription = "Preview da imagem",
                                modifier = Modifier
                                    .size(400.dp)
                                    .padding(2.dp)
                            )
                        }

                        MessageType.File -> {

                        }
                    }

                }
                if (reaction != "") {
                    Box(
                        modifier = Modifier
                            .align(if (isUserMessage) Alignment.BottomEnd else Alignment.BottomStart)
                            .offset(y = 22.dp, x = (0).dp)
                    ) {
                        ReactionBadge(
                            reaction = reaction,
                            onBadgeClick = {
                                reaction = ""
                            },
                            modifier = modifier
                                .align(Alignment.BottomEnd)
                        )
                    }
                }

            }
        }
    }


}

@Composable
fun ReactionBadge(
    reaction: String,
    onBadgeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF5E5C5C),
                contentColor = Color.White
            ),
            onClick = {
                onBadgeClick()
            },
            shape = CircleShape,
            modifier = Modifier

                .align(Alignment.BottomEnd)

        ) {
            Text(
                text = reaction,
                modifier = Modifier
                    .border(1.dp, Color.White, CircleShape)
                    .padding(4.dp)
            )
        }
    }
}

@Composable
fun ReactionDialog(
    onDismiss: () -> Unit,
    onReactionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Popup(
        onDismissRequest = {
            onDismiss()
        },
        properties = PopupProperties(focusable = true),
    ) {
        ReactionsCard(
            onReactionClick = {
                onReactionSelected(it)
                onDismiss()
            },
            modifier = modifier
        )
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
    chatUiState: ChatUiState,
    onBackClick: () -> Unit,
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
                        userProfilePic = ImageBitmap.imageResource(R.drawable.yasmin)
                    )
                }

                is GroupChat -> {
                    UserDetails(
                        userName = chat.chatName,
                        secondaryText = (chat as? GroupChat)?.members?.size.toString(),
                        modifier = Modifier
                            .fillMaxWidth(),
                        userStatus = "",
                        userProfilePic = createInitialsBitmap("Teste")
                    )
                }
            }


        },
        navigationIcon = {
            IconButton(
                onClick = {
                    onBackClick()
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
    onSendClick: (Message) -> Unit,
    onAudioCaptured: (Uri?) -> Unit,
    onImageCaptured: (Message?) -> Unit,
    chatUiState: ChatUiState,
    currentLoggedUser: String,
    chat: Chat,
    selected: (String) -> Unit,
    onBackClick: () -> Unit,
    loadMessages: (String) -> Unit,
    navigationType: TheComposeNavigationType,
    modifier: Modifier = Modifier,
) {
    var replyingMessage by remember { mutableStateOf<Message?>(null) }

    LaunchedEffect(chat.jid) {
        loadMessages(chat.jid)
        selected(chat.jid)
    }
    if (navigationType == TheComposeNavigationType.NAVIGATION_RAIL) {
        Row(
            modifier = modifier
        ) {
            TheComposeNavigationRail(
                currentScreen = TeamsScreen.CHAT,
                onNavigationSelected = {

                },
                modifier = modifier
            )
            Scaffold(
                topBar = {
                    UserChatTopBar(
                        chatUiState = chatUiState,
                        chat = chat,
                        onBackClick = {
                            onBackClick()
                        }
                    )
                },
                bottomBar = {
                    ChatMessageBottomAppBar(
                        onSendClick = onSendClick,
                        uiState = chatUiState,
                        onImageCaptured = {
                            onImageCaptured(it)
                        },
                        onAudioCaptured = onAudioCaptured,
                        replyMessage = replyingMessage,
                        onReplyDismiss = {
                            replyingMessage = null
                        }

                    )
                },

                ) { innerPadding ->

                ChatMessages(
                    messages = chatUiState.currentChatMessages,
                    user = currentLoggedUser,
                    modifier = modifier
                        .padding(innerPadding),
                    states = chatUiState.chatState,
                    onSlideToReply = { message ->
                        Log.i("Reply", message.content)
                        replyingMessage = message
                    }
                )


            }
        }
    } else {
        Scaffold(
            topBar = {
                UserChatTopBar(
                    chatUiState = chatUiState,
                    onBackClick = onBackClick,
                    chat = chat,
                )
            },
            bottomBar = {
                ChatMessageBottomAppBar(
                    onSendClick = onSendClick,
                    uiState = chatUiState,
                    onAudioCaptured = {},
                    replyMessage = replyingMessage,
                    onImageCaptured = {
                        onImageCaptured(it)
                    },
                    onReplyDismiss = {
                        replyingMessage = null
                    }
                )
            },

            ) { innerPadding ->

            ChatMessages(
                states = chatUiState.chatState,
                messages = chatUiState.currentChatMessages,
                user = currentLoggedUser,
                onSlideToReply = { message ->
                    Log.i("Reply", message.content)
                    replyingMessage = message
                },
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
    onSlideToReply: (Message) -> Unit,
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
            var offsetX by remember { mutableStateOf(0f) }

            ChatBubble(
                content = message.content,
                isUserMessage = message.senderId == user,
                type = message.type,
                isQuoted = false,
                userName = "",
                reaction = "",
                modifier = Modifier
                    .offset { IntOffset(offsetX.roundToInt(), 0) }
                    .draggable(
                        orientation = Orientation.Horizontal,
                        state = rememberDraggableState { delta ->
                            Log.i("Delta", delta.toString())

                            if (delta > 0) {
                                offsetX += delta
                            } else {
                                offsetX = 0F
                            }

                        },
                        onDragStopped = {
                            offsetX = 0F
                            onSlideToReply(message)
                        }
                    )
                    .padding(
                        top = paddingTop
                    ),
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
                        .animateItem(fadeInSpec = tween(1000), fadeOutSpec = tween(1000))
                )
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CompactChatScreen(
    chatUiState: ChatUiState,
    onStatusClick: (String) -> Unit,
    userUiState: UserUiState,
    onNavigate: (String) -> Unit,
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
                onSelectPresence = { onStatusClick(it) },
                loggedUser = LocalLoggedAccounts.account,
                onNavigate = { onNavigate(it) },
                userUiState = userUiState,
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
                        onNavigate(NavigationRoutes.SEARCHBAR)
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
                    onNavigationSelected = { route ->
                        onNavigate(route)
                    }
                )
            },
            floatingActionButton = {
                NewChatFloatingActionButton(
                    onclick = {
                        onNavigate(NavigationRoutes.NEWCHAT)
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                )
            },

            modifier = modifier

        ) {
            ChatList(
                chats = chatUiState.chats,
                onChatSelected = { route ->
                    onNavigate(route)
                },
                showSpacer = true,
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
    chatUiState: ChatUiState,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
    ) {
        TheComposeNavigationRail(
            currentScreen = TeamsScreen.CHAT,
            onNavigationSelected = { route ->
                onNavigate(route)
            },
            modifier = Modifier
        )
        ChatList(
            chats = chatUiState.chats,
            showSpacer = false,
            onChatSelected = {}
        )
    }

}


@Composable
fun ExpandedChatScreen(
    chatUiState: ChatUiState,
    chat: Chat? = null,
    currentLoggedUser: String,
    onNavigate: (String) -> Unit,
    onSendClick: (Message) -> Unit,
    onAudioCaptured: (Uri?) -> Unit,
    onImageCaptured: (Message?) -> Unit,
    modifier: Modifier = Modifier
) {
    var replyingMessage by remember { mutableStateOf<Message?>(null) }
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxSize()
    ) {
        TheComposeNavigationRail(
            currentScreen = TeamsScreen.CHAT,
            onNavigationSelected = {
                onNavigate(it)
            },
            modifier = Modifier
        )
        Column(
            modifier = Modifier
                .padding(4.dp)
                .width(400.dp)
                .fillMaxHeight()
        ) {
            ExpandedChatTopBar(
                onDropdownMenuClick = {
                    expanded = !expanded
                },
                modifier = Modifier
            )
            ExpandedChatListAndDetailContent(
                modifier = Modifier
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {
            Scaffold(
                topBar = {
                    if (chat != null) {
                        UserChatTopBar(
                            chatUiState = chatUiState,
                            chat = chat,
                            onBackClick = {},
                            modifier = Modifier
                        )
                    }
                },
                bottomBar = {
                    if (chat != null) {
                        ExpandedBottomChatBar()
                    }
                },

                ) { innerPadding ->

                ChatMessages(
                    messages = chatUiState.currentChatMessages,
                    user = currentLoggedUser,
                    modifier = modifier
                        .padding(innerPadding),
                    states = chatUiState.chatState,
                    onSlideToReply = { message ->
                        Log.i("Reply", message.content)
                        replyingMessage = message
                    }
                )

                TopBarDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = !expanded },
                    currentScreen = TeamsScreen.CHAT,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandedChatTopBar(
    onDropdownMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = "Chat",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        },
        actions = {
            Row(
                modifier = Modifier
            ) {
                IconButton(
                    onClick = {
                        onDropdownMenuClick()
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.more_horiz_24px),
                        contentDescription = null
                    )
                }
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        painter = painterResource(R.drawable.filter_list_24px),
                        contentDescription = null
                    )
                }
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        painter = painterResource(R.drawable.edit_square_24px),
                        contentDescription = null
                    )
                }
            }
        },
        modifier = modifier
    )
}

@Composable
fun ExpandedChatListAndDetailContent(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        ChatList(
            chats = generateRandomUserChats(),
            showSpacer = false,
            onChatSelected = {}
        )
    }
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

@Composable
fun QuotedMessage(
    isUserReply: Boolean = false,
    quotedText: String,
    user: String,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isUserReply) Color(0xFF7D4DD2) else Color.DarkGray
    if (quotedText != "" && user != "") {
        Row(
            modifier = modifier
                .background(shape = RoundedCornerShape(8.dp), color = backgroundColor)
                .height(IntrinsicSize.Min),
        ) {
            VerticalDivider(
                color = Color.Gray,
                thickness = 4.dp,
                modifier = Modifier.padding(8.dp)
            )
            Column(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .padding(8.dp)
            ) {
                Text(
                    text = user,
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                )
                Text(
                    text = quotedText,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                    modifier = Modifier
                        .wrapContentSize()
                        .weight(1f)
                )
            }
        }
    }
}

@Composable
fun ExpandedBottomChatBar(
    modifier: Modifier = Modifier
) {
    var content by remember { mutableStateOf("") }
    Box(
        modifier = modifier
            .wrapContentSize()
    ) {
        OutlinedTextField(
            value = content,
            label = {
                Text(text = stringResource(R.string.type_message))
            },
            onValueChange = {
                content = it
            },
            trailingIcon = {
                Row(
                    modifier = Modifier
                ) {
                    IconButton(
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.mood_24px),
                            contentDescription = null,
                            modifier = Modifier
                                .size(35.dp)
                        )
                    }
                    IconButton(
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.add_24px),
                            contentDescription = null,
                            modifier = Modifier
                                .size(35.dp)
                        )
                    }
                    VerticalDivider(
                        modifier = Modifier
                            .height(45.dp)
                            .padding(4.dp)
                    )
                    IconButton(
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.send_24px),
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReactionsCard(
    onReactionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val reactions = listOf(
        "👍",
        "😂",
        "\uD83D\uDE2F",
        "❤\uFE0F",
        "🥲",
        "🙏",
        "🤣"
    )
    Box(
        modifier = modifier
            .padding(8.dp)
    ) {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF5C5A5A)
            )
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                reactions.forEach {
                    Text(
                        text = it,
                        fontSize = 22.sp,
                        modifier = Modifier
                            .padding(4.dp)
                            .combinedClickable(
                                onClick = {
                                    onReactionClick(it)
                                },
                            )
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ChatScreenCompactPreview() {
    MaterialTheme {
//        CompactChatScreen(
//            chatUiState = ChatUiState(),
//            onStatusClick = {},
//            userUiState = UserUiState(),
//            onNavigate = {},
//            modifier = Modifier
//        )
//        ReactionsCard()
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
//            ReactionDialog(
//                onDismiss = {},
//                modifier = Modifier
//                    .padding(start = 16.dp, top = 16.dp)
//            )
            ChatBubble(
                content = "Olá, tudo bem?",
                isUserMessage = true,
                type = MessageType.Text,
                modifier = Modifier,
                isQuoted = false,
                reaction = "👍"
            )
            ChatBubble(
                content = "Olá, tudo bem?",
                isUserMessage = false,
                type = MessageType.Text,
                modifier = Modifier,
                isQuoted = false,
                reaction = "\uD83D\uDE0D"
            )
            ChatBubble(
                content = "Olá, tudo bem?",
                isUserMessage = true,
                type = MessageType.Text,
                modifier = Modifier,
                isQuoted = false,
                reaction = ""
            )
            ChatBubble(
                content = "Olá, tudo bem?",
                isUserMessage = false,
                type = MessageType.Text,
                modifier = Modifier,
                isQuoted = false,
                reaction = ""
            )
            ChatBubble(
                content = "Olá, tudo bem?",
                isUserMessage = true,
                type = MessageType.Text,
                modifier = Modifier,
                isQuoted = false,
                reaction = ""
            )
            ChatBubble(
                content = "Olá, tudo bem? açldfçaksdflçkasfl~çkasdfl~çkasdfl~çkasdfl~çkasdf~çlkasd",
                isUserMessage = false,
                type = MessageType.Text,
                modifier = Modifier,
                isQuoted = false,
                reaction = ""
            )
            ChatBubble(
                content = "Olá, tudo bem? açldfçaksdflçkasfl~çkasdfl~çkasdfl~çkasdfl~çkasdf~çlkasd",
                isUserMessage = false,
                type = MessageType.Text,
                modifier = Modifier,
                isQuoted = false,
                reaction = "❤\uFE0F"
            )
            ChatBubble(
                content = "Olá, tudo bem?",
                isUserMessage = true,
                type = MessageType.Text,
                modifier = Modifier,
                isQuoted = false,
                reaction = ""
            )
            ChatBubble(
                content = "Olá, tudo bem?",
                isUserMessage = false,
                type = MessageType.Text,
                modifier = Modifier,
                isQuoted = false,
                reaction = ""
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenMediumPreview() {
    MaterialTheme {
        MediumChatScreen(
            chatUiState = ChatUiState(),
            onNavigate = {},
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true, widthDp = 1000)
@Composable
fun ChatScreenExpandedPreview() {

    MaterialTheme {
        ExpandedChatScreen(
            chatUiState = ChatUiState(),
            currentLoggedUser = LocalLoggedAccounts.account.jid,
            onSendClick = {},
            onAudioCaptured = {},
            onImageCaptured = {},
            onNavigate = {}
        )
    }
}