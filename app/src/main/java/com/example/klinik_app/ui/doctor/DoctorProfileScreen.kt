package com.example.klinik_app.ui.doctor

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.klinik_app.data.Doctor
import com.example.klinik_app.data.FirebaseData

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DoctorProfileScreen() {
    val scrollState = rememberScrollState()
    val currentDoctor by produceState<Doctor?>(initialValue = null) {
        value = FirebaseData.getCurrentDoctor()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "My Profile",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = DoctorHomeColors.TextDark
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        if (currentDoctor != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DoctorHomeColors.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF3F4F6))
                    ) {
                        Image(
                            painter = painterResource(id = currentDoctor!!.imageRes),
                            contentDescription = "Profile Picture",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = currentDoctor!!.fullName,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = DoctorHomeColors.TextDark
                    )

                    Text(
                        text = "${currentDoctor!!.position} • ${currentDoctor!!.field}",
                        fontSize = 14.sp,
                        color = DoctorHomeColors.Primary
                    )

                    Text(
                        text = currentDoctor!!.email,
                        fontSize = 13.sp,
                        color = DoctorHomeColors.TextGray
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = DoctorHomeColors.StarYellow,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${currentDoctor!!.ratings}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DoctorHomeColors.TextDark
                        )
                        Text(
                            text = " (${currentDoctor!!.totalReviews} reviews)",
                            fontSize = 14.sp,
                            color = DoctorHomeColors.TextGray
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Specializations",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = DoctorHomeColors.TextDark
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                currentDoctor!!.tags.forEach { tag ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(DoctorHomeColors.PrimaryLight)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = tag,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = DoctorHomeColors.Primary
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Personal Information",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = DoctorHomeColors.TextDark
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DoctorHomeColors.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DoctorProfileInfoRow(
                        icon = Icons.Default.Person,
                        label = "Sex",
                        value = currentDoctor!!.sex.displayName()
                    )
                    DoctorProfileInfoRow(
                        icon = Icons.Default.CalendarToday,
                        label = "Birthdate",
                        value = currentDoctor!!.birthdate
                    )
                    DoctorProfileInfoRow(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = currentDoctor!!.email
                    )
                    DoctorProfileInfoRow(
                        icon = Icons.Default.Work,
                        label = "Position",
                        value = currentDoctor!!.position
                    )
                    DoctorProfileInfoRow(
                        icon = Icons.Default.MedicalServices,
                        label = "Field",
                        value = currentDoctor!!.field
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "About",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = DoctorHomeColors.TextDark
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DoctorHomeColors.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = currentDoctor!!.description,
                    fontSize = 14.sp,
                    color = DoctorHomeColors.TextGray,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(120.dp))
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Doctor Profile",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = DoctorHomeColors.TextDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Please sign in to view your profile",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = DoctorHomeColors.TextGray
                    )
                }
            }
        }
    }
}

@Composable
private fun DoctorProfileInfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(DoctorHomeColors.PrimaryLight),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = DoctorHomeColors.Primary,
                modifier = Modifier.size(20.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = DoctorHomeColors.TextGray
            )
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = DoctorHomeColors.TextDark
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDoctorProfileScreen() {
    MaterialTheme {
        DoctorProfileScreen()
    }
}
