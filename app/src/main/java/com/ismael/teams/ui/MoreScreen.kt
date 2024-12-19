package com.ismael.teams.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.ismael.teams.ui.utils.TeamsScreen
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreScreen(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState()
    var expanded by remember { mutableStateOf(false) }
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    if (isVisible) { // Show ModalBottomSheet only if isVisible is true
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            ),
            //modifier = Modifier.navigationBarsPadding() // Add navigation bar padding
        ) {
            Scaffold(
                bottomBar = {
                    TeamsBottomNavigationBar(
                        currentScreen = TeamsScreen.More,
                        navController = navController
                    )
                }
            ) { innerPadding ->
                // Content of the ModalBottomSheet
                // Use innerPadding to avoid overlapping with the BottomAppBar
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding)
                ) {
                    // ... your content ...
                }
            }
        }
    }
}