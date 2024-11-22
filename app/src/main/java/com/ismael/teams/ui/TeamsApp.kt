package com.ismael.teams.ui

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ismael.teams.R
import com.ismael.teams.data.DataSource


enum class TeamsScreen(
    @StringRes val title: Int
) {
    ChatList(title = R.string.chat),
    ActivityList(title = R.string.activity),
    CalendarList(title = R.string.calendar),
    CallList(title = R.string.calls),
    TeamsList(title = R.string.teams)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TeamsApp(
    modifier: Modifier = Modifier
){
    val navController: NavHostController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = TeamsScreen.valueOf(
        backStackEntry?.destination?.route ?: TeamsScreen.ChatList.name
    )

    Scaffold(
        topBar = {
            TeamsTopAppBar(
                currentScreen = currentScreen
            )
        },
        bottomBar = {
            TeamsBottomNavigationBar(
                currentScreen = currentScreen,
            )
        },
        floatingActionButton = {
            NewChatFloatingActionButton(
                onclick = {},
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
            )
        }
    ) {

        NavHost(
            navController = navController,
            startDestination = TeamsScreen.ChatList.name,
        ){
            composable(route = TeamsScreen.ChatList.name){
                ChatList(
                    postList = DataSource().loadChats()
                )
            }
        }
        
    }
}



@Composable
@Preview(showBackground = true)
fun TeamsAppPreview(){
    TeamsApp()
}