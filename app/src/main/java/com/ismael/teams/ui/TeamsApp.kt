package com.ismael.teams.ui

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ismael.teams.R
import com.ismael.teams.data.DataSource
import com.ismael.teams.ui.utils.TeamsScreen


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TeamsApp(
    modifier: Modifier = Modifier
) {
    val navController: NavHostController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = TeamsScreen.valueOf(
        backStackEntry?.destination?.route ?: TeamsScreen.ChatList.name
    )
    var showBottomSheet by remember { mutableStateOf(false) }

    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val secondaryAppBarScrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier
            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
            .nestedScroll(secondaryAppBarScrollBehavior.nestedScrollConnection),

        topBar = {
            TeamsTopAppBar(
                currentScreen = currentScreen,
                onFilterClick = { showBottomSheet = true },
                scrollBehavior = topAppBarScrollBehavior,
                )
            Spacer(
                modifier.height(900.dp)
            )
            if (currentScreen == TeamsScreen.ActivityList) {
                Column {
                    TeamsTopAppBar(
                        currentScreen = currentScreen,
                        onFilterClick = { showBottomSheet = true },
                        scrollBehavior = topAppBarScrollBehavior,
                    )
                    FiltersTopAppBar(
                        onFilterClick = { showBottomSheet = true },
                        currentScreen = currentScreen,
                        scrollBehavior = secondaryAppBarScrollBehavior
                    )
                }
            }
        },
        bottomBar = {
            TeamsBottomNavigationBar(
                currentScreen = currentScreen,
                navController = navController
            )
        },
        floatingActionButton = {
            NewChatFloatingActionButton(
                onclick = {},
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
            )
        },
    ) {

        NavHost(
            navController = navController,
            startDestination = TeamsScreen.ChatList.name,
        ) {
            composable(route = TeamsScreen.ChatList.name) {
                ChatList(
                    postList = DataSource().loadChats()
                )
                if (showBottomSheet) {
                    ChatFilterBottomSheet(
                        isVisible = showBottomSheet,
                        onDismiss = { showBottomSheet = false }
                    )
                }
            }
            composable(route = TeamsScreen.ActivityList.name) {
                ActivityList()
            }
            composable(route = TeamsScreen.CalendarList.name) {

            }
            composable(route = TeamsScreen.CallList.name) {

            }
            composable(route = TeamsScreen.TeamsList.name) {

            }
        }

    }
}


@Composable
@Preview(showBackground = true)
fun TeamsAppPreview() {
    TeamsApp()
}