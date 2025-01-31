package com.ismael.thecompose.ui.screens.chat

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ismael.thecompose.R
import com.ismael.thecompose.data.local.LocalChatsDataProvider
import com.ismael.thecompose.data.model.ChatType
import com.ismael.thecompose.data.model.NavigationRoutes
import com.ismael.thecompose.data.model.User
import com.ismael.thecompose.data.model.UserChat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewChatTopAppBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = "New Chat",
                color = Color.White,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                style = MaterialTheme.typography.titleMedium
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    tint = Color.White,
                    contentDescription = "Localized description"
                )
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            Color.DarkGray
        ),
        modifier = modifier
            .fillMaxWidth()
    )
}

@Composable
fun SearchInputArea(
    modifier: Modifier = Modifier
) {
    var content by remember { mutableStateOf("") }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "To:"
        )
        TextField(
            value = content,
            label = {
                Text(
                    text = "Enter name, phone number, or email",
                    fontStyle = MaterialTheme.typography.displaySmall.fontStyle
                )
            },
            onValueChange = { content = it },
            modifier = Modifier
                .padding(start = 4.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun SuggestionsList(
    navController: NavController,
    suggestions: List<User>,
    modifier: Modifier = Modifier
) {
    val suggestionsListState = rememberLazyListState()

    LazyColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        state = suggestionsListState,
        modifier = modifier
    ) {
        items(
            items = suggestions,
            key = { it.jid }
        ) { suggestion ->
            UserCard(
                user = suggestion,
                navController = navController,
                addChatIfNotExists = { addChatIfNotExists(suggestion) },
            )
            HorizontalDivider(
                modifier = Modifier
                    .padding(start = 70.dp, end = 0.dp)
                    .fillMaxWidth(),
                thickness = 0.8.dp
            )
        }
    }

}

@Composable
fun NewChatScreen(
    navController: NavController,
    suggestions: List<User>,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            NewChatTopAppBar(
                navController = navController
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            SearchInputArea(
                modifier = Modifier
            )
            Text(
                text = "Suggestions",
                fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                modifier = Modifier
                    .padding(8.dp)
            )
            SuggestionsList(
                navController = navController,
                suggestions = suggestions,
                modifier = Modifier
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserCard(
    user: User,
    navController: NavController,
    addChatIfNotExists: (User) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .combinedClickable(
                onClick = {
                    addChatIfNotExists(user)
                    navController.navigate("${NavigationRoutes.CHATWITHUSER.substringBefore("/{chatId}")}/${user.jid}")
                }
            )
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        UserIconWithStatus(
            status = "available",
            userProfile = ImageBitmap.imageResource(R.drawable.yasmin),
        )
        Text(
            text = user.displayName,
            modifier = Modifier
                .padding(start = 16.dp)
        )
    }
}

fun addChatIfNotExists(user: User) {
    val existingChat = LocalChatsDataProvider.chats.find { it.jid == user.jid }
    if (existingChat == null) {
        // Adicionar o chat à lista de chats
        val newChat = UserChat(
            jid = user.jid,
            lastMessage = "",
            lastMessageTime = 0,
            chatName = user.displayName,
            chatPhotoUrl = "",
            isUnread = false,
            chatType = ChatType.User,
            lastSeen = 0
        )
        LocalChatsDataProvider.chats.add(newChat)
        println(LocalChatsDataProvider.chats.toString())

    }
}

@Composable
@Preview(showBackground = true)
fun NewChatTopAppBarPreview() {
    MaterialTheme(
        darkColorScheme()
    ) {

    }
}