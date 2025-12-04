package com.example.klinik_app.ui.doctor

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.klinik_app.R
import com.example.klinik_app.data.FirebaseData
import com.example.klinik_app.data.Appointment as DataAppointment
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

///TODO: FIREBASE - DOCTOR APPOINTMENTS MANAGEMENT
/// 1. Create DoctorAppointmentsViewModel
/// 2. Observe all appointments for doctor + pending unassigned:
/// 3. Implement Accept appointment:
/// 4. Implement Decline appointment:
/// 5. Implement Complete appointment:
/// 6. Send push notifications to patient on status changes

data class DoctorAppointment(
    val id: String,
    val patientId: String,
    val patientName: String,
    val patientAge: Int?,
    val patientGender: String?,
    val status: DoctorAppointmentStatus,
    val symptoms: String,
    val description: String,
    val scheduledDate: String? = null,
    val scheduledTime: String? = null,
    val doctorNotes: String? = null,
    val createdAt: String,
    val updatedAt: String
) {
    companion object {
        fun fromDataAppointment(dataAppointment: DataAppointment): DoctorAppointment {
            val patient = FirebaseData.getPatientById(dataAppointment.patientId)
            return DoctorAppointment(
                id = dataAppointment.id,
                patientId = dataAppointment.patientId,
                patientName = patient?.fullName ?: "Unknown Patient",
                patientAge = patient?.age,
                patientGender = patient?.sex?.displayName(),
                status = DoctorAppointmentStatus.fromDataStatus(dataAppointment.status),
                symptoms = dataAppointment.symptoms,
                description = dataAppointment.description,
                scheduledDate = dataAppointment.doctorResponse?.respondedAt?.substring(0, 10),
                scheduledTime = null,
                doctorNotes = dataAppointment.doctorResponse?.let {
                    "${it.diagnosis}\n${it.recommendations}"
                },
                createdAt = dataAppointment.createdAt,
                updatedAt = dataAppointment.updatedAt
            )
        }
    }
}

enum class AppointmentFilter {
    ALL,
    PENDING,
    ACCEPTED,
    COMPLETED,
    DECLINED
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorAppointmentsScreen() {
    var selectedFilter by remember { mutableStateOf(AppointmentFilter.ALL) }
    var selectedAppointment by remember { mutableStateOf<DoctorAppointment?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val currentDoctor = FirebaseData.getCurrentDoctor()

    val allAppointments = remember(currentDoctor) {
        val doctorAppointments = if (currentDoctor != null) {
            FirebaseData.getAppointmentsForDoctor(currentDoctor.id)
        } else {
            emptyList()
        }

        val pendingAppointments = FirebaseData.getPendingAppointments()

        (doctorAppointments + pendingAppointments)
            .distinctBy { it.id }
            .map { DoctorAppointment.fromDataAppointment(it) }
    }

    val filteredAppointments = remember(selectedFilter, allAppointments) {
        when (selectedFilter) {
            AppointmentFilter.ALL -> allAppointments
            AppointmentFilter.PENDING -> allAppointments.filter { it.status == DoctorAppointmentStatus.PENDING }
            AppointmentFilter.ACCEPTED -> allAppointments.filter { it.status == DoctorAppointmentStatus.ACCEPTED }
            AppointmentFilter.COMPLETED -> allAppointments.filter { it.status == DoctorAppointmentStatus.COMPLETED }
            AppointmentFilter.DECLINED -> allAppointments.filter { it.status == DoctorAppointmentStatus.DECLINED }
        }
    }

    if (selectedAppointment != null) {
        ModalBottomSheet(
            onDismissRequest = { selectedAppointment = null },
            sheetState = sheetState,
            containerColor = DoctorHomeColors.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            DoctorAppointmentDetailContent(
                appointment = selectedAppointment!!,
                onDismiss = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        selectedAppointment = null
                    }
                },
                onAccept = {
                    ///TODO: Implement Firebase accept appointment
                    /// viewModel.acceptAppointment(
                    ///     appointmentId = selectedAppointment!!.id,
                    ///     diagnosis = "", // Get from input field
                    ///     recommendations = "" // Get from input field
                    /// ) { result ->
                    ///     result.onSuccess { /* Show success message */ }
                    ///     result.onFailure { /* Show error */ }
                    /// }
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        selectedAppointment = null
                    }
                },
                onDecline = {
                    ///TODO: Implement Firebase decline appointment
                    /// viewModel.declineAppointment(
                    ///     appointmentId = selectedAppointment!!.id,
                    ///     reason = "" // Get from input field
                    /// ) { result ->
                    ///     result.onSuccess { /* Show success message */ }
                    ///     result.onFailure { /* Show error */ }
                    /// }
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        selectedAppointment = null
                    }
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
    ) {
        DoctorAppointmentsHeader()

        AppointmentFilterTabs(
            selectedFilter = selectedFilter,
            onFilterSelected = { selectedFilter = it },
            appointmentCounts = mapOf(
                AppointmentFilter.ALL to allAppointments.size,
                AppointmentFilter.PENDING to allAppointments.count { it.status == DoctorAppointmentStatus.PENDING },
                AppointmentFilter.ACCEPTED to allAppointments.count { it.status == DoctorAppointmentStatus.ACCEPTED },
                AppointmentFilter.COMPLETED to allAppointments.count { it.status == DoctorAppointmentStatus.COMPLETED },
                AppointmentFilter.DECLINED to allAppointments.count { it.status == DoctorAppointmentStatus.DECLINED }
            )
        )

        if (filteredAppointments.isEmpty()) {
            EmptyDoctorAppointmentsView(filter = selectedFilter)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredAppointments) { appointment ->
                    DoctorAppointmentCard(
                        appointment = appointment,
                        onClick = { selectedAppointment = appointment }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}

@Composable
fun DoctorAppointmentsHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Patient Appointments",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = DoctorHomeColors.TextDark
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Manage your patient appointments",
            fontSize = 14.sp,
            color = DoctorHomeColors.TextGray
        )
    }
}

@Composable
fun AppointmentFilterTabs(
    selectedFilter: AppointmentFilter,
    onFilterSelected: (AppointmentFilter) -> Unit,
    appointmentCounts: Map<AppointmentFilter, Int>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AppointmentFilter.entries.forEach { filter ->
            FilterChip(
                filter = filter,
                isSelected = selectedFilter == filter,
                count = appointmentCounts[filter] ?: 0,
                onClick = { onFilterSelected(filter) }
            )
        }
    }
}

@Composable
fun FilterChip(
    filter: AppointmentFilter,
    isSelected: Boolean,
    count: Int,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            when (filter) {
                AppointmentFilter.ALL -> DoctorHomeColors.Primary
                AppointmentFilter.PENDING -> DoctorStatusColors.PendingText
                AppointmentFilter.ACCEPTED -> DoctorStatusColors.AcceptedText
                AppointmentFilter.COMPLETED -> DoctorStatusColors.CompletedText
                AppointmentFilter.DECLINED -> DoctorStatusColors.DeclinedText
            }
        } else {
            Color(0xFFF3F4F6)
        },
        animationSpec = tween(200),
        label = "backgroundColor"
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else DoctorHomeColors.TextGray,
        animationSpec = tween(200),
        label = "textColor"
    )

    val filterText = when (filter) {
        AppointmentFilter.ALL -> "All"
        AppointmentFilter.PENDING -> "Pending"
        AppointmentFilter.ACCEPTED -> "Accepted"
        AppointmentFilter.COMPLETED -> "Completed"
        AppointmentFilter.DECLINED -> "Declined"
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = filterText,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor
            )
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (isSelected) Color.White.copy(alpha = 0.25f) else Color(0xFFE5E7EB))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = count.toString(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else DoctorHomeColors.TextGray
                )
            }
        }
    }
}

@Composable
fun EmptyDoctorAppointmentsView(filter: AppointmentFilter) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = null,
                tint = DoctorHomeColors.TextLightGray,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = when (filter) {
                    AppointmentFilter.ALL -> "No Appointments"
                    AppointmentFilter.PENDING -> "No Pending Appointments"
                    AppointmentFilter.ACCEPTED -> "No Accepted Appointments"
                    AppointmentFilter.COMPLETED -> "No Completed Appointments"
                    AppointmentFilter.DECLINED -> "No Declined Appointments"
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = DoctorHomeColors.TextGray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Appointments will appear here",
                fontSize = 14.sp,
                color = DoctorHomeColors.TextLightGray
            )
        }
    }
}

@Composable
fun DoctorAppointmentCard(
    appointment: DoctorAppointment,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DoctorHomeColors.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DoctorAppointmentStatusBadge(status = appointment.status)
                Text(
                    text = formatDoctorDate(appointment.createdAt),
                    fontSize = 12.sp,
                    color = DoctorHomeColors.TextLightGray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF3F4F6)),
                    contentAlignment = Alignment.Center
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
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DoctorHomeColors.TextDark
                    )
                    if (appointment.patientAge != null && appointment.patientGender != null) {
                        Text(
                            text = "${appointment.patientAge} years old • ${appointment.patientGender}",
                            fontSize = 12.sp,
                            color = DoctorHomeColors.TextGray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.MedicalServices,
                    contentDescription = null,
                    tint = DoctorHomeColors.Primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = appointment.symptoms,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = DoctorHomeColors.TextDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = appointment.description,
                fontSize = 13.sp,
                color = DoctorHomeColors.TextGray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            if (appointment.scheduledDate != null && appointment.scheduledTime != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(DoctorHomeColors.PrimaryLight)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Event,
                        contentDescription = null,
                        tint = DoctorHomeColors.Primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Scheduled: ${appointment.scheduledDate}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = DoctorHomeColors.Primary
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = DoctorHomeColors.Primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = appointment.scheduledTime,
                        fontSize = 12.sp,
                        color = DoctorHomeColors.Primary
                    )
                }
            }
        }
    }
}

@Composable
fun DoctorAppointmentStatusBadge(status: DoctorAppointmentStatus) {
    val (backgroundColor, textColor, icon, text) = when (status) {
        DoctorAppointmentStatus.PENDING -> listOf(
            DoctorStatusColors.PendingBackground,
            DoctorStatusColors.PendingText,
            Icons.Default.HourglassEmpty,
            "Pending"
        )
        DoctorAppointmentStatus.ACCEPTED -> listOf(
            DoctorStatusColors.AcceptedBackground,
            DoctorStatusColors.AcceptedText,
            Icons.Default.CheckCircle,
            "Accepted"
        )
        DoctorAppointmentStatus.DECLINED -> listOf(
            DoctorStatusColors.DeclinedBackground,
            DoctorStatusColors.DeclinedText,
            Icons.Default.Close,
            "Declined"
        )
        DoctorAppointmentStatus.COMPLETED -> listOf(
            DoctorStatusColors.CompletedBackground,
            DoctorStatusColors.CompletedText,
            Icons.Default.CheckCircle,
            "Completed"
        )
    }

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor as Color)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon as ImageVector,
            contentDescription = null,
            tint = textColor as Color,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text as String,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}

@Composable
fun DoctorAppointmentDetailContent(
    appointment: DoctorAppointment,
    onDismiss: () -> Unit,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(40.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color(0xFFE0E0E0))
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Appointment Details",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DoctorHomeColors.TextDark
            )
            DoctorAppointmentStatusBadge(status = appointment.status)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DoctorHomeColors.PrimaryLight)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF3F4F6)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_user_placeholder),
                        contentDescription = "Patient",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = appointment.patientName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DoctorHomeColors.TextDark
                    )
                    if (appointment.patientAge != null && appointment.patientGender != null) {
                        Text(
                            text = "${appointment.patientAge} years old • ${appointment.patientGender}",
                            fontSize = 14.sp,
                            color = DoctorHomeColors.TextGray
                        )
                    }
                    Text(
                        text = "Patient ID: ${appointment.patientId}",
                        fontSize = 12.sp,
                        color = DoctorHomeColors.Primary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        DoctorDetailSection(
            icon = Icons.Default.MedicalServices,
            title = "Symptoms",
            content = appointment.symptoms
        )

        Spacer(modifier = Modifier.height(16.dp))

        DoctorDetailSection(
            icon = Icons.Default.Description,
            title = "Patient's Description",
            content = appointment.description
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Request Date",
                        fontSize = 13.sp,
                        color = DoctorHomeColors.TextGray
                    )
                    Text(
                        text = formatDoctorDateTime(appointment.createdAt),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = DoctorHomeColors.TextDark
                    )
                }
                if (appointment.scheduledDate != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Scheduled For",
                            fontSize = 13.sp,
                            color = DoctorHomeColors.TextGray
                        )
                        Text(
                            text = "${appointment.scheduledDate} at ${appointment.scheduledTime ?: "TBD"}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = DoctorHomeColors.Primary
                        )
                    }
                }
            }
        }

        if (appointment.doctorNotes != null) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Your Notes",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = DoctorHomeColors.TextDark
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when (appointment.status) {
                        DoctorAppointmentStatus.ACCEPTED -> DoctorStatusColors.AcceptedBackground.copy(alpha = 0.5f)
                        DoctorAppointmentStatus.DECLINED -> DoctorStatusColors.DeclinedBackground.copy(alpha = 0.5f)
                        DoctorAppointmentStatus.COMPLETED -> DoctorStatusColors.CompletedBackground.copy(alpha = 0.5f)
                        else -> Color(0xFFF5F5F5)
                    }
                )
            ) {
                Text(
                    text = appointment.doctorNotes,
                    fontSize = 14.sp,
                    color = DoctorHomeColors.TextDark,
                    lineHeight = 20.sp,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (appointment.status == DoctorAppointmentStatus.PENDING) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDecline,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = DoctorStatusColors.DeclinedText
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Decline",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Button(
                    onClick = onAccept,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DoctorStatusColors.AcceptedText
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Accept",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        } else {
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DoctorHomeColors.Primary
                )
            ) {
                Text(
                    text = "Close",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun DoctorDetailSection(
    icon: ImageVector,
    title: String,
    content: String
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = DoctorHomeColors.Primary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = DoctorHomeColors.TextGray
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = content,
            fontSize = 15.sp,
            color = DoctorHomeColors.TextDark,
            lineHeight = 22.sp
        )
    }
}

fun formatDoctorDate(isoDate: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val date = inputFormat.parse(isoDate)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        isoDate
    }
}

fun formatDoctorDateTime(isoDate: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, yyyy 'at' h:mm a", Locale.getDefault())
        val date = inputFormat.parse(isoDate)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        isoDate
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
fun PreviewDoctorAppointmentsScreen() {
    MaterialTheme {
        DoctorAppointmentsScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDoctorAppointmentCard() {
    MaterialTheme {
        DoctorAppointmentCard(
            appointment = DoctorAppointment(
                id = "1",
                patientId = "patient001",
                patientName = "John Smith",
                patientAge = 35,
                patientGender = "Male",
                status = DoctorAppointmentStatus.ACCEPTED,
                symptoms = "Severe Headache, Dizziness",
                description = "I've been experiencing severe headaches for the past week.",
                scheduledDate = "2024-12-10",
                scheduledTime = "10:00 AM",
                createdAt = "2024-12-01T10:30:00Z",
                updatedAt = "2024-12-02T14:20:00Z"
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFilterChips() {
    MaterialTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                filter = AppointmentFilter.ALL,
                isSelected = true,
                count = 8,
                onClick = {}
            )
            FilterChip(
                filter = AppointmentFilter.PENDING,
                isSelected = false,
                count = 3,
                onClick = {}
            )
        }
    }
}
