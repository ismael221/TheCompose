package com.ismael.thecompose.ui.screens


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun SearchInputArea(
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    TextField(
        value = text,
        label = { Text(text = "Search") },
        onValueChange = { text = it },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.DarkGray, focusedContainerColor = Color.DarkGray
        ),
        modifier = modifier
            .focusRequester(focusRequester)
            .fillMaxWidth()
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopAppbar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            SearchInputArea()
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    onBackClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            Color.DarkGray
        ),
        modifier = modifier
            .padding(8.dp)
            .clip(Shapes().small)
            .fillMaxWidth()
    )

}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            SearchTopAppbar(
                onBackClick = {
                    onBackClick()
                }
            )
        },
        content = {

        },
        modifier = modifier

    )


}


@Composable
@Preview(showBackground = true)
fun SearchTopAppbarPreview() {
    SearchScreen(
        onBackClick = {}
    )
}