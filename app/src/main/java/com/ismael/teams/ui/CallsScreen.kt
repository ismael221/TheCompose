package com.ismael.teams.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.ismael.teams.R

@Composable
fun NewCallActionButton(
    onclick: () -> Unit,
    containerColor: Color,
    elevation: FloatingActionButtonElevation,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onclick,
        containerColor = containerColor,
        elevation = elevation,

        ) {
        Icon(
            painter = painterResource(id = R.drawable.add_call_24px),
            contentDescription = null
        )
    }
}