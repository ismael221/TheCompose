package com.ismael.teams.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.ismael.teams.ui.utils.TeamsScreen

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

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = { onDismiss() },
            sheetState = sheetState,
        ) {
            Column(modifier = modifier) {
                Text(text = "More")
                TeamsBottomNavigationBar(
                    currentScreen = TeamsScreen.More,
                    navController = navController
                )
            }
        }
    }

}