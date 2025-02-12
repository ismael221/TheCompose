package com.ismael.thecompose

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ismael.thecompose.data.local.LocalAccountsDataProvider
import com.ismael.thecompose.data.local.LocalChatsDataProvider
import com.ismael.thecompose.data.local.LocalLoggedAccounts
import com.ismael.thecompose.data.model.Message
import com.ismael.thecompose.data.model.NavigationRoutes
import com.ismael.thecompose.ui.screens.ActivityExpandedScreen
import com.ismael.thecompose.ui.screens.ActivityMediumScreen
import com.ismael.thecompose.ui.screens.ActivityScreen
import com.ismael.thecompose.ui.screens.CalendarScreen
import com.ismael.thecompose.ui.screens.CallScreen
import com.ismael.thecompose.ui.screens.ExpandedActivityScreen
import com.ismael.thecompose.ui.screens.ExpandedCalendarScreen
import com.ismael.thecompose.ui.screens.ExpandedCallScreen
import com.ismael.thecompose.ui.screens.ExpandedMoreScreen
import com.ismael.thecompose.ui.screens.ExpandedTeamsScreen
import com.ismael.thecompose.ui.screens.MediumActivityScreen
import com.ismael.thecompose.ui.screens.MediumCalendarScreen
import com.ismael.thecompose.ui.screens.MediumCallScreen
import com.ismael.thecompose.ui.screens.MediumMoreScreen
import com.ismael.thecompose.ui.screens.MediumTeamsScreen
import com.ismael.thecompose.ui.screens.MoreCompactScreen
import com.ismael.thecompose.ui.screens.chat.CompactChatScreen
import com.ismael.thecompose.ui.screens.chat.ChatWithUser
import com.ismael.thecompose.ui.screens.SearchScreen
import com.ismael.thecompose.ui.screens.TeamsScreen
import com.ismael.thecompose.ui.screens.chat.ChatViewModel
import com.ismael.thecompose.ui.screens.chat.ExpandedChatScreen
import com.ismael.thecompose.ui.screens.chat.MediumChatScreen
import com.ismael.thecompose.ui.screens.chat.NewChatScreen
import com.ismael.thecompose.ui.screens.status.StatusScreen
import com.ismael.thecompose.ui.screens.user.UserViewModel
import com.ismael.thecompose.ui.utils.TheComposeNavigationType
import org.jivesoftware.smack.packet.Presence


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TheComposeApp(
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    val chatViewModel: ChatViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        chatViewModel.setContext(context)
        chatViewModel.observeIncomingMessages()
    }

    val chatUiState = chatViewModel.uiState.collectAsState().value
    val userUiState = userViewModel.uiState.collectAsState().value
    val navController: NavHostController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.CHAT
    ) {
        composable(route = NavigationRoutes.CHAT) { backStackEntry ->

            val chatId = backStackEntry.arguments?.getString("chatId")

            when (windowSize) {

                WindowWidthSizeClass.Compact -> {
                    Log.i("WindowSize", windowSize.toString())
                    CompactChatScreen(
                        drawerState = drawerState,
                        scope = scope,
                        chatUiState = chatUiState,
                        onStatusClick = { presence: String ->
                            userViewModel.updatePresence(
                                presence,
                                userUiState.status.toString()
                            )
                        },
                        userUiState = userUiState,
                        onNavigate = { route ->
                            navController.navigate(route)
                        },
                        modifier = modifier
                    )
                }

                WindowWidthSizeClass.Medium -> {
                    MediumChatScreen(
                        chatUiState = chatUiState,
                        onNavigate = { route ->
                            navController.navigate(route)
                        },
                        modifier = modifier
                    )
                }

                WindowWidthSizeClass.Expanded -> {

                    ExpandedChatScreen(
                        modifier = modifier,
                        chatUiState = chatUiState,
                        currentLoggedUser = LocalLoggedAccounts.account.jid,
                        onSendClick = {},
                        onAudioCaptured = {},
                        onImageCaptured = {},
                        onNavigate = { route ->
                            navController.navigate(route)
                        },
                        onChatSelected = { chatId: String ->
                            LocalChatsDataProvider.chats.find { it.jid == chatId }?.let {
                                chatViewModel.updateCurrentSelectedChat(
                                    chat = it
                                )
                                chatViewModel.loadMessagesForChat(chatId)
                            }
                        }
                    )

                }

                else -> {
                    CompactChatScreen(
                        drawerState = drawerState,
                        scope = scope,
                        chatUiState = chatUiState,
                        userUiState = userUiState,
                        modifier = modifier,
                        onNavigate = { route ->
                            navController.navigate(route)
                        },
                        onStatusClick = { presence: String ->
                            userViewModel.updatePresence(
                                presence,
                                userUiState.status.toString()
                            )
                        },
                    )
                }

            }

        }
        composable(route = NavigationRoutes.ACTIVITY) {
            when (windowSize) {
                WindowWidthSizeClass.Compact -> {
                    ActivityScreen(
                        drawerState = drawerState,
                        chatUiState = chatUiState,
                        userUiState = userUiState,
                        scope = scope,
                        onNavigate = { route ->
                            navController.navigate(route)
                        },
                        onPresenceClick = { presence ->
                            userViewModel.updatePresence(
                                presence,
                                userUiState.status.toString()
                            )
                        }
                    )
                }

                WindowWidthSizeClass.Medium -> {
                    ActivityMediumScreen(
                        onNavigate = { route ->
                            navController.navigate(route)
                        }
                    )
                }

                WindowWidthSizeClass.Expanded -> {
                    ActivityExpandedScreen(
                        onNavigate = { route ->
                            navController.navigate(route)
                        }
                    )
                }

                else -> {
                    ActivityScreen(
                        drawerState = drawerState,
                        chatUiState = chatUiState,
                        userUiState = userUiState,
                        onNavigate = { route ->
                            navController.navigate(route)
                        },
                        onPresenceClick = { presence ->
                            userViewModel.updatePresence(
                                presence,
                                userUiState.status.toString()
                            )
                        },
                        scope = scope
                    )
                }

            }
        }
        composable(route = NavigationRoutes.CALENDAR) {

            when (windowSize) {
                WindowWidthSizeClass.Compact -> {
                    CalendarScreen(
                        drawerState = drawerState,
                        chatUiState = chatUiState,
                        scope = scope,
                        onNavigate = { route ->
                            navController.navigate(route)
                        },
                        userUiState = userUiState,
                        onPresenceClick = { presence ->
                            userViewModel.updatePresence(
                                presence,
                                userUiState.status.toString()
                            )
                        }
                    )
                }

                WindowWidthSizeClass.Medium -> {
                    MediumCalendarScreen(
                        onNavigate = { route ->
                            navController.navigate(route)
                        }
                    )
                }

                WindowWidthSizeClass.Expanded -> {
                    ExpandedCalendarScreen(
                        onNavigate = { route ->
                            navController.navigate(route)
                        }
                    )
                }

            }
        }
        composable(route = NavigationRoutes.CALL) {

            when (windowSize) {
                WindowWidthSizeClass.Compact -> {
                    CallScreen(
                        drawerState = drawerState,
                        chatUiState = chatUiState,
                        userUiState = userUiState,
                        onNavigate = { route ->
                            navController.navigate(route)
                        },
                        onPresenceClick = { presence ->
                            userViewModel.updatePresence(
                                presence,
                                userUiState.status.toString()
                            )
                        },
                        scope = scope
                    )
                }

                WindowWidthSizeClass.Medium -> {
                    MediumCallScreen(
                        onNavigate = { route ->
                            navController.navigate(route)
                        }
                    )
                }

                WindowWidthSizeClass.Expanded -> {
                    ExpandedCallScreen(
                        onNavigate = { route ->
                            navController.navigate(route)
                        }
                    )
                }

            }
        }
        composable(route = NavigationRoutes.TEAMS) {
            when (windowSize) {
                WindowWidthSizeClass.Compact -> {
                    TeamsScreen(
                        onNavigate = { route ->
                            navController.navigate(route)
                        },
                        drawerState = drawerState,
                        chatUiState = chatUiState,
                        userUiState = userUiState,
                        onPresenceClick = { presence ->
                            userViewModel.updatePresence(
                                presence,
                                userUiState.status.toString()
                            )
                        },
                        scope = scope
                    )
                }

                WindowWidthSizeClass.Medium -> {
                    MediumTeamsScreen(
                        onNavigate = { route ->
                            navController.navigate(route)
                        }
                    )
                }

                WindowWidthSizeClass.Expanded -> {
                    ExpandedTeamsScreen(
                        onNavigate = { route ->
                            navController.navigate(route)
                        }
                    )
                }

            }
        }
        composable(route = NavigationRoutes.MORE) {
            when (windowSize) {
                WindowWidthSizeClass.Compact -> {
                    MoreCompactScreen(
                        isVisible = true,
                        onDismiss = { },
                        chatUiState = chatUiState,
                        onNavigate = { route ->
                            navController.navigate(route)
                        }
                    )
                }

                WindowWidthSizeClass.Medium -> {
                    MediumMoreScreen(
                        onNavigate = { route ->
                            navController.navigate(route)
                        }
                    )
                }

                WindowWidthSizeClass.Expanded -> {
                    ExpandedMoreScreen(
                        onNavigate = { route ->
                            navController.navigate(route)
                        }
                    )
                }

            }
        }
        composable(route = NavigationRoutes.SEARCHBAR) {
            when (windowSize) {
                WindowWidthSizeClass.Compact -> {
                    SearchScreen(
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }

                WindowWidthSizeClass.Medium -> {

                }

                WindowWidthSizeClass.Expanded -> {

                }

            }
        }
        composable(route = NavigationRoutes.NEWCHAT) {
            NewChatScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                suggestions = LocalAccountsDataProvider.accounts,
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }
        composable(route = NavigationRoutes.STATUS) {
            StatusScreen(
                status = userUiState.status.toString(),
                onStatusChange = { status ->
                    userViewModel.updateStatus(
                        status,
                        Presence.Mode.fromString(userUiState.mode.toString())
                    )
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = NavigationRoutes.CHATWITHUSER,
            arguments = listOf(navArgument("chatId") { type = NavType.StringType })
        ) { backStackEntry ->

            val chatId = backStackEntry.arguments?.getString("chatId")
            val selectedChat = LocalChatsDataProvider.chats.find { it.jid == chatId }

            when (windowSize) {
                WindowWidthSizeClass.Compact -> {
                    selectedChat?.let {
                        ChatWithUser(
                            onSendClick = { message: Message ->
                                chatViewModel.sendMessage(
                                    chatId = message.to,
                                    message = message
                                )
                            },
                            chatUiState = chatUiState,
                            selected = { chatId: String ->
                                chatViewModel.loadMessagesForChat(chatId)
                                chatViewModel.updateCurrentSelectedChat(chat = selectedChat)
                            },
                            chat = it,
                            navigationType = TheComposeNavigationType.BOTTOM_NAVIGATION,
                            currentLoggedUser = LocalLoggedAccounts.account.jid,
                            onImageCaptured = { image: Message? ->
                                chatViewModel.sendImageMessage(image!!, context)
                                chatViewModel.loadMessagesForChat(it.jid)
                            },
                            onAudioCaptured = { uri: Uri? ->

                            },
                            loadMessages = {
                                chatViewModel.loadMessagesForChat(it)
                            },
                            onBackClick = {
                                navController.popBackStack()
                            }

                        )
                    }
                }

                WindowWidthSizeClass.Medium -> {
                    selectedChat?.let {
                        ChatWithUser(
                            onSendClick = { message: Message ->
                                chatViewModel.sendMessage(
                                    chatId = message.to,
                                    message = message
                                )
                            },
                            chatUiState = chatUiState,
                            selected = { chatId: String ->
                                chatViewModel.loadMessagesForChat(chatId)
                                chatViewModel.updateCurrentSelectedChat(chat = selectedChat)
                            },
                            chat = it,
                            navigationType = TheComposeNavigationType.NAVIGATION_RAIL,
                            currentLoggedUser = LocalLoggedAccounts.account.jid,
                            onImageCaptured = { image: Message? ->
                                chatViewModel.sendImageMessage(image!!, context)
                                chatViewModel.loadMessagesForChat(it.jid)
                            },
                            onAudioCaptured = { uri: Uri? ->

                            },
                            onBackClick = {
                                navController.popBackStack()
                            },
                            loadMessages = {
                                chatViewModel.loadMessagesForChat(it)
                            },

                            )
                    }
                }

                else -> {
                    selectedChat?.let {
                        ChatWithUser(
                            onSendClick = { message: Message ->
                                chatViewModel.sendMessage(
                                    chatId = message.to,
                                    message = message
                                )
                            },
                            chatUiState = chatUiState,
                            selected = { chatId: String ->
                                chatViewModel.loadMessagesForChat(chatId)
                                chatViewModel.updateCurrentSelectedChat(chat = selectedChat)
                            },
                            chat = it,
                            navigationType = TheComposeNavigationType.NAVIGATION_RAIL,
                            currentLoggedUser = LocalLoggedAccounts.account.jid,
                            onImageCaptured = { image: Message? ->
                                chatViewModel.sendImageMessage(image!!, context)
                                chatViewModel.loadMessagesForChat(it.jid)
                            },
                            onAudioCaptured = { uri: Uri? ->

                            },
                            onBackClick = {
                                navController.popBackStack()
                            },
                            loadMessages = {
                                chatViewModel.loadMessagesForChat(it)
                            },

                            )
                    }
                }
            }

        }
    }

}


@Preview(showBackground = true)
@Composable
fun TeamsAppCompactPreview() {
    MaterialTheme {
        Surface {
            TheComposeApp(
                windowSize = WindowWidthSizeClass.Compact
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 700)
@Composable
fun TeamsAppMediumPreview() {
    MaterialTheme {
        Surface {
            TheComposeApp(
                windowSize = WindowWidthSizeClass.Medium
            )
        }
    }
}


@Preview(showBackground = true, widthDp = 1000)
@Composable
fun TeamsAppExpandedPreview() {
    MaterialTheme {
        Surface {
            TheComposeApp(
                windowSize = WindowWidthSizeClass.Expanded
            )
        }
    }
}