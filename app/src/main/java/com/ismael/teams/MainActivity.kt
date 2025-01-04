package com.ismael.teams

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.getSystemService
import com.example.compose.TeamsTheme
import com.ismael.teams.data.remote.xmpp.XmppManager
import org.jxmpp.jid.impl.JidCreate

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeXmpp()
        // enableEdgeToEdge()
        setContent {
            TeamsTheme {
                val layoutDirection = LocalLayoutDirection.current
                Scaffold(
                    modifier =  Modifier
                        .padding(
                            start = WindowInsets.safeDrawing.asPaddingValues()
                                .calculateStartPadding(layoutDirection),
                            end = WindowInsets.safeDrawing.asPaddingValues()
                                .calculateEndPadding(layoutDirection)
                        )
                ) {
                    val windowSize = calculateWindowSizeClass(this)
                    TheComposeApp(
                        windowSize = windowSize.widthSizeClass,
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        XmppManager.disconnect()
    }
}


private fun initializeXmpp() {
    val server = "ismael"
    val username = "ismael221"
    val password = "Ismuca18@"

    try {
        XmppManager.connect(server, username, password)
    } catch (e: Exception) {
        println("Erro ao conectar no XMPP: ${e.message}")
    }
}



