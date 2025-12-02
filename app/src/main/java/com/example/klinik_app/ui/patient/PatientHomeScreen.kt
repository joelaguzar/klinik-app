package com.example.klinik_app.ui.patient

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Widgets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.klinik_app.R
import com.example.klinik_app.ui.components.GlassBottomNavBar
import com.example.klinik_app.ui.components.GlassNavItem

// Color scheme matching the design
object PatientHomeColors {
    val Primary = Color(0xFF0A6B5E) // Teal/Green primary color
    val PrimaryLight = Color(0xFFE8F5F3) // Light teal background
    val TextDark = Color(0xFF1A1A1A)
    val TextGray = Color(0xFF6B7280)
    val TextLightGray = Color(0xFF9CA3AF)
    val CardBackground = Color(0xFFF8F9FA)
    val White = Color.White
    val StarYellow = Color(0xFFFBBF24)
    val NavSelected = Color(0xFF0A6B5E)
    val NavUnselected = Color(0xFF9CA3AF)
    
    // Background blob colors (same as Sign In page)
    val Cyan = Color(0xFF06b6d4)
    val Blue = Color(0xFF2563EB)
    val Purple = Color(0xFF7c3aed)
}

data class DoctorCategory(
    val name: String,
    val iconRes: Int,
    val backgroundColor: Color
)

data class Doctor(
    val name: String,
    val specialty: String,
    val rating: Double,
    val reviews: Int,
    val fee: String,
    val imageRes: Int
)

@Composable
fun PatientHomeScreen() {
    var selectedNavItem by remember { mutableIntStateOf(0) }
    val scrollState = rememberScrollState()
    
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
        // Background blobs (same as Sign In page)
        PatientBackgroundBlobs()
        
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                .verticalScroll(scrollState)
        ) {
            // Top Header Section
            HeaderSection()

            Spacer(modifier = Modifier.height(24.dp))

            // Search Banner
            SearchBanner()

            Spacer(modifier = Modifier.height(24.dp))

            // Find Your Doctor Section
            FindYourDoctorSection()

            Spacer(modifier = Modifier.height(24.dp))

            // Popular Doctors Section
            PopularDoctorsSection()

            // Bottom spacing for navbar
            Spacer(modifier = Modifier.height(120.dp))
        }
        
        // Glass Bottom Navigation Bar
        GlassBottomNavBar(
            items = navItems,
            selectedIndex = selectedNavItem,
            onItemSelected = { selectedNavItem = it },
            onFabClick = { /* Handle medical/appointment action */ },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
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

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // User Info
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Profile Picture with green border
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(Color.Transparent)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_user_placeholder),
                    contentDescription = "Profile",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "Welcome Back",
                    fontSize = 12.sp,
                    color = PatientHomeColors.TextGray,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = "Mr. Williamson",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PatientHomeColors.TextDark
                )
            }
        }

        // Action Icons
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Search Icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .border(1.dp, Color(0xFFE5E7EB), CircleShape)
                    .clip(CircleShape)
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = PatientHomeColors.TextDark,
                    modifier = Modifier.size(22.dp)
                )
            }

            // Notification Icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .border(1.dp, Color(0xFFE5E7EB), CircleShape)
                    .clip(CircleShape)
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    tint = PatientHomeColors.TextDark,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
fun SearchBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(160.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFE8F5F3),
                        Color(0xFFF0F9F7)
                    )
                )
            )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Text Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 24.dp, top = 20.dp, bottom = 20.dp)
            ) {
                Text(
                    text = "Looking for",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = PatientHomeColors.TextDark
                )
                Text(
                    text = "desired doctor?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = PatientHomeColors.TextDark
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Search Button
                Button(
                    onClick = { },
                    modifier = Modifier.height(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PatientHomeColors.TextDark
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
                ) {
                    Text(
                        text = "Search for",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = PatientHomeColors.White
                    )
                }
            }

            // Doctor Image
            Box(
                modifier = Modifier
                    .weight(0.8f)
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                // Placeholder for doctor image - you would use actual image resource
                Image(
                    painter = painterResource(id = R.drawable.ic_doctor_placeholder),
                    contentDescription = "Doctor",
                    modifier = Modifier
                        .size(140.dp)
                        .offset(x = 10.dp, y = 10.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun FindYourDoctorSection() {
    val categories = listOf(
        DoctorCategory("Neurology", R.drawable.ic_neurology, Color(0xFFFEF3C7)),
        DoctorCategory("Cardiology", R.drawable.ic_cardiology, Color(0xFFFFE4E6)),
        DoctorCategory("Orthopedics", R.drawable.ic_orthopedics, Color(0xFFDCFCE7)),
        DoctorCategory("Pathology", R.drawable.ic_pathology, Color(0xFFE0E7FF))
    )

    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Find your doctor",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PatientHomeColors.TextDark
            )
            Text(
                text = "See All >",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = PatientHomeColors.Primary,
                modifier = Modifier.clickable { }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Categories Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            categories.forEach { category ->
                DoctorCategoryItem(category = category)
            }
        }
    }
}

@Composable
fun DoctorCategoryItem(category: DoctorCategory) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { }
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(category.backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            // Icon placeholder - would use actual category icons
            Image(
                painter = painterResource(id = category.iconRes),
                contentDescription = category.name,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = category.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = PatientHomeColors.TextDark
        )
    }
}

@Composable
fun PopularDoctorsSection() {
    val doctors = listOf(
        Doctor(
            name = "Chloe Kelly",
            specialty = "M.Ch (Neuro)",
            rating = 4.5,
            reviews = 2530,
            fee = "$50.99",
            imageRes = R.drawable.ic_doctor_placeholder
        ),
        Doctor(
            name = "Lauren Hemp",
            specialty = "Spinal Surgery",
            rating = 4.5,
            reviews = 2530,
            fee = "$50.99",
            imageRes = R.drawable.ic_doctor_placeholder
        )
    )

    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Popular Doctors",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PatientHomeColors.TextDark
            )
            Text(
                text = "See All >",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = PatientHomeColors.Primary,
                modifier = Modifier.clickable { }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Doctor Cards
        doctors.forEach { doctor ->
            DoctorCard(doctor = doctor)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun DoctorCard(doctor: Doctor) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PatientHomeColors.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Doctor Image
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF3F4F6))
            ) {
                Image(
                    painter = painterResource(id = doctor.imageRes),
                    contentDescription = doctor.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Doctor Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = doctor.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PatientHomeColors.TextDark
                )

                Text(
                    text = doctor.specialty,
                    fontSize = 13.sp,
                    color = PatientHomeColors.TextGray,
                    modifier = Modifier.padding(top = 2.dp)
                )

                // Rating Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = PatientHomeColors.StarYellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${doctor.rating} (${doctor.reviews})",
                        fontSize = 12.sp,
                        color = PatientHomeColors.TextGray
                    )
                }
            }

            // Fee and Book Button
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Fees",
                    fontSize = 11.sp,
                    color = PatientHomeColors.TextLightGray
                )
                Text(
                    text = doctor.fee,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PatientHomeColors.TextDark
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { },
                    modifier = Modifier.height(32.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PatientHomeColors.Primary
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
                ) {
                    Text(
                        text = "Book Now",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PatientHomeColors.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
fun PreviewPatientHomeScreen() {
    MaterialTheme {
        PatientHomeScreen()
    }
}
