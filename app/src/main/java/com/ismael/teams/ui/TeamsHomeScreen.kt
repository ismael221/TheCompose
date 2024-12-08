package com.ismael.teams.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ismael.teams.R
import com.ismael.teams.ui.utils.TeamsScreen


@Composable
fun TeamsBottomNavigationBar(
    modifier: Modifier = Modifier,
    currentScreen: TeamsScreen,
    navController: NavController,
) {
    NavigationBar(
        modifier = modifier
    ) {
        var selected = false
        var itemCount by remember { mutableIntStateOf(5) }


        NavigationBarItem(
            selected = currentScreen == TeamsScreen.ActivityList,
            onClick = { navController.navigate(TeamsScreen.ActivityList.name) },
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
                        painter = if (currentScreen == TeamsScreen.ActivityList) painterResource(R.drawable.notifications_filled) else painterResource(
                            R.drawable.notifications_24px
                        ),
                        contentDescription = null
                    )
                }
            },
            label = {
                Text(text = "Activity")
            }
        )
        NavigationBarItem(
            selected = currentScreen == TeamsScreen.ChatList,
            onClick = { navController.navigate(TeamsScreen.ChatList.name) },
            icon = {
                Icon(
                    painter = if (currentScreen == TeamsScreen.ChatList) painterResource(R.drawable.chat_filled) else painterResource(
                        R.drawable.chat_24px
                    ),
                    contentDescription = null
                )
            },
            label = {
                Text(text = "Chat")
            }
        )
        NavigationBarItem(
            selected = currentScreen == TeamsScreen.CalendarList,
            onClick = { navController.navigate(TeamsScreen.CalendarList.name) },
            icon = {
                Icon(
                    painter = if (currentScreen == TeamsScreen.CalendarList) painterResource(R.drawable.calendar_month_filled) else painterResource(
                        R.drawable.calendar_month_24px
                    ),
                    contentDescription = null
                )
            },
            label = {
                Text(text = "Calendar")
            }
        )
        NavigationBarItem(
            selected = currentScreen == TeamsScreen.TeamsList,
            onClick = { navController.navigate(TeamsScreen.TeamsList.name) },
            icon = {
                Icon(
                    painter = if (currentScreen == TeamsScreen.TeamsList) painterResource(R.drawable.groups_filled) else painterResource(
                        R.drawable.groups_24px
                    ),
                    contentDescription = null
                )
            },
            label = {
                Text(text = "Teams")
            }
        )
        NavigationBarItem(
            selected = currentScreen == TeamsScreen.CallList,
            onClick = { navController.navigate(TeamsScreen.CallList.name) },
            icon = {
                Icon(
                    painter = if (currentScreen == TeamsScreen.CallList) painterResource(R.drawable.call_filled_24px) else painterResource(
                        R.drawable.call_24px
                    ),
                    contentDescription = null
                )
            },
            label = {
                Text(text = "Calls")
            }
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.more_horiz_24px),
                    contentDescription = null
                )
            },
            label = {
                Text(text = "More")
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
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = stringResource(currentScreen.title)

            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {

            UserIcon(
                painter = painterResource(R.drawable.perfil),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 8.dp)
            )
        },
        actions = {
            if (currentScreen == TeamsScreen.ChatList) {
                IconButton(onClick = { onFilterClick() }) {
                    Icon(
                        painter = painterResource(R.drawable.filter_list_24px),
                        contentDescription = "Localized description"
                    )
                }
            }
            if (currentScreen == TeamsScreen.CallList) {
                IconButton(onClick = { /* do something */ }) {
                    Icon(
                        painter = painterResource(R.drawable.voicemail_24px),
                        contentDescription = "Localized description"
                    )
                }

            }
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Localized description"
                )
            }
            if (currentScreen != TeamsScreen.CallList) {
                IconButton(onClick = { /* do something */ }) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersTopAppBar(
    onFilterClick: () -> Unit,
    currentScreen: TeamsScreen,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = stringResource(R.string.unread_only),
                style = MaterialTheme.typography.bodySmall
            )
        },
        navigationIcon = {
            var checked by remember { mutableStateOf(false) }

            FilterSwitch(
                checked = checked,
                onCheckedChange = { checked = it },
                scale = 0.8f,
            )

        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun TeamsHomeScreenPreview() {
    TeamsTopAppBar(
        currentScreen = TeamsScreen.ActivityList,
        onFilterClick = {}
    )
}