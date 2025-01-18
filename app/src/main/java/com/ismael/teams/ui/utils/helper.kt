package com.ismael.teams.ui.utils

fun removeAfterSlash(input: String): String {
    return input.substringBefore("/")
}