package com.ismael.teams

import android.annotation.SuppressLint
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ismael.teams.ui.ActivityScreen
import com.ismael.teams.ui.CalendarScreen
import com.ismael.teams.ui.CallScreen
import com.ismael.teams.ui.ChatScreen
import com.ismael.teams.ui.SearchScreen
import com.ismael.teams.ui.TeamsScreen
import com.ismael.teams.ui.utils.TeamsScreen


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TheComposeApp(
    modifier: Modifier = Modifier
) {
    val navController: NavHostController = rememberNavController()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = TeamsScreen.valueOf(
        backStackEntry?.destination?.route ?: TeamsScreen.ChatList.name
    )
    var showBottomSheet by remember { mutableStateOf(false) }

    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    //Todo Create diffrent TopBars for Each Screen

    NavHost(
        navController = navController,
        startDestination = TeamsScreen.ChatList.name,
    ) {
        composable(route = TeamsScreen.ChatList.name) {
            ChatScreen(
                navController = navController,
                drawerState = drawerState,
                scope = scope
            )
        }
        composable(route = TeamsScreen.ActivityList.name) {
            ActivityScreen(
                navController = navController,
                drawerState = drawerState,
                scope = scope
            )
        }
        composable(route = TeamsScreen.CalendarList.name) {
            CalendarScreen(
                navController = navController,
                drawerState = drawerState,
                scope = scope
            )
        }
        composable(route = TeamsScreen.CallList.name) {
            CallScreen(
                navController = navController,
                drawerState = drawerState,
                scope = scope
            )
        }
        composable(route = TeamsScreen.TeamsList.name) {
            TeamsScreen(
                navController = navController,
                drawerState = drawerState,
                scope = scope
            )
        }
        composable(route = TeamsScreen.SearchBarList.name) {
            SearchScreen()
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