package com.ismael.teams.ui.screens

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.ismael.teams.R
import com.ismael.teams.data.local.LocalLoggedAccounts
import com.ismael.teams.ui.components.SideNavBarItems
import com.ismael.teams.ui.components.TeamsBottomNavigationBar
import com.ismael.teams.ui.components.TeamsTopAppBar
import com.ismael.teams.ui.components.TheComposeNavigationRail
import com.ismael.teams.ui.components.TopBarDropdownMenu
import com.ismael.teams.ui.screens.chat.ChatUiState
import com.ismael.teams.ui.screens.user.UserUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

enum class TeamsScreen(
    @StringRes val title: Int
) {
    CHAT(title = R.string.chat),
    ACTIVITY(title = R.string.activity),
    CALENDAR(title = R.string.calendar),
    CALL(title = R.string.calls),
    TEAMS(title = R.string.teams),
    SEARCHBAR(title = R.string.search),
    MORE(title = R.string.more),
    ChatWithUser(title = R.string.ChatWithUser)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamsScreen(
    navController: NavController,
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    chatUiState: ChatUiState,
    userUiState: UserUiState,
    scope: CoroutineScope = rememberCoroutineScope(),
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SideNavBarItems(
                loggedUser = LocalLoggedAccounts.account,
                userUiState = userUiState,
                navController = navController
            )
        }
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                TeamsTopAppBar(
                    currentScreen = TeamsScreen.TEAMS,
                    onFilterClick = {},
                    scrollBehavior = topAppBarScrollBehavior,
                    onSearchBarClick = {
                        navController.navigate(TeamsScreen.SEARCHBAR.name)
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
                    currentScreen = TeamsScreen.TEAMS,
                    unReadMessages = chatUiState.unReadMessages,
                    navController = navController
                )
            }
        ) {
            TopBarDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = !expanded },
                currentScreen = TeamsScreen.TEAMS,
            )
        }
    }
}


@Composable
fun MediumTeamsScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    TheComposeNavigationRail(
        currentScreen = TeamsScreen.TEAMS,
        navController = navController,
        modifier = modifier
    )
}

@Composable
fun ExpandedTeamsScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    TheComposeNavigationRail(
        currentScreen = TeamsScreen.TEAMS,
        navController = navController,
        modifier = modifier
    )
}