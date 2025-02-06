package com.ismael.thecompose.ui.screens

import android.annotation.SuppressLint
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.ismael.thecompose.R
import com.ismael.thecompose.data.local.LocalLoggedAccounts
import com.ismael.thecompose.data.model.NavigationRoutes
import com.ismael.thecompose.ui.components.SideNavBarItems
import com.ismael.thecompose.ui.components.TeamsBottomNavigationBar
import com.ismael.thecompose.ui.components.TeamsTopAppBar
import com.ismael.thecompose.ui.components.TheComposeNavigationRail
import com.ismael.thecompose.ui.screens.chat.ChatUiState
import com.ismael.thecompose.ui.screens.user.UserUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NewCallActionButton(
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
            painter = painterResource(id = R.drawable.add_call_24px),
            contentDescription = null
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CallScreen(
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    chatUiState: ChatUiState,
    userUiState: UserUiState,
    onNavigate: (String) -> Unit,
    onPresenceClick: (String) -> Unit,
    scope: CoroutineScope = rememberCoroutineScope(),
    modifier: Modifier = Modifier
) {
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SideNavBarItems(
                loggedUser = LocalLoggedAccounts.account,
                userUiState = userUiState,
                onSelectPresence = { presence ->
                    onPresenceClick(presence)
                },
                onNavigate = { route ->
                    onNavigate(route)
                },
            )
        }
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                TeamsTopAppBar(
                    currentScreen = TeamsScreen.CALL,
                    onFilterClick = { },
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
                    }

                )
            },
            bottomBar = {
                TeamsBottomNavigationBar(
                    currentScreen = TeamsScreen.CALL,
                    onNavigationSelected = { route ->
                        onNavigate(route)
                    },
                    unReadMessages = chatUiState.unReadMessages,
                )
            },
            floatingActionButton = {
                NewCallActionButton(
                    onclick = { /*TODO*/ },
                    containerColor = MaterialTheme.colorScheme.primary,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                )
            }
        ) {

        }
    }
}

@Composable
fun MediumCallScreen(
    modifier: Modifier = Modifier
) {
    TheComposeNavigationRail(
        currentScreen = TeamsScreen.CALL,
        modifier = modifier
    )
}

@Composable
fun ExpandedCallScreen(
    modifier: Modifier = Modifier
) {
    TheComposeNavigationRail(
        currentScreen = TeamsScreen.CALL,
        modifier = modifier
    )

}

@Preview(showBackground = true)
@Composable
fun CallScreenCompactPreview() {
    MaterialTheme {
        CallScreen(
            chatUiState = ChatUiState(),
            userUiState = UserUiState(),
            onNavigate = {},
            onPresenceClick = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 700)
@Composable
fun CallScreenMediumPreview() {
    MaterialTheme {
        CallScreen(
            chatUiState = ChatUiState(),
            userUiState = UserUiState(),
            onNavigate = {},
            onPresenceClick = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 1000)
@Composable
fun CallScreenExpandedPreview() {
    MaterialTheme {
        CallScreen(
            chatUiState = ChatUiState(),
            userUiState = UserUiState(),
            onNavigate = {},
            onPresenceClick = {}
        )
    }
}