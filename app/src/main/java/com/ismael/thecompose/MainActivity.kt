package com.ismael.thecompose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import com.example.compose.TheComposeTheme
import com.ismael.thecompose.data.remote.xmpp.XmppManager

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeXmpp()
        // enableEdgeToEdge()
        setContent {
            TheComposeTheme {
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
    val username = "yasmin"
    val password = "1234"

    try {
        XmppManager.connect(server, username, password)
    } catch (e: Exception) {
        println("Erro ao conectar no XMPP: ${e.message}")
    }
}



