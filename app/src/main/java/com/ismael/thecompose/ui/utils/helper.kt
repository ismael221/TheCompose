package com.ismael.thecompose.ui.utils


import android.content.Context
import android.util.Log
import com.ismael.thecompose.data.model.Message
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.JitsiMeetUserInfo
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


fun startCall(context: Context) {
    val serverURL = URL("https://meet.jit.si")

    val options = JitsiMeetConferenceOptions.Builder()
        .setServerURL(serverURL)
        .setRoom("OpenAIComposeTestRoom")
        .setAudioMuted(false)
        .setUserInfo(JitsiMeetUserInfo().apply {
            displayName = "Yasmin"
            email = "yasmin@ismael"
            avatar =
                URL("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQoocigdf8WpKI5tUJteYSVVEL1kJJiLTPuOg&s")
        })
        .setAudioOnly(true)
        .build()

    JitsiMeetActivity.launch(context, options)
}


fun startMeeting(context: Context) {
    val serverURL = URL("https://meet.jit.si")

    val options = JitsiMeetConferenceOptions.Builder()
        .setServerURL(serverURL)
        .setRoom("OpenAIComposeTestRoom")
        .setAudioMuted(false)
        .setVideoMuted(false)
        .setUserInfo(JitsiMeetUserInfo().apply {
            displayName = "Yasmin"
            email = "yasmin@ismael"
            avatar =
                URL("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQoocigdf8WpKI5tUJteYSVVEL1kJJiLTPuOg&s")
        })
        .setFeatureFlag("lobby-mode.enabled",false)
        .setFeatureFlag("add-people.enabled",false)
        .setFeatureFlag("invite.enabled",false)
        .setFeatureFlag("chat.enabled",false)
        .setFeatureFlag("kick-out.enabled",false)
        .setFeatureFlag("meeting-name.enabled",false)
        .setFeatureFlag("prejoinpage.enabled",false)
        .setFeatureFlag("prejoinpage.hideDisplayName",true)
        .build()

    JitsiMeetActivity.launch(context, options)
}

