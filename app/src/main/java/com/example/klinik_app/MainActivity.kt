package com.example.klinik_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.klinik_app.ui.auth.KlinikSignUpScreen

enum class AuthScreen {
    SignIn, SignUp
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AuthNavigation()
                }
            }
        }
    }
}

@Composable
fun AuthNavigation() {
    var currentScreen by remember { mutableStateOf(AuthScreen.SignIn) }

    when (currentScreen) {
        AuthScreen.SignIn -> KlinikSignInScreen(
            onNavigateToSignUp = { currentScreen = AuthScreen.SignUp }
        )
        AuthScreen.SignUp -> KlinikSignUpScreen(
            onNavigateToSignIn = { currentScreen = AuthScreen.SignIn }
        )
    }
}