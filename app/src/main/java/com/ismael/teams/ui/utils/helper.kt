package com.ismael.teams.ui.utils

import android.util.Log
import com.ismael.teams.data.model.Message

fun removeAfterSlash(input: String): String {
    return input.substringBefore("/")
}

fun addMessageToMap(
    map: MutableMap<String, List<Message>>,
    key: String,
    message: Message
) {
    Log.i("Mensagem adicionada", key)
    map.getOrPut(key) { emptyList() }
        .let { it + message }
        .also { map[key] = it }

}