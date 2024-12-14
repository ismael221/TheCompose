package com.ismael.teams.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ismael.teams.R
import com.ismael.teams.data.DataSource
import com.ismael.teams.ui.utils.TeamsScreen


@Composable
fun ActivityList(
    modifier: Modifier = Modifier
){

    ChatList(
        DataSource().loadChats()
    )
}

@Composable
fun TopActivityFilter(
    modifier: Modifier = Modifier
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        var checked by remember { mutableStateOf(false) }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
        ) {
            FilterSwitch(
                checked = checked,
                onCheckedChange = { checked = it },
                scale = 0.8f,
            )
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = stringResource(R.string.unread_only),
                style = MaterialTheme.typography.bodySmall
            )
        }
        ElevatedButton(
            onClick = { /* do something */ },
            modifier = Modifier
                .height(35.dp)
                .width(130.dp)
                .padding(top = 2.dp, end = 4.dp)
        ) {

            Icon(
                painter = painterResource(R.drawable.filter_list_24px),
                contentDescription = "Localized description"
            )
            Text(
                text = "Filters",
                modifier = Modifier
                    .padding(start = 8.dp)
            )

        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopActivityTopAppBar(
    currentScreen: TeamsScreen,
    onFilterClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onUserIconClick: () -> Unit,
    modifier: Modifier = Modifier,
){
    var checked by remember { mutableStateOf(false) }
    TopAppBar(
        title = {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = stringResource(R.string.unread_only),
                style = MaterialTheme.typography.bodySmall
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {

            FilterSwitch(
                checked = checked,
                onCheckedChange = { checked = it },
                scale = 0.8f,
            )
        },
        actions = {

            ElevatedButton(
                onClick = { /* do something */ },
                modifier = Modifier
                    .height(35.dp)
                    .width(130.dp)
                    .padding(top = 2.dp, end = 4.dp)
            ) {

                Icon(
                    painter = painterResource(R.drawable.filter_list_24px),
                    contentDescription = "Localized description"
                )
                Text(
                    text = "Filters",
                    modifier = Modifier
                        .padding(start = 8.dp)
                )

            }

        },
        scrollBehavior = scrollBehavior

    )
}

@Composable
@Preview(showBackground = true)
fun ActivityScreenPreview(
    modifier: Modifier = Modifier
){
    TopActivityFilter()
}