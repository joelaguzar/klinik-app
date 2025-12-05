package com.example.klinik_app.ui.patient

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.klinik_app.R
import com.example.klinik_app.data.FirebaseData
import com.example.klinik_app.data.Patient
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import com.example.klinik_app.data.Doctor as DataDoctor

data class DoctorCategory(
    val name: String,
    val iconRes: Int,
    val backgroundColor: Color
)

data class Doctor(
    val id: String,
    val firstName: String,
    val lastName: String,
    val field: String,
    val position: String,
    val rating: Double,
    val appointments: Int,
    val imageRes: Int,
    val sex: String = "Female",
    val age: Int = 35,
    val tags: List<String> = emptyList(),
    val description: String = ""
) {
    val fullName: String get() = "$firstName $lastName"
    
    companion object {
        fun fromDataDoctor(dataDoctor: DataDoctor): Doctor {
            return Doctor(
                id = dataDoctor.id,
                firstName = dataDoctor.firstName,
                lastName = dataDoctor.lastName,
                field = dataDoctor.field,
                position = dataDoctor.position,
                rating = dataDoctor.ratings,
                appointments = dataDoctor.totalReviews,
                imageRes = dataDoctor.imageRes,
                sex = dataDoctor.sex.displayName(),
                age = dataDoctor.age,
                tags = dataDoctor.tags,
                description = dataDoctor.description
            )
        }
    }
}

data class PatientInfo(
    val age: Int,
    val height: String,
    val weight: String,
    val bloodType: String
) {
    companion object {
        fun fromPatient(patient: Patient): PatientInfo {
            return PatientInfo(
                age = patient.age,
                height = patient.height,
                weight = patient.weight,
                bloodType = patient.bloodType
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    onDoctorClick: (Doctor) -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    val currentPatient by produceState<Patient?>(initialValue = null) {
        withContext(Dispatchers.IO) {
            value = FirebaseData.getCurrentPatient()
        }
    }


    val patientInfo = currentPatient?.let { PatientInfo.fromPatient(it) } ?: PatientInfo(
        age = 28,
        height = "175 cm",
        weight = "70 kg",
        bloodType = "O+"
    )
    
    val patientName = currentPatient?.fullName ?: "Guest"

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = PatientHomeColors.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            SDGBottomSheetContent(
                onDismiss = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet = false
                        }
                    }
                }
            )
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
            .verticalScroll(scrollState)
    ) {
        HeaderSection(patientName = patientName, onLogoutClick = onLogoutClick)

        PatientInfoSection(patientInfo = patientInfo)
        
        Spacer(modifier = Modifier.height(24.dp))

        SearchBanner(onSDGClick = { showBottomSheet = true })
        Spacer(modifier = Modifier.height(24.dp))

        FindYourDoctorSection()
        Spacer(modifier = Modifier.height(24.dp))

        PopularDoctorsSection(onDoctorClick = onDoctorClick)

        Spacer(modifier = Modifier.height(120.dp))
    }
}

@Composable
fun HeaderSection(patientName: String = "Mr. Williamson", onLogoutClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
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
                    text = patientName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PatientHomeColors.TextDark
                )
            }
        }

        Button(
            onClick = onLogoutClick,
            modifier = Modifier.height(40.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE53935)
            ),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
        ) {
            Text(
                text = "Logout",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Composable
fun PatientInfoSection(patientInfo: PatientInfo) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        PatientInfoItem(
            iconRes = R.drawable.ic_age,
            label = "Age",
            value = "${patientInfo.age} yrs",
            backgroundColor = Color(0xFFE3F2FD)
        )
        PatientInfoItem(
            iconRes = R.drawable.ic_height,
            label = "Height",
            value = patientInfo.height,
            backgroundColor = Color(0xFFFCE4EC)
        )
        PatientInfoItem(
            iconRes = R.drawable.ic_weight,
            label = "Weight",
            value = patientInfo.weight,
            backgroundColor = Color(0xFFF3E5F5)
        )
        PatientInfoItem(
            iconRes = R.drawable.ic_blood,
            label = "Blood",
            value = patientInfo.bloodType,
            backgroundColor = Color(0xFFFFEBEE)
        )
    }
}

@Composable
fun PatientInfoItem(
    iconRes: Int,
    label: String,
    value: String,
    backgroundColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = PatientHomeColors.TextGray,
            fontWeight = FontWeight.Normal
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = PatientHomeColors.TextDark
        )
    }
}

@Composable
fun SearchBanner(onSDGClick: () -> Unit = {}) {
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
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 24.dp, top = 20.dp, bottom = 20.dp)
            ) {
                Text(
                    text = "Aligned with SDG 3",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,
                    color = PatientHomeColors.TextDark
                )
                Text(
                    text = "Good Health and Well Being",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,
                    color = PatientHomeColors.TextDark
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onSDGClick,
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

            Box(
                modifier = Modifier
                    .weight(0.8f)
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
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
fun PopularDoctorsSection(onDoctorClick: (Doctor) -> Unit = {}) {

    val doctors by produceState<List<Doctor>>(initialValue = emptyList()) {
        try {

            val firestore = FirebaseFirestore.getInstance()
            val snapshot = firestore.collection("doctors").get().await()

            val tempList = mutableListOf<Doctor>()
            for (document in snapshot.documents) {
                val doctorItem = document.toObject(DataDoctor::class.java)

                if (doctorItem != null) {
                    val finalDoctor = doctorItem.copy(id = document.id)
                    tempList.add(Doctor.fromDataDoctor(finalDoctor))
                }
            }

            tempList.sortByDescending { it.rating }

            value = tempList

        } catch (e: Exception) {
            e.printStackTrace()
            value = emptyList()
        }
    }

    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
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

        doctors.forEach { doctor ->
            DoctorCard(doctor = doctor, onViewDetailsClick = { onDoctorClick(doctor) })
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun DoctorCard(doctor: Doctor, onViewDetailsClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onViewDetailsClick() },
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
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF3F4F6))
            ) {
                Image(
                    painter = painterResource(id = doctor.imageRes),
                    contentDescription = doctor.fullName,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = doctor.fullName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PatientHomeColors.TextDark
                )

                Text(
                    text = doctor.field,
                    fontSize = 13.sp,
                    color = PatientHomeColors.TextGray,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Text(
                    text = doctor.position,
                    fontSize = 12.sp,
                    color = PatientHomeColors.TextLightGray,
                    modifier = Modifier.padding(top = 2.dp)
                )

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
                        text = "${doctor.rating} (${doctor.appointments})",
                        fontSize = 12.sp,
                        color = PatientHomeColors.TextGray
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = onViewDetailsClick,
                    modifier = Modifier.height(32.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PatientHomeColors.Primary
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
                ) {
                    Text(
                        text = "View Details",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PatientHomeColors.White
                    )
                }
            }
        }
    }
}

@Composable
fun SDGBottomSheetContent(onDismiss: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color(0xFFE0E0E0))
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.klinik_logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(70.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Klinik",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = PatientHomeColors.TextDark
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "SDG 3: Good Health and Well-Being",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = PatientHomeColors.TextDark,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Ensure healthy lives and promote well-being for all at all ages",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = PatientHomeColors.Primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Klinik is an e-consultation platform designed to provide accessible and timely healthcare for all users, regardless of their location or schedule. By offering asynchronous consultations, users can receive medical advice and guidance at their own convenience, without needing to book in-person appointments or wait for long hours. Aligned with SDG 3: Good Health and Well-Being, Klinik's mission is to ensure equitable access to healthcare services and empower individuals to manage their health more effectively.",
            fontSize = 14.sp,
            color = PatientHomeColors.TextGray,
            textAlign = TextAlign.Start,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Key Targets",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PatientHomeColors.TextDark
                )

                Spacer(modifier = Modifier.height(12.dp))

                SDGTargetItem(text = "Provide universal access to healthcare")
                SDGTargetItem(text = "Achieve universal healthcare coverage")
                SDGTargetItem(text = "Reduce deaths from non-communicable diseases")
                SDGTargetItem(text = "Strengthen health risk management")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PatientHomeColors.Primary
            )
        ) {
            Text(
                text = "Got It",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Composable
fun SDGTargetItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(PatientHomeColors.Primary)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 13.sp,
            color = PatientHomeColors.TextGray
        )
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
fun PreviewHomeContent() {
    MaterialTheme {
        HomeContent()
    }
}
