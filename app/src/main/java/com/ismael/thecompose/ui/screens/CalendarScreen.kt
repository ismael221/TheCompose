package com.ismael.thecompose.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ismael.thecompose.data.local.LocalLoggedAccounts
import com.ismael.thecompose.data.model.NavigationRoutes
import com.ismael.thecompose.ui.components.SideNavBarItems
import com.ismael.thecompose.ui.components.TeamsBottomNavigationBar
import com.ismael.thecompose.ui.components.TeamsTopAppBar
import com.ismael.thecompose.ui.components.TheComposeNavigationRail
import com.ismael.thecompose.ui.screens.chat.ChatUiState
import com.ismael.thecompose.ui.screens.user.UserUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun NewCalendarEventActionButton(
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
            imageVector = Icons.Default.Add,
            contentDescription = null
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CalendarScreen(
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    chatUiState: ChatUiState,
    onNavigate: (String) -> Unit,
    userUiState: UserUiState,
    onPresenceClick: (String) -> Unit,
    scope: CoroutineScope = rememberCoroutineScope(),
    modifier: Modifier = Modifier
) {
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SideNavBarItems(
                loggedUser = LocalLoggedAccounts.account,
                userUiState = userUiState,
                onNavigate = onNavigate,
                onSelectPresence = { presence ->
                    onPresenceClick(presence)
                },
            )
        }
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                TeamsTopAppBar(
                    currentScreen = TeamsScreen.CALENDAR,
                    onFilterClick = { },
                    scrollBehavior = topAppBarScrollBehavior,
                    onSearchBarClick = {
                        onNavigate(NavigationRoutes.SEARCHBAR)
                    },
                    onUserIconClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                )
            },
            bottomBar = {
                TeamsBottomNavigationBar(
                    currentScreen = TeamsScreen.CALENDAR,
                    unReadMessages = chatUiState.unReadMessages,
                    onNavigationSelected = { route ->
                        onNavigate(route)
                    },
                    modifier = modifier
                )
            },
            floatingActionButton = {
                NewCalendarEventActionButton(
                    onclick = { /*TODO*/ },
                    containerColor = MaterialTheme.colorScheme.primary,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                )
            }
        ) { innerPadding ->
            Calendar(
                selectedDate = LocalDate.now(),
                onDateSelected = { /*TODO*/ },
                modifier = Modifier
                    .padding(innerPadding)
            )
        }
    }
}

@Composable
fun MediumCalendarScreen(
    modifier: Modifier = Modifier
) {
    TheComposeNavigationRail(
        currentScreen = TeamsScreen.CALENDAR,
        modifier = modifier
    )
}

@Composable
fun ExpandedCalendarScreen(
    modifier: Modifier = Modifier
) {
    TheComposeNavigationRail(
        currentScreen = TeamsScreen.CALENDAR,
        modifier = modifier
    )
}

@Composable
fun CalendarHeader(month: YearMonth, onPreviousMonth: () -> Unit, onNextMonth: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Previous Month")
        }
        Text(text = month.format(DateTimeFormatter.ofPattern("MMMM yyyy")))
        IconButton(onClick = onNextMonth) {
            Icon(Icons.Default.ArrowForward, contentDescription = "Next Month")
        }
    }
}

@Composable
fun DaysOfWeekHeader(locale: Locale) {
    val daysOfWeek =
        DayOfWeek.values().map { it.getDisplayName(java.time.format.TextStyle.NARROW, locale) }
    Row(horizontalArrangement = Arrangement.SpaceBetween) {
        for (day in daysOfWeek) {
            Text(text = day, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun CalendarGrid(
    month: YearMonth,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    val daysInMonth = month.lengthOfMonth()
    val firstDayOfMonth = month.atDay(1).dayOfWeek.value % 7 // Ajuste para começar no domingo
    val days = (1..daysInMonth).map { month.atDay(it) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7), // 7 colunas
        contentPadding = PaddingValues(4.dp),
    ) {
        items(firstDayOfMonth) {
            Box(modifier = Modifier.aspectRatio(1f)) { /* Espaço vazio */ }
        }

        items(days) { date ->
            val isSelected = date == selectedDate
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .aspectRatio(1f)
                    .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                    .clickable { onDateSelected(date) }
            ) {
                Text(text = date.dayOfMonth.toString())
            }
        }
    }
}

@Composable
fun Calendar(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate? = null,
    onDateSelected: (LocalDate) -> Unit,
    month: YearMonth = YearMonth.now(),
    locale: Locale = Locale.getDefault()
) {
    Column(modifier = modifier) {
        // Cabeçalho com navegação
        CalendarHeader(
            month = month,
            onPreviousMonth = { /* Implementar */ },
            onNextMonth = { /* Implementar */ })

        // Dias da semana
        DaysOfWeekHeader(locale = locale)

        // Grade de datas
        CalendarGrid(
            month = month,
            selectedDate = selectedDate,
            onDateSelected = onDateSelected
        )
    }
}

@Composable
@Preview(showBackground = true)
fun CalendarCompactPreview() {
    MaterialTheme {
        Calendar(
            selectedDate = LocalDate.now(),
            onDateSelected = {}
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 700)
fun CalendarMediumPreview() {
    MaterialTheme {
        Calendar(
            selectedDate = LocalDate.now(),
            onDateSelected = {}
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 1000)
fun CalendarExpandedPreview() {
    MaterialTheme {
        Calendar(
            selectedDate = LocalDate.now(),
            onDateSelected = {}
        )
    }
}