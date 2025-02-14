package com.ismael.thecompose.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.ismael.thecompose.ui.components.TeamsBottomNavigationBar
import com.ismael.thecompose.ui.components.TheComposeNavigationRail
import com.ismael.thecompose.ui.screens.chat.ChatUiState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreCompactScreen(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onNavigate: (String) -> Unit,
    chatUiState: ChatUiState,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState()
    var expanded by remember { mutableStateOf(false) }
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            ),
            modifier = modifier
        ) {
            Scaffold(
                bottomBar = {
                    TeamsBottomNavigationBar(
                        currentScreen = TeamsScreen.MORE,
                        unReadMessages = chatUiState.unReadMessages,
                        onNavigationSelected = { route ->
                          onNavigate(route)
                        }
                    )
                }
            ) { innerPadding ->

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding)
                ) {

                }
            }
        }
    }
}


@Composable
fun MediumMoreScreen(
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        TheComposeNavigationRail(
            currentScreen = TeamsScreen.MORE,
            onNavigationSelected = {
              onNavigate(it)
            },
            modifier = Modifier
        )
    }
}

@Composable
fun ExpandedMoreScreen(
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        TheComposeNavigationRail(
            currentScreen = TeamsScreen.MORE,
            onNavigationSelected = {
                onNavigate(it)
            },
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MoreScreenCompactPreview() {
    MaterialTheme {
        MoreCompactScreen(
            isVisible = true,
            onDismiss = {},
            chatUiState = ChatUiState(),
            onNavigate = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 700)
@Composable
fun MoreScreenMediumPreview() {
    MaterialTheme {
        MediumMoreScreen(
            onNavigate = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 1000)
@Composable
fun MoreScreenExpandedPreview() {
    MaterialTheme {
        ExpandedMoreScreen(
            onNavigate = {}
        )
    }
}