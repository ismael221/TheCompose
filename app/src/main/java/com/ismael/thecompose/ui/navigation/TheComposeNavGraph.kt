package com.ismael.thecompose.ui.navigation

import android.net.Uri
import android.util.Log
import androidx.compose.material3.DrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ismael.thecompose.model.NavigationRoutes
import androidx.navigation.navArgument
import com.ismael.thecompose.data.local.LocalChatsDataProvider
import com.ismael.thecompose.data.local.LocalLoggedAccounts
import com.ismael.thecompose.model.Message
import com.ismael.thecompose.ui.screens.activity.ActivityExpandedScreen
import com.ismael.thecompose.ui.screens.activity.ActivityMediumScreen
import com.ismael.thecompose.ui.screens.activity.ActivityScreen
import com.ismael.thecompose.ui.screens.calendar.CalendarScreen
import com.ismael.thecompose.ui.screens.calls.CallScreen
import com.ismael.thecompose.ui.screens.calendar.ExpandedCalendarScreen
import com.ismael.thecompose.ui.screens.calls.ExpandedCallScreen
import com.ismael.thecompose.ui.screens.more.ExpandedMoreScreen
import com.ismael.thecompose.ui.screens.teams.ExpandedTeamsScreen
import com.ismael.thecompose.ui.screens.calendar.MediumCalendarScreen
import com.ismael.thecompose.ui.screens.calls.MediumCallScreen
import com.ismael.thecompose.ui.screens.more.MediumMoreScreen
import com.ismael.thecompose.ui.screens.teams.MediumTeamsScreen
import com.ismael.thecompose.ui.screens.more.MoreCompactScreen
import com.ismael.thecompose.ui.screens.chat.CompactChatScreen
import com.ismael.thecompose.ui.screens.chat.ChatWith
import com.ismael.thecompose.ui.screens.search.SearchScreen
import com.ismael.thecompose.ui.screens.teams.TeamsScreen
import com.ismael.thecompose.ui.screens.chat.ExpandedChatScreen
import com.ismael.thecompose.ui.screens.chat.MediumChatScreen
import com.ismael.thecompose.ui.screens.chat.NewChatScreen
import androidx.navigation.NavType
import com.ismael.thecompose.ui.screens.chat.ChatUiState
import com.ismael.thecompose.ui.screens.chat.ChatViewModel
import com.ismael.thecompose.ui.screens.chat.UserSearchUiState
import com.ismael.thecompose.ui.utils.TheComposeNavigationType
import org.jivesoftware.smack.packet.Presence
import com.ismael.thecompose.ui.screens.status.StatusScreen
import com.ismael.thecompose.ui.screens.user.UserUiState
import com.ismael.thecompose.ui.screens.user.UserViewModel
import kotlinx.coroutines.CoroutineScope


@Composable
fun TheComposeNavHost(
    navController: NavHostController,
    scope: CoroutineScope,
    drawerState: DrawerState,
    windowSize: WindowWidthSizeClass,
    chatUiState: ChatUiState,
    userUiState: UserUiState,
    searchUiState: UserSearchUiState,
    userViewModel: UserViewModel,
    chatViewModel: ChatViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.CHAT,
        modifier = modifier
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
                searchUiState = searchUiState,
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
                        ChatWith(
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
                        ChatWith(
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
                            onNavigate = {
                                navController.navigate(it)
                            }

                        )
                    }
                }

                else -> {
                    selectedChat?.let {
                        ChatWith(
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
                            onNavigate = {
                                navController.navigate(it)
                            }

                        )
                    }
                }
            }

        }
    }
}