package com.example.klinik_app.ui.patient

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.klinik_app.ui.components.GlassBottomNavBar
import com.example.klinik_app.ui.components.GlassNavItem

object PatientHomeColors {
    val Primary = Color(0xFF0A6B5E)
    val PrimaryLight = Color(0xFFE8F5F3)
    val TextDark = Color(0xFF1A1A1A)
    val TextGray = Color(0xFF6B7280)
    val TextLightGray = Color(0xFF9CA3AF)
    val CardBackground = Color(0xFFF8F9FA)
    val White = Color.White
    val StarYellow = Color(0xFFFBBF24)
    val NavSelected = Color(0xFF0A6B5E)
    val NavUnselected = Color(0xFF9CA3AF)

    val Cyan = Color(0xFF06b6d4)
    val Blue = Color(0xFF2563EB)
    val Purple = Color(0xFF7c3aed)
}

@Composable
fun PatientHomeScreen(
    onLogoutClick: () -> Unit = {}
) {
    var selectedNavItem by remember { mutableIntStateOf(0) }
    var selectedDoctor by remember { mutableStateOf<Doctor?>(null) }
    
    // Navigation items using the reusable GlassNavItem with modern rounded icons
    val navItems = listOf(
        GlassNavItem("Home", Icons.Rounded.Home, Icons.Outlined.Home),
        GlassNavItem("Appointments", Icons.Rounded.CalendarMonth, Icons.Outlined.CalendarMonth),
        GlassNavItem("Inbox", Icons.Rounded.Chat, Icons.Outlined.Chat),
        GlassNavItem("Profile", Icons.Rounded.Person, Icons.Outlined.Person)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PatientHomeColors.White)
    ) {
        PatientBackgroundBlobs()

        // Show Doctor Profile Screen if a doctor is selected
        AnimatedContent(
            targetState = selectedDoctor,
            transitionSpec = {
                if (targetState != null) {
                    slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
                } else {
                    slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
                }
            },
            label = "DoctorProfileTransition"
        ) { doctor ->
            if (doctor != null) {
                DoctorProfileScreen(
                    doctor = doctor,
                    onBackClick = { selectedDoctor = null }
                )
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    when (selectedNavItem) {
                        0 -> HomeContent(
                            onDoctorClick = { selectedDoctor = it },
                            onLogoutClick = onLogoutClick
                        )
                        1 -> AppointmentsScreen()
                        2 -> InboxScreen()
                        3 -> ProfileScreen()
                    }
                }
            }
        }
        
        // Only show bottom navbar when not viewing doctor profile
        if (selectedDoctor == null) {
            GlassBottomNavBar(
                items = navItems,
                selectedIndex = selectedNavItem,
                onItemSelected = { selectedNavItem = it },
                onFabClick = { /* Handle medical/appointment action */ },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun PatientBackgroundBlobs() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        // Top Left Cyan
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(PatientHomeColors.Cyan.copy(alpha = 0.4f), Color.Transparent),
                center = Offset(0f, 0f), radius = 900f
            ),
            center = Offset(0f, 0f), radius = 900f
        )
        // Center Right Blue
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(PatientHomeColors.Blue.copy(alpha = 0.3f), Color.Transparent),
                center = Offset(size.width, size.height * 0.4f), radius = 700f
            ),
            center = Offset(size.width, size.height * 0.4f), radius = 700f
        )
        // Bottom Left Purple
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(PatientHomeColors.Purple.copy(alpha = 0.2f), Color.Transparent),
                center = Offset(0f, size.height), radius = 800f
            ),
            center = Offset(0f, size.height), radius = 800f
        )
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
fun PreviewPatientHomeScreen() {
    MaterialTheme {
        PatientHomeScreen()
    }
}
