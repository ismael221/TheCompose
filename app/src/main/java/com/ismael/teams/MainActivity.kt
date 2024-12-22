package com.ismael.teams

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.compose.TeamsTheme
import com.ismael.teams.xmpp.XmppManager
import org.jxmpp.jid.impl.JidCreate

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeXmpp()
        // enableEdgeToEdge()
        setContent {
            TeamsTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    // val windowSize = calculateWindowSizeClass(this)
                    TheComposeApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        initializeXmpp()
    }

    override fun onStop() {
        super.onStop()
        XmppManager.disconnect()
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


@Preview(showBackground = true)
@Composable
fun TeamsAppPreview() {

}