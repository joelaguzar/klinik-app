package com.example.klinik_app.ui.patient

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.klinik_app.R

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
fun HomeContent() {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
            .verticalScroll(scrollState)
    ) {
        // header
        HeaderSection()
        Spacer(modifier = Modifier.height(24.dp))

        // search
        SearchBanner()
        Spacer(modifier = Modifier.height(24.dp))

        // sdg card
        FindYourDoctorSection()
        Spacer(modifier = Modifier.height(24.dp))

        // doctor cards
        PopularDoctorsSection()

        // Bottom spacing for navbar
        Spacer(modifier = Modifier.height(120.dp))
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
            // profile image
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
            // card text content
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

                // SDG Button
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
                        text = "SDG 3 Good Health",
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
                // doctor icon placeholder
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
                text = "See All",
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
                text = "See All",
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
fun PreviewHomeContent() {
    MaterialTheme {
        HomeContent()
    }
}
