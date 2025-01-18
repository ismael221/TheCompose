package com.ismael.teams.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ui.theme.AppTypography
import com.ismael.teams.R
import com.ismael.teams.ui.screens.chat.UserIcon
import com.ismael.teams.ui.screens.TeamsScreen


@Composable
fun TeamsBottomNavigationBar(
    modifier: Modifier = Modifier,
    unReadMessages: Int,
    currentScreen: TeamsScreen,
    navController: NavController,
) {
    NavigationBar(
        modifier = modifier
    ) {
        var itemCount by remember { mutableIntStateOf(5) }


        NavigationBarItem(
            selected = currentScreen == TeamsScreen.ACTIVITY,
            onClick = { navController.navigate(TeamsScreen.ACTIVITY.name) },
            icon = {
                BadgedBox(
                    badge = {
                        if (itemCount > 0) {
                            Badge(
                                containerColor = Color.Red,
                                contentColor = Color.White
                            ) {
                                Text(
                                    text = itemCount.toString()
                                )
                            }
                        }
                    }
                ) {
                    Icon(
                        painter = if (currentScreen == TeamsScreen.ACTIVITY) painterResource(R.drawable.notifications_filled) else painterResource(
                            R.drawable.notifications_24px
                        ),
                        contentDescription = null
                    )
                }
            },
            label = {
                Text(
                    text = "Activity",
                    style = AppTypography.bodySmall
                )
            }
        )
        NavigationBarItem(
            selected = currentScreen == TeamsScreen.CHAT,
            onClick = { navController.navigate(TeamsScreen.CHAT.name) },
            icon = {
                BadgedBox(
                    badge = {
                        if (unReadMessages > 0) {
                            Badge(
                                containerColor = Color.Red,
                                contentColor = Color.White
                            ) {
                                Text(
                                    text = unReadMessages.toString()
                                )
                            }
                        }
                    }
                ) {
                    Icon(
                        painter = if (currentScreen == TeamsScreen.CHAT) painterResource(R.drawable.chat_filled) else painterResource(
                            R.drawable.chat_24px
                        ),
                        contentDescription = null
                    )
                }
            },
            label = {
                Text(
                    text = "Chat",
                    style = AppTypography.bodySmall
                )
            }
        )
        NavigationBarItem(
            selected = currentScreen == TeamsScreen.CALENDAR,
            onClick = { navController.navigate(TeamsScreen.CALENDAR.name) },
            icon = {
                Icon(
                    painter = if (currentScreen == TeamsScreen.CALENDAR) painterResource(R.drawable.calendar_month_filled) else painterResource(
                        R.drawable.calendar_month_24px
                    ),
                    contentDescription = null
                )
            },
            label = {
                Text(
                    text = "Calendar",
                    style = AppTypography.bodySmall
                )
            }
        )
        NavigationBarItem(
            selected = currentScreen == TeamsScreen.TEAMS,
            onClick = { navController.navigate(TeamsScreen.TEAMS.name) },
            icon = {
                Icon(
                    painter = if (currentScreen == TeamsScreen.TEAMS) painterResource(R.drawable.groups_filled) else painterResource(
                        R.drawable.groups_24px
                    ),
                    contentDescription = null
                )
            },
            label = {
                Text(
                    text = "Teams",
                    style = AppTypography.bodySmall
                )
            }
        )
        NavigationBarItem(
            selected = currentScreen == TeamsScreen.CALL,
            onClick = { navController.navigate(TeamsScreen.CALL.name) },
            icon = {
                Icon(
                    painter = if (currentScreen == TeamsScreen.CALL) painterResource(R.drawable.call_filled_24px) else painterResource(
                        R.drawable.call_24px
                    ),
                    contentDescription = null
                )
            },
            label = {
                Text(
                    text = "Calls",
                    style = AppTypography.bodySmall
                )
            }
        )
        NavigationBarItem(
            selected = currentScreen == TeamsScreen.MORE,
            onClick = { navController.navigate(TeamsScreen.MORE.name) },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.more_horiz_24px),
                    contentDescription = null
                )
            },
            label = {
                Text(
                    text = "MORE",
                    style = AppTypography.bodySmall
                )
            }
        )

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamsTopAppBar(
    currentScreen: TeamsScreen,
    onFilterClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onUserIconClick: () -> Unit,
    onSearchBarClick: () -> Unit,
    onDropdownMenuClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var showSearch by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = stringResource(currentScreen.title)

            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        modifier = modifier,
        navigationIcon = {

            UserIcon(
                painter = painterResource(R.drawable.perfil),
                contentDescription = null,
                onclick = { onUserIconClick() },
                modifier = Modifier
                    .padding(start = 8.dp)
            )
        },
        actions = {
            if (currentScreen == TeamsScreen.CHAT) {
                IconButton(onClick = { onFilterClick() }) {
                    Icon(
                        painter = painterResource(R.drawable.filter_list_24px),
                        contentDescription = "Localized description"
                    )
                }
            }
            if (currentScreen == TeamsScreen.CALL) {
                IconButton(onClick = { /* do something */ }) {
                    Icon(
                        painter = painterResource(R.drawable.voicemail_24px),
                        contentDescription = "Localized description"
                    )
                }

            }
            if (currentScreen == TeamsScreen.CALENDAR) {
                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.videocam_24px),
                        contentDescription = "Localized description"
                    )
                }
            }
            IconButton(
                onClick = { onSearchBarClick() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Localized description"
                )
            }
            if (currentScreen != TeamsScreen.CALL && currentScreen != TeamsScreen.CALENDAR) {
                IconButton(onClick = { onDropdownMenuClick() }) {
                    Icon(
                        painter = painterResource(R.drawable.more_vert_24px),
                        contentDescription = "Localized description"
                    )
                }
            }

        },
        scrollBehavior = scrollBehavior
    )
}


@Composable
fun FilterSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    scale: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale
            )
    ) {
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier
                .size(40.dp)
                .padding(start = 8.dp)
        )
    }
}

@Composable
fun TopBarDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    currentScreen: TeamsScreen,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
            .padding(8.dp)

    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onDismissRequest() }
        ) {
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.eyeglasses_24px),
                        contentDescription = null
                    )
                },
                text = { Text("Mark all as read") },
                onClick = {}
            )
            if (currentScreen == TeamsScreen.ACTIVITY) {
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.swipe_right_alt_24px),
                            contentDescription = null
                        )
                    },
                    text = { Text("Swipe options") },
                    onClick = {}
                )
            }
        }
    }
}

@Composable
fun TheComposeNavigationRail(
    currentScreen: TeamsScreen,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        modifier = modifier
    ) {
        NavigationRailItem(
            selected = currentScreen == TeamsScreen.ACTIVITY,
            label = {
                Text(
                    text = "Activity",
                    style = AppTypography.bodySmall
                )
            },
            onClick = { navController.navigate(TeamsScreen.ACTIVITY.name) },
            icon = {
                Icon(
                    painter = if (currentScreen == TeamsScreen.ACTIVITY) painterResource(R.drawable.notifications_filled) else painterResource(
                        R.drawable.notifications_24px
                    ),
                    contentDescription = null
                )
            }
        )
        NavigationRailItem(
            selected = currentScreen == TeamsScreen.CHAT,
            label = {
                Text(
                    text = "Chat",
                    style = AppTypography.bodySmall
                )
            },
            onClick = { navController.navigate(TeamsScreen.CHAT.name) },
            icon = {
                Icon(
                    painter = if (currentScreen == TeamsScreen.CHAT) painterResource(R.drawable.chat_filled) else painterResource(
                        R.drawable.chat_24px
                    ),
                    contentDescription = null
                )
            }
        )
        NavigationRailItem(
            selected = currentScreen == TeamsScreen.CALENDAR,
            label = {
                Text(
                    text = "Calendar",
                    style = AppTypography.bodySmall
                )
            },
            onClick = { navController.navigate(TeamsScreen.CALENDAR.name) },
            icon = {
                Icon(
                    painter = if (currentScreen == TeamsScreen.CALENDAR) painterResource(R.drawable.calendar_month_filled) else painterResource(
                        R.drawable.calendar_month_24px
                    ),
                    contentDescription = null
                )
            }
        )
        NavigationRailItem(
            selected = currentScreen == TeamsScreen.TEAMS,
            label = {
                Text(
                    text = "Teams",
                    style = AppTypography.bodySmall
                )
            },
            onClick = { navController.navigate(TeamsScreen.TEAMS.name) },
            icon = {
                Icon(
                    painter = if (currentScreen == TeamsScreen.TEAMS) painterResource(R.drawable.groups_filled) else painterResource(
                        R.drawable.groups_24px
                    ),
                    contentDescription = null
                )
            }
        )
        NavigationRailItem(
            selected = currentScreen == TeamsScreen.CALL,
            label = {
                Text(
                    text = "Calls",
                    style = AppTypography.bodySmall
                )
            },
            onClick = { navController.navigate(TeamsScreen.CALL.name) },
            icon = {
                Icon(
                    painter = if (currentScreen == TeamsScreen.CALL) painterResource(R.drawable.call_filled_24px) else painterResource(
                        R.drawable.call_24px
                    ),
                    contentDescription = null
                )
            }
        )
        NavigationRailItem(
            selected = currentScreen == TeamsScreen.MORE,
            label = {
                Text(
                    text = "MORE",
                    style = AppTypography.bodySmall
                )
            },
            onClick = { },
            icon = {
                Icon(
                    painter = if (currentScreen == TeamsScreen.MORE) painterResource(R.drawable.more_horiz_24px) else painterResource(
                        R.drawable.more_horiz_24px
                    ),
                    contentDescription = null
                )
            }
        )
    }

}

@Preview
@Composable
private fun TeamsHomeScreenPreview() {
//    TheComposeNavigationRail(
//        currentScreen = TeamsScreen.CHAT
//    )
}