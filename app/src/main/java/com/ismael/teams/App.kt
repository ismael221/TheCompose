package com.ismael.teams

import android.annotation.SuppressLint
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ismael.teams.data.repository.DataSource
import com.ismael.teams.data.model.Message
import com.ismael.teams.data.model.NavigationRoutes
import com.ismael.teams.ui.screens.ActivityScreen
import com.ismael.teams.ui.screens.CalendarScreen
import com.ismael.teams.ui.screens.CallScreen
import com.ismael.teams.ui.screens.chat.ChatScreen
import com.ismael.teams.ui.screens.chat.ChatWithUser
import com.ismael.teams.ui.screens.MoreScreen
import com.ismael.teams.ui.screens.SearchScreen
import com.ismael.teams.ui.screens.TeamsScreen
import com.ismael.teams.ui.screens.chat.ChatViewModel


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TheComposeApp(
    modifier: Modifier = Modifier
) {
    val viewModel: ChatViewModel = viewModel()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.setContext(context)
        viewModel.observeIncomingMessages()
    }


    val chatUiState = viewModel.uiState.collectAsState().value

    val navController: NavHostController = rememberNavController()


    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = backStackEntry?.destination?.route


    val currentScreen = when (currentRoute) {
        NavigationRoutes.ChatList -> TeamsScreen.ChatList
        NavigationRoutes.ActivityList -> TeamsScreen.ActivityList
        NavigationRoutes.CalendarList -> TeamsScreen.CalendarList
        NavigationRoutes.CallList -> TeamsScreen.CallList
        NavigationRoutes.TeamsList -> TeamsScreen.TeamsList
        NavigationRoutes.SearchBarList -> TeamsScreen.SearchBarList
        NavigationRoutes.More -> TeamsScreen.More
        NavigationRoutes.ChatWithUser.substringBefore("/") -> TeamsScreen.ChatWithUser
        else -> TeamsScreen.ChatList
    }


    //Todo Create diffrent TopBars for Each Screen

    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.ChatList
    ) {
        composable(route = NavigationRoutes.ChatList) {
            ChatScreen(
                navController = navController,
                drawerState = drawerState,
                scope = scope
            )
        }
        composable(route = NavigationRoutes.ActivityList) {
            ActivityScreen(
                navController = navController,
                drawerState = drawerState,
                scope = scope
            )
        }
        composable(route = NavigationRoutes.CalendarList) {
            CalendarScreen(
                navController = navController,
                drawerState = drawerState,
                scope = scope
            )
        }
        composable(route = NavigationRoutes.CallList) {
            CallScreen(
                navController = navController,
                drawerState = drawerState,
                scope = scope
            )
        }
        composable(route = NavigationRoutes.TeamsList) {
            TeamsScreen(
                navController = navController,
                drawerState = drawerState,
                scope = scope
            )
        }
        composable(route = NavigationRoutes.More) {
            MoreScreen(
                isVisible = true,
                onDismiss = { },
                navController = navController
            )
        }
        composable(route = NavigationRoutes.SearchBarList) {
            SearchScreen(
                navController = navController,
            )
        }
        composable(
            route = NavigationRoutes.ChatWithUser,
            arguments = listOf(navArgument("chatId") { type = NavType.StringType })

        ) { backStackEntry ->

            val chatId = backStackEntry.arguments?.getString("chatId")
            val selectedChat = DataSource().loadChats().find { it.jid == chatId }

            selectedChat?.let {
                ChatWithUser(
                    navController = navController,
                    onSendClick = { message: Message ->
                        viewModel.sendMessage(
                            chatId = message.to,
                            message = message
                        )
                    },
                    chatUiState = chatUiState,
                    selected = { chatId: String ->
                        viewModel.loadMessagesForChat(chatId)
                        viewModel.updateCurrentSelectedChat(chat = selectedChat)
                    },
                    chat = it
                )
            }
            
        }
    }

}


@Composable
fun getCurrentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

@Composable
@Preview(showBackground = true)
fun TheComposeAppPreview() {
    TheComposeApp()
}