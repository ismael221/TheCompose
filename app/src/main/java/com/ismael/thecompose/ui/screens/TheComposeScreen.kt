package com.ismael.thecompose.ui.screens

import android.annotation.SuppressLint

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ismael.thecompose.ui.navigation.TheComposeNavHost
import com.ismael.thecompose.ui.screens.chat.ChatViewModel
import com.ismael.thecompose.ui.screens.chat.UserSearchViewModel
import com.ismael.thecompose.ui.screens.user.UserViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TheComposeApp(
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    val chatViewModel: ChatViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()
    val searchViewModel: UserSearchViewModel = viewModel()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        chatViewModel.setContext(context)
        chatViewModel.observeIncomingMessages()
    }

    val chatUiState = chatViewModel.uiState.collectAsState().value
    val userUiState = userViewModel.uiState.collectAsState().value
    val searchUiState = searchViewModel.uiState.collectAsState().value

    val navController: NavHostController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    TheComposeNavHost(
        navController = navController,
        scope = scope,
        drawerState = drawerState,
        windowSize = windowSize,
        chatUiState = chatUiState,
        userUiState = userUiState,
        searchUiState = searchUiState,
        userViewModel = userViewModel,
        chatViewModel = chatViewModel,
        modifier = modifier
    )


}


@Preview(showBackground = true)
@Composable
fun TeamsAppCompactPreview() {
    MaterialTheme {
        Surface {
            TheComposeApp(
                windowSize = WindowWidthSizeClass.Compact
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 700)
@Composable
fun TeamsAppMediumPreview() {
    MaterialTheme {
        Surface {
            TheComposeApp(
                windowSize = WindowWidthSizeClass.Medium
            )
        }
    }
}


@Preview(showBackground = true, widthDp = 1000)
@Composable
fun TeamsAppExpandedPreview() {
    MaterialTheme {
        Surface {
            TheComposeApp(
                windowSize = WindowWidthSizeClass.Expanded
            )
        }
    }
}