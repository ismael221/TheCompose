package com.ismael.thecompose.ui.screens.status

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ismael.thecompose.R

@Composable
fun StatusScreen(
    status: String,
    onStatusChange: (String) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var statusMessage by remember { mutableStateOf(status) }

    Scaffold(
        topBar = {
            StatusTopAppBar(
                onStatusChange = {
                    onStatusChange(statusMessage)
                },
                navController = navController
            )
        },
        modifier = modifier
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            TextField(
                value = statusMessage,
                onValueChange = {
                    statusMessage = it
                },
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth(),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusTopAppBar(
    navController: NavController,
    onStatusChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = "Set status message"
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        navigationIcon = {
            IconButton(
                onClick = {
                    navController.navigateUp()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    onStatusChange()
                    navController.navigateUp()
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.check_24px),
                    contentDescription = null
                )
            }
        },
        modifier = modifier
    )
}


@Composable
@Preview(showBackground = true)
fun StatusScreenPreview() {

}