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

///TODO: FIREBASE INITIALIZATION & AUTH STATE
/// 1. Initialize Firebase in Application class or MainActivity.onCreate():
///    Firebase.initialize(this)
/// 2. Observe auth state changes:
///    Firebase.auth.addAuthStateListener { auth ->
///        val user = auth.currentUser
///        // Update navigation based on auth state
///    }
/// 3. Implement auto-login check:
///    if (Firebase.auth.currentUser != null) { navigate to home }
/// 4. Consider using a SplashScreen to check auth state

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

    ///TODO: Replace with Firebase auth state observation
    /// val authState by viewModel.authState.collectAsState()
    /// LaunchedEffect(authState) {
    ///     when (authState) {
    ///         is AuthState.Authenticated -> {
    ///             val userType = (authState as AuthState.Authenticated).userType
    ///             currentScreen = if (userType == UserType.DOCTOR) AppScreen.DoctorHome else AppScreen.PatientHome
    ///         }
    ///         is AuthState.Unauthenticated -> currentScreen = AppScreen.SignIn
    ///         is AuthState.Loading -> { /* Show loading */ }
    ///     }
    /// }

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