package com.example.salvationarmyconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.example.salvationarmyconnect.ui.theme.SalvationArmyConnectTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SalvationArmyConnectTheme {
                val auth = FirebaseAuth.getInstance()
                var isLoggedIn by remember { mutableStateOf(auth.currentUser != null) }

                // NAVIGATION STATE: "home", "songbook", "giving", etc.
                var currentScreen by remember { mutableStateOf("home") }

                if (isLoggedIn) {
                    when (currentScreen) {
                        "home" -> HomeScreen(
                            userName = auth.currentUser?.displayName ?: "Soldier",
                            onNavigate = { destination ->
                                currentScreen = destination // Switch screen
                            }
                        )
                        "songs" -> SongbookScreen(
                            onBackHome = { currentScreen = "home" } // Go back
                        )
                        // Add other screens here later
                        else -> HomeScreen(
                            onNavigate = { currentScreen = it }
                        )
                    }
                } else {
                    LoginScreen(onLoginSuccess = { isLoggedIn = true })
                }
            }
        }
    }
}