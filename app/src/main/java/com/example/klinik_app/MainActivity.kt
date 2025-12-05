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
import com.example.klinik_app.ui.doctor.DoctorHomeScreen
import com.example.klinik_app.ui.patient.PatientHomeScreen

enum class AppScreen {
    SignIn, SignUp, PatientHome, DoctorHome
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
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf(AppScreen.SignIn) }

    when (currentScreen) {
        AppScreen.SignIn -> KlinikSignInScreen(
            onNavigateToSignUp = { currentScreen = AppScreen.SignUp },
            onSignInSuccess = { userRole ->
                currentScreen = if (userRole == "doctor") AppScreen.DoctorHome else AppScreen.PatientHome
            }
        )
        AppScreen.SignUp -> KlinikSignUpScreen(
            onNavigateToSignIn = { currentScreen = AppScreen.SignIn }
        )
        AppScreen.PatientHome -> PatientHomeScreen(
            onLogoutClick = { currentScreen = AppScreen.SignIn }
        )
        AppScreen.DoctorHome -> DoctorHomeScreen(
            onLogoutClick = { currentScreen = AppScreen.SignIn }
        )
    }
}