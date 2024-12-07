package com.ismael.teams.ui

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ismael.teams.R
import com.ismael.teams.data.DataSource
import com.ismael.teams.ui.utils.TeamsScreen


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
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TeamsTopAppBar(
                currentScreen = currentScreen,
                onFilterClick = {showBottomSheet = true},
            )
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
                if (showBottomSheet) {
                    ChatFilterBottomSheet(
                        isVisible = showBottomSheet,
                        onDismiss = { showBottomSheet = false }
                    )
                }
            }
            composable(route = TeamsScreen.ActivityList.name){
                ActivityList()
            }
            composable(route = TeamsScreen.CalendarList.name){

            }
            composable(route = TeamsScreen.CallList.name){

            }
            composable(route = TeamsScreen.TeamsList.name){

            }
        }
        
    }
}



@Composable
@Preview(showBackground = true)
fun TeamsAppPreview(){
    TeamsApp()
}