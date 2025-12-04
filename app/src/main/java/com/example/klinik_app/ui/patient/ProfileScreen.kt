package com.example.klinik_app.ui.patient

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Bloodtype
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
import com.example.klinik_app.data.FirebaseData
import com.example.klinik_app.data.Patient

///TODO: FIREBASE - PATIENT PROFILE
/// 1. Create ProfileViewModel with PatientRepository
/// 2. Observe current patient data in real-time:
///    - firestore.collection("patients").document(currentUserId).snapshots()
/// 3. Implement profile update:
///    - firestore.collection("patients").document(currentUserId).update(updatedData)
/// 4. Implement profile image upload:
///    - storage.reference.child("profile_images/$userId.jpg").putFile(imageUri)
///    - Update imageUrl field in patient document
/// 5. Add edit mode for profile fields
/// 6. Implement account deletion with confirmation

@Composable
fun ProfileScreen() {
    val scrollState = rememberScrollState()
    val currentPatient by produceState<Patient?>(initialValue = null) {
        value = FirebaseData.getCurrentPatient()
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
            color = PatientHomeColors.TextDark
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        if (currentPatient != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = PatientHomeColors.White),
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
                            painter = painterResource(id = currentPatient!!.imageRes),
                            contentDescription = "Profile Picture",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = currentPatient!!.fullName,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = PatientHomeColors.TextDark
                    )

                    Text(
                        text = currentPatient!!.email,
                        fontSize = 14.sp,
                        color = PatientHomeColors.TextGray
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Personal Information",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = PatientHomeColors.TextDark
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PatientHomeColors.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ProfileInfoRow(
                        icon = Icons.Default.Person,
                        label = "Sex",
                        value = currentPatient!!.sex.displayName()
                    )
                    ProfileInfoRow(
                        icon = Icons.Default.CalendarToday,
                        label = "Birthdate",
                        value = currentPatient!!.birthdate
                    )
                    ProfileInfoRow(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = currentPatient!!.email
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Health Information",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = PatientHomeColors.TextDark
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PatientHomeColors.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ProfileInfoRow(
                        icon = Icons.Default.Height,
                        label = "Height",
                        value = currentPatient!!.height
                    )
                    ProfileInfoRow(
                        icon = Icons.Default.MonitorWeight,
                        label = "Weight",
                        value = currentPatient!!.weight
                    )
                    ProfileInfoRow(
                        icon = Icons.Default.Bloodtype,
                        label = "Blood Type",
                        value = currentPatient!!.bloodType
                    )
                }
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
                        text = "Profile",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = PatientHomeColors.TextDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Please sign in to view your profile",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = PatientHomeColors.TextGray
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileInfoRow(
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
                .background(PatientHomeColors.PrimaryLight),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = PatientHomeColors.Primary,
                modifier = Modifier.size(20.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = PatientHomeColors.TextGray
            )
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = PatientHomeColors.TextDark
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
    MaterialTheme {
        ProfileScreen()
    }
}
