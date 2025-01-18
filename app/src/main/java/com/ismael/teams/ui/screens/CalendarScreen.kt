package com.ismael.teams.ui.screens

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.navigation.NavController
import com.ismael.teams.data.local.LocalLoggedAccounts
import com.ismael.teams.ui.components.SideNavBarItems
import com.ismael.teams.ui.components.TeamsBottomNavigationBar
import com.ismael.teams.ui.components.TeamsTopAppBar
import com.ismael.teams.ui.components.TheComposeNavigationRail
import com.ismael.teams.ui.screens.chat.ChatUiState
import com.ismael.teams.ui.screens.user.UserUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NewCalendarEventActionButton(
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
            imageVector = Icons.Default.Add,
            contentDescription = null
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CalendarScreen(
    navController: NavController,
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    chatUiState: ChatUiState,
    userUiState: UserUiState,
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
                navController = navController
            )
        }
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                TeamsTopAppBar(
                    currentScreen = TeamsScreen.CALENDAR,
                    onFilterClick = { },
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
                    }
                )
            },
            bottomBar = {
                TeamsBottomNavigationBar(
                    currentScreen = TeamsScreen.CALENDAR,
                    navController = navController,
                    unReadMessages = chatUiState.unReadMessages,
                    modifier = modifier
                )
            },
            floatingActionButton = {
                NewCalendarEventActionButton(
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
fun MediumCalendarScreen(
    navController: NavController,
    modifier: Modifier = Modifier
){
    TheComposeNavigationRail(
        currentScreen = TeamsScreen.CALENDAR,
        navController = navController,
        modifier = modifier
    )
}
@Composable
fun ExpandedCalendarScreen(
    navController: NavController,
    modifier: Modifier = Modifier
){
    TheComposeNavigationRail(
        currentScreen = TeamsScreen.CALENDAR,
        navController = navController,
        modifier = modifier
    )
}