package com.ismael.teams

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ismael.teams.data.DataSource
import com.ismael.teams.ui.ChatList
import com.ismael.teams.ui.TeamsApp
import com.ismael.teams.ui.theme.TeamsTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TeamsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                   TeamsApp(
                        modifier = Modifier.padding(innerPadding)
                   )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TeamsAppPreview() {

}