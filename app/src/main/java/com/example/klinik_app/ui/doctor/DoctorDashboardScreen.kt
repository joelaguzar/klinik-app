package com.example.klinik_app.ui.doctor

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.klinik_app.R
import com.example.klinik_app.data.FirebaseData
import com.example.klinik_app.data.Doctor as DataDoctor
import com.example.klinik_app.data.AppointmentStatus as DataAppointmentStatus

///TODO: FIREBASE - DOCTOR DASHBOARD
/// 1. Create DoctorDashboardViewModel
/// 2. Observe doctor profile in real-time:
///    - firestore.collection("doctors").document(currentUserId).snapshots()
/// 3. Observe appointment statistics:
///    - Query appointments where doctorId == currentUserId
///    - Calculate counts for each status
/// 4. Observe recent appointments:
///    - firestore.collection("appointments")
///        .whereEqualTo("doctorId", currentUserId)
///        .orderBy("createdAt", Query.Direction.DESCENDING)
///        .limit(5)
///        .snapshots()
/// 5. Also show pending appointments without assigned doctor
/// 6. Implement real-time updates for new appointment notifications

data class DoctorProfile(
    val firstName: String,
    val lastName: String,
    val position: String,
    val field: String,
    val tags: List<String>,
    val description: String,
    val rating: Double,
    val totalReviews: Int,
    val imageRes: Int
) {
    val fullName: String get() = "Dr. $firstName $lastName"
    
    companion object {
        fun fromDataDoctor(doctor: DataDoctor): DoctorProfile {
            return DoctorProfile(
                firstName = doctor.firstName,
                lastName = doctor.lastName,
                position = doctor.position,
                field = doctor.field,
                tags = doctor.tags,
                description = doctor.description,
                rating = doctor.ratings,
                totalReviews = doctor.totalReviews,
                imageRes = doctor.imageRes
            )
        }
    }
}

enum class DoctorAppointmentStatus {
    PENDING,
    ACCEPTED,
    COMPLETED,
    DECLINED;
    
    companion object {
        fun fromDataStatus(status: DataAppointmentStatus): DoctorAppointmentStatus {
            return when (status) {
                DataAppointmentStatus.PENDING -> PENDING
                DataAppointmentStatus.ACCEPTED -> ACCEPTED
                DataAppointmentStatus.COMPLETED -> COMPLETED
                DataAppointmentStatus.DECLINED -> DECLINED
            }
        }
    }
}

data class AppointmentCounts(
    val pending: Int,
    val accepted: Int,
    val completed: Int,
    val declined: Int
)

data class DoctorRecentAppointment(
    val id: String,
    val patientName: String,
    val symptoms: String,
    val status: DoctorAppointmentStatus,
    val scheduledTime: String?,
    val createdAt: String
)

object DoctorStatusColors {
    val PendingBackground = Color(0xFFFFF3CD)
    val PendingText = Color(0xFF856404)
    val AcceptedBackground = Color(0xFFD4EDDA)
    val AcceptedText = Color(0xFF155724)
    val DeclinedBackground = Color(0xFFF8D7DA)
    val DeclinedText = Color(0xFF721C24)
    val CompletedBackground = Color(0xFFD1ECF1)
    val CompletedText = Color(0xFF0C5460)
}

@Composable
fun DoctorDashboardScreen(
    onLogoutClick: () -> Unit = {}
) {
    ///TODO: Inject DoctorDashboardViewModel
    /// val viewModel: DoctorDashboardViewModel = hiltViewModel()
    /// val doctorProfile by viewModel.doctorProfileFlow.collectAsState()
    /// val appointmentCounts by viewModel.appointmentCountsFlow.collectAsState()
    /// val recentAppointments by viewModel.recentAppointmentsFlow.collectAsState()
    /// val isLoading by viewModel.isLoading.collectAsState()
    
    val currentDoctor = FirebaseData.getCurrentDoctor()
    val doctorProfile = remember(currentDoctor) {
        currentDoctor?.let { DoctorProfile.fromDataDoctor(it) } ?: DoctorProfile(
            firstName = "Guest",
            lastName = "Doctor",
            position = "Consultant",
            field = "General",
            tags = listOf("General Practice"),
            description = "Welcome to Klinik App",
            rating = 0.0,
            totalReviews = 0,
            imageRes = R.drawable.ic_doctor_placeholder
        )
    }

    val doctorAppointments = remember(currentDoctor) {
        if (currentDoctor != null) {
            FirebaseData.getAppointmentsForDoctor(currentDoctor.id)
        } else {
            emptyList()
        }
    }

    val appointmentCounts = remember(doctorAppointments) {
        AppointmentCounts(
            pending = doctorAppointments.count { it.status == DataAppointmentStatus.PENDING },
            accepted = doctorAppointments.count { it.status == DataAppointmentStatus.ACCEPTED },
            completed = doctorAppointments.count { it.status == DataAppointmentStatus.COMPLETED },
            declined = doctorAppointments.count { it.status == DataAppointmentStatus.DECLINED }
        )
    }

    val recentAppointments = remember(doctorAppointments) {
        doctorAppointments.take(5).map { apt ->
            DoctorRecentAppointment(
                id = apt.id,
                patientName = FirebaseData.getPatientNameForAppointment(apt) ?: "Unknown Patient",
                symptoms = apt.symptoms,
                status = DoctorAppointmentStatus.fromDataStatus(apt.status),
                scheduledTime = apt.doctorResponse?.respondedAt?.let { 
                    formatRelativeTime(it)
                },
                createdAt = formatRelativeTime(apt.createdAt)
            )
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        item {
            DashboardHeader(onLogoutClick = onLogoutClick)
        }

        item {
            DoctorProfileCard(doctorProfile = doctorProfile)
        }

        item {
            AppointmentStatisticsSection(counts = appointmentCounts)
        }

        item {
            RecentAppointmentsHeader()
        }

        items(recentAppointments) { appointment ->
            RecentAppointmentCard(appointment = appointment)
        }
    }
}

private fun formatRelativeTime(isoDate: String): String {
    return try {
        val dateStr = isoDate.substring(0, 10)
        val parts = dateStr.split("-")
        "${parts[1]}/${parts[2]}/${parts[0]}"
    } catch (e: Exception) {
        "Recently"
    }
}

@Composable
fun DashboardHeader(onLogoutClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Dashboard",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = DoctorHomeColors.TextDark
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Welcome back, Doctor!",
                fontSize = 14.sp,
                color = DoctorHomeColors.TextGray
            )
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
fun DoctorProfileCard(doctorProfile: DoctorProfile) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DoctorHomeColors.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF3F4F6))
                ) {
                    Image(
                        painter = painterResource(id = doctorProfile.imageRes),
                        contentDescription = doctorProfile.fullName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = doctorProfile.fullName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = DoctorHomeColors.TextDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = doctorProfile.position,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = DoctorHomeColors.Primary
                    )
                    Text(
                        text = doctorProfile.field,
                        fontSize = 13.sp,
                        color = DoctorHomeColors.TextGray
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = DoctorHomeColors.StarYellow,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${doctorProfile.rating}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DoctorHomeColors.TextDark
                        )
                        Text(
                            text = " (${doctorProfile.totalReviews} reviews)",
                            fontSize = 12.sp,
                            color = DoctorHomeColors.TextGray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                doctorProfile.tags.take(3).forEach { tag ->
                    DoctorTag(tag = tag)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = doctorProfile.description,
                fontSize = 13.sp,
                color = DoctorHomeColors.TextGray,
                lineHeight = 20.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun DoctorTag(tag: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(DoctorHomeColors.PrimaryLight)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = tag,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = DoctorHomeColors.Primary
        )
    }
}

@Composable
fun AppointmentStatisticsSection(counts: AppointmentCounts) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Appointment Statistics",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = DoctorHomeColors.TextDark
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatisticCard(
                modifier = Modifier.weight(1f),
                title = "Pending",
                count = counts.pending,
                icon = Icons.Default.HourglassEmpty,
                backgroundColor = DoctorStatusColors.PendingBackground,
                iconColor = DoctorStatusColors.PendingText
            )
            StatisticCard(
                modifier = Modifier.weight(1f),
                title = "Accepted",
                count = counts.accepted,
                icon = Icons.Default.CheckCircle,
                backgroundColor = DoctorStatusColors.AcceptedBackground,
                iconColor = DoctorStatusColors.AcceptedText
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatisticCard(
                modifier = Modifier.weight(1f),
                title = "Completed",
                count = counts.completed,
                icon = Icons.Default.CheckCircle,
                backgroundColor = DoctorStatusColors.CompletedBackground,
                iconColor = DoctorStatusColors.CompletedText
            )
            StatisticCard(
                modifier = Modifier.weight(1f),
                title = "Declined",
                count = counts.declined,
                icon = Icons.Default.Close,
                backgroundColor = DoctorStatusColors.DeclinedBackground,
                iconColor = DoctorStatusColors.DeclinedText
            )
        }
    }
}

@Composable
fun StatisticCard(
    modifier: Modifier = Modifier,
    title: String,
    count: Int,
    icon: ImageVector,
    backgroundColor: Color,
    iconColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor.copy(alpha = 0.7f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = count.toString(),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = iconColor
                )
                Text(
                    text = title,
                    fontSize = 12.sp,
                    color = iconColor.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun RecentAppointmentsHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Recent Appointments",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = DoctorHomeColors.TextDark
        )
        Text(
            text = "See All",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = DoctorHomeColors.Primary,
            modifier = Modifier.clickable { }
        )
    }
}

@Composable
fun RecentAppointmentCard(appointment: DoctorRecentAppointment) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clickable { /* Handle click */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DoctorHomeColors.White),
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
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF3F4F6))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_user_placeholder),
                    contentDescription = "Patient",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = appointment.patientName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DoctorHomeColors.TextDark
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.MedicalServices,
                        contentDescription = null,
                        tint = DoctorHomeColors.Primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = appointment.symptoms,
                        fontSize = 12.sp,
                        color = DoctorHomeColors.TextGray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                if (appointment.scheduledTime != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = DoctorHomeColors.TextLightGray,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = appointment.scheduledTime,
                            fontSize = 11.sp,
                            color = DoctorHomeColors.TextLightGray
                        )
                    }
                } else {
                    Text(
                        text = appointment.createdAt,
                        fontSize = 11.sp,
                        color = DoctorHomeColors.TextLightGray
                    )
                }
            }

            DoctorStatusBadge(status = appointment.status)
        }
    }
}

@Composable
fun DoctorStatusBadge(status: DoctorAppointmentStatus) {
    val (backgroundColor, textColor, text) = when (status) {
        DoctorAppointmentStatus.PENDING -> Triple(
            DoctorStatusColors.PendingBackground,
            DoctorStatusColors.PendingText,
            "Pending"
        )
        DoctorAppointmentStatus.ACCEPTED -> Triple(
            DoctorStatusColors.AcceptedBackground,
            DoctorStatusColors.AcceptedText,
            "Accepted"
        )
        DoctorAppointmentStatus.DECLINED -> Triple(
            DoctorStatusColors.DeclinedBackground,
            DoctorStatusColors.DeclinedText,
            "Declined"
        )
        DoctorAppointmentStatus.COMPLETED -> Triple(
            DoctorStatusColors.CompletedBackground,
            DoctorStatusColors.CompletedText,
            "Completed"
        )
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
fun PreviewDoctorDashboardScreen() {
    MaterialTheme {
        DoctorDashboardScreen()
    }
}
