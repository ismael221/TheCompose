package com.ismael.teams.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ismael.teams.R
import com.ismael.teams.data.local.LocalLoggedAccounts
import com.ismael.teams.ui.components.FilterSwitch
import com.ismael.teams.ui.components.SideNavBarItems
import com.ismael.teams.ui.components.TeamsBottomNavigationBar
import com.ismael.teams.ui.components.TeamsTopAppBar
import com.ismael.teams.ui.components.TheComposeNavigationRail
import com.ismael.teams.ui.components.TopBarDropdownMenu
import com.ismael.teams.ui.screens.chat.ChatUiState
import com.ismael.teams.ui.screens.user.UserUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun ActivityList(
    navController: NavController,
    modifier: Modifier = Modifier
) {

}

@Composable
fun TopActivityFilter(
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        var checked by remember { mutableStateOf(false) }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
        ) {
            FilterSwitch(
                checked = checked,
                onCheckedChange = { checked = it },
                scale = 0.8f,
            )
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = stringResource(R.string.unread_only),
                style = MaterialTheme.typography.bodySmall
            )
        }
        ElevatedButton(
            onClick = { /* do something */ },
            modifier = Modifier
                .height(35.dp)
                .width(130.dp)
                .padding(top = 2.dp, end = 4.dp)
        ) {

            Icon(
                painter = painterResource(R.drawable.filter_list_24px),
                contentDescription = "Localized description"
            )
            Text(
                text = "Filters",
                modifier = Modifier
                    .padding(start = 8.dp)
            )

        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopActivityTopAppBar(
    onFilterClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onFilterListClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var checked by remember { mutableStateOf(false) }
    TopAppBar(
        title = {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = stringResource(R.string.unread_only),
                style = MaterialTheme.typography.bodySmall
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        modifier = modifier,
        navigationIcon = {

            FilterSwitch(
                checked = checked,
                onCheckedChange = { checked = it },
                scale = 0.8f,
            )
        },
        actions = {

            ElevatedButton(
                onClick = { /* do something */ },
                modifier = Modifier
                    .height(35.dp)
                    .width(130.dp)
                    .padding(top = 2.dp, end = 4.dp)
            ) {

                Icon(
                    painter = painterResource(R.drawable.filter_list_24px),
                    contentDescription = "Localized description"
                )
                Text(
                    text = "Filters",
                    modifier = Modifier
                        .padding(start = 8.dp)
                )

            }

        },
        scrollBehavior = scrollBehavior

    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen(
    navController: NavController,
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    chatUiState: ChatUiState,
    userUiState: UserUiState,
    scope: CoroutineScope = rememberCoroutineScope(),
    modifier: Modifier = Modifier
) {
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val secondaryAppBarScrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    var expanded by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SideNavBarItems(
                navController = navController,
                userUiState = userUiState,
                loggedUser = LocalLoggedAccounts.account,
            )
        }
    ) {
        Scaffold(
            modifier = modifier
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
                .nestedScroll(secondaryAppBarScrollBehavior.nestedScrollConnection),
            topBar = {
                Column(
                    modifier = modifier
                ) {
                    TeamsTopAppBar(
                        currentScreen = TeamsScreen.ActivityList,
                        onFilterClick = { },
                        scrollBehavior = topAppBarScrollBehavior,
                        onSearchBarClick = {
                            navController.navigate(TeamsScreen.SearchBarList.name)
                        },
                        onDropdownMenuClick = {
                            expanded = !expanded
                        },
                        onUserIconClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }
                    )
                    TopActivityTopAppBar(
                        scrollBehavior = secondaryAppBarScrollBehavior,
                        onFilterClick = { /*TODO*/ },
                        onFilterListClick = { /*TODO*/ }
                    )
                }
            },
            bottomBar = {
                TeamsBottomNavigationBar(
                    currentScreen = TeamsScreen.ActivityList,
                    unReadMessages = chatUiState.unReadMessages,
                    navController = navController
                )
            }
        ) {
            ActivityList(
                navController = navController
            )

            TopBarDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = !expanded },
                currentScreen = TeamsScreen.ActivityList,
            )

        }
    }
}


@Composable
fun MediumActivityScreen(
    navController: NavController,
    modifier: Modifier = Modifier
){
    TheComposeNavigationRail(
        currentScreen = TeamsScreen.ActivityList,
        navController = navController
    )
}

@Composable
fun ExpandedActivityScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    TheComposeNavigationRail(
        currentScreen = TeamsScreen.ActivityList,
        navController = navController
    )
}

@Composable
@Preview(showBackground = true)
fun ActivityScreenPreview(
    modifier: Modifier = Modifier
) {
    TopActivityFilter()
}