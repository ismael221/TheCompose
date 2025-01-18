package com.ismael.teams.ui.utils


import android.util.Log
import com.ismael.teams.data.model.Message
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.net.HttpURLConnection
import java.net.URL
import kotlin.random.Random


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

fun createInitialsBitmap(userName: String): ImageBitmap {
    val initials = userName.firstOrNull()?.toString()?.uppercase() ?: "?"
    val size = 100 // Tamanho do bitmap

    // Gera uma cor aleatória
    val randomColor = Color(
        red = Random.nextFloat(),
        green = Random.nextFloat(),
        blue = Random.nextFloat()
    ).toArgb()

    val textColor = Color.White.toArgb()

    val paint = Paint().apply {
        color = textColor
        style = Paint.Style.FILL
        textSize = 50f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap).apply {
        drawColor(randomColor) // Cor de fundo aleatória
        drawText(
            initials,
            size / 2f, // Centralização horizontal
            size / 2f - (paint.descent() + paint.ascent()) / 2, // Centralização vertical
            paint
        )
    }

    return bitmap.asImageBitmap()
}
