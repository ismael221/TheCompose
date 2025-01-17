package com.ismael.teams.ui.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun Long.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

fun Long.toFormattedDateString(): String {
    val messageDateTime = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDateTime()
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)
    val formatterTime = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault())
    val formatterDate = DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.getDefault())
    val formatterDay = DateTimeFormatter.ofPattern("EEEE", Locale.getDefault())

    return when {
        messageDateTime.toLocalDate().isEqual(today) -> {
            "Today, ${messageDateTime.format(formatterTime)}"
        }
        messageDateTime.toLocalDate().isEqual(yesterday) -> {
            "Yesterday, ${messageDateTime.format(formatterTime)}"
        }
        messageDateTime.isAfter(today.minusDays(7).atStartOfDay(ZoneId.systemDefault()).toLocalDateTime()) -> {
            "${messageDateTime.format(formatterDay)}, ${messageDateTime.format(formatterTime)}"
        }
        else -> {
            "${messageDateTime.format(formatterDate)}, ${messageDateTime.format(formatterTime)}"
        }
    }
}