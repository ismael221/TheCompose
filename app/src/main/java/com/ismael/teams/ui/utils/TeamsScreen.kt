package com.ismael.teams.ui.utils

import androidx.annotation.StringRes
import com.ismael.teams.R

enum class TeamsScreen(
    @StringRes val title: Int
) {
    ChatList(title = R.string.chat),
    ActivityList(title = R.string.activity),
    CalendarList(title = R.string.calendar),
    CallList(title = R.string.calls),
    TeamsList(title = R.string.teams),
    SearchBarList(title = R.string.search),
    More(title = R.string.more),
    ChatWithUser(title = R.string.ChatWithUser)
}