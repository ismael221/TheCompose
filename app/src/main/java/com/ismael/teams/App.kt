package com.ismael.teams

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
import com.ismael.teams.data.local.LocalAccountsDataProvider
import com.ismael.teams.data.local.LocalChatsDataProvider
import com.ismael.teams.data.local.LocalLoggedAccounts
import com.ismael.teams.data.model.Message
import com.ismael.teams.data.model.NavigationRoutes
import com.ismael.teams.ui.screens.ActivityScreen
import com.ismael.teams.ui.screens.CalendarScreen
import com.ismael.teams.ui.screens.CallScreen
import com.ismael.teams.ui.screens.ExpandedActivityScreen
import com.ismael.teams.ui.screens.ExpandedCalendarScreen
import com.ismael.teams.ui.screens.ExpandedCallScreen
import com.ismael.teams.ui.screens.ExpandedMoreScreen
import com.ismael.teams.ui.screens.ExpandedTeamsScreen
import com.ismael.teams.ui.screens.MediumActivityScreen
import com.ismael.teams.ui.screens.MediumCalendarScreen
import com.ismael.teams.ui.screens.MediumCallScreen
import com.ismael.teams.ui.screens.MediumMoreScreen
import com.ismael.teams.ui.screens.MediumTeamsScreen
import com.ismael.teams.ui.screens.chat.CompactChatScreen
import com.ismael.teams.ui.screens.chat.ChatWithUser
import com.ismael.teams.ui.screens.MoreScreen
import com.ismael.teams.ui.screens.SearchScreen
import com.ismael.teams.ui.screens.TeamsScreen
import com.ismael.teams.ui.screens.chat.ChatViewModel
import com.ismael.teams.ui.screens.chat.ExpandedChatScreen
import com.ismael.teams.ui.screens.chat.MediumChatScreen
import com.ismael.teams.ui.screens.chat.NewChatScreen
import com.ismael.teams.ui.screens.status.StatusScreen
import com.ismael.teams.ui.screens.user.UserViewModel
import com.ismael.teams.ui.utils.TheComposeNavigationType
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
        composable(route = NavigationRoutes.CHAT) {

            when (windowSize) {

                WindowWidthSizeClass.Compact -> {
                    Log.i("WindowSize", windowSize.toString())
                    CompactChatScreen(
                        navController = navController,
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
                        modifier = modifier
                    )
                }

                WindowWidthSizeClass.Medium -> {
                    MediumChatScreen(
                        navController = navController,
                        chatUiState = chatUiState,
                        modifier = modifier
                    )
                }

                WindowWidthSizeClass.Expanded -> {
                    ExpandedChatScreen(
                        navController = navController,
                        modifier = modifier
                    )
                }

                else -> {
                    CompactChatScreen(
                        navController = navController,
                        drawerState = drawerState,
                        scope = scope,
                        chatUiState = chatUiState,
                        userUiState = userUiState,
                        modifier = modifier,
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
                        navController = navController,
                        drawerState = drawerState,
                        chatUiState = chatUiState,
                        userUiState = userUiState,
                        scope = scope
                    )
                }

                WindowWidthSizeClass.Medium -> {
                    MediumActivityScreen(
                        navController = navController
                    )
                }

                WindowWidthSizeClass.Expanded -> {
                    ExpandedActivityScreen(
                        navController = navController,
                    )
                }

                else -> {
                    ActivityScreen(
                        navController = navController,
                        drawerState = drawerState,
                        chatUiState = chatUiState,
                        userUiState = userUiState,
                        scope = scope
                    )
                }

            }
        }
        composable(route = NavigationRoutes.CALENDAR) {

            when (windowSize) {
                WindowWidthSizeClass.Compact -> {
                    CalendarScreen(
                        navController = navController,
                        drawerState = drawerState,
                        chatUiState = chatUiState,
                        scope = scope,
                        userUiState = userUiState,
                    )
                }

                WindowWidthSizeClass.Medium -> {
                    MediumCalendarScreen(
                        navController = navController
                    )
                }

                WindowWidthSizeClass.Expanded -> {
                    ExpandedCalendarScreen(
                        navController = navController
                    )
                }

            }
        }
        composable(route = NavigationRoutes.CALL) {

            when (windowSize) {
                WindowWidthSizeClass.Compact -> {
                    CallScreen(
                        navController = navController,
                        drawerState = drawerState,
                        chatUiState = chatUiState,
                        userUiState = userUiState,
                        scope = scope
                    )
                }

                WindowWidthSizeClass.Medium -> {
                    MediumCallScreen(
                        navController = navController
                    )
                }

                WindowWidthSizeClass.Expanded -> {
                    ExpandedCallScreen(
                        navController = navController
                    )
                }

            }
        }
        composable(route = NavigationRoutes.TEAMS) {
            when (windowSize) {
                WindowWidthSizeClass.Compact -> {
                    TeamsScreen(
                        navController = navController,
                        drawerState = drawerState,
                        chatUiState = chatUiState,
                        userUiState = userUiState,
                        scope = scope
                    )
                }

                WindowWidthSizeClass.Medium -> {
                    MediumTeamsScreen(
                        navController = navController,
                    )
                }

                WindowWidthSizeClass.Expanded -> {
                    ExpandedTeamsScreen(
                        navController = navController
                    )
                }

            }
        }
        composable(route = NavigationRoutes.MORE) {
            when (windowSize) {
                WindowWidthSizeClass.Compact -> {
                    MoreScreen(
                        isVisible = true,
                        onDismiss = { },
                        navController = navController,
                        chatUiState = chatUiState
                    )
                }

                WindowWidthSizeClass.Medium -> {
                    MediumMoreScreen(
                        navController = navController
                    )
                }

                WindowWidthSizeClass.Expanded -> {
                    ExpandedMoreScreen(
                        navController = navController
                    )
                }

            }
        }
        composable(route = NavigationRoutes.SEARCHBAR) {
            when (windowSize) {
                WindowWidthSizeClass.Compact -> {
                    SearchScreen(
                        navController = navController,
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
                suggestions = LocalAccountsDataProvider.accounts,
                navController = navController
            )
        }
        composable(route = NavigationRoutes.STATUS) {
            StatusScreen(
                navController = navController,
                status = userUiState.status.toString(),
                onStatusChange = { status ->
                    userViewModel.updateStatus(
                        status,
                        Presence.Mode.fromString(userUiState.mode.toString())
                    )
                },
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
                            navController = navController,
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
                            },
                            onAudioCaptured = { uri: Uri? ->

                            },

                        )
                    }
                }

                WindowWidthSizeClass.Medium -> {
                    selectedChat?.let {
                        ChatWithUser(
                            navController = navController,
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
                            onImageCaptured = {  image: Message? ->
                                chatViewModel.sendImageMessage(image!!,context)
                            },
                            onAudioCaptured = {  uri: Uri? ->

                            },

                        )
                    }
                }

                else -> {
                    selectedChat?.let {
                        ChatWithUser(
                            navController = navController,
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
                            onImageCaptured = {  image: Message? ->
                                chatViewModel.sendImageMessage(image!!,context)
                            },
                            onAudioCaptured = {  uri: Uri? ->

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