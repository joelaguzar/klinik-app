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
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
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
import com.example.klinik_app.data.Patient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.klinik_app.data.Appointment as DataAppointment
import com.example.klinik_app.data.AppointmentStatus as DataAppointmentStatus
import com.example.klinik_app.data.DoctorResponse as DataDoctorResponse

enum class AppointmentStatus {
    PENDING,
    ACCEPTED,
    DECLINED,
    COMPLETED;
    
    companion object {
        fun fromDataStatus(status: DataAppointmentStatus): AppointmentStatus {
            return when (status) {
                DataAppointmentStatus.PENDING -> PENDING
                DataAppointmentStatus.ACCEPTED -> ACCEPTED
                DataAppointmentStatus.DECLINED -> DECLINED
                DataAppointmentStatus.COMPLETED -> COMPLETED
            }
        }
    }
}

data class DoctorResponse(
    val message: String = "",
    val scheduledDate: String? = null,
    val scheduledTime: String? = null,
    val notes: String? = null
) {
    companion object {
        fun fromDataResponse(response: DataDoctorResponse?): DoctorResponse? {
            if (response == null) return null
            return DoctorResponse(
                message = response.recommendations,
                scheduledDate = response.respondedAt.substring(0, 10),
                scheduledTime = null,
                notes = response.diagnosis
            )
        }
    }
}

data class Appointment(
    val id: String,
    val patientId: String,
    val doctorId: String? = null,
    val doctorName: String? = null,
    val status: AppointmentStatus,
    val symptoms: String,
    val description: String,
    val doctorResponse: DoctorResponse? = null,
    val createdAt: String,
    val updatedAt: String
) {
    companion object {
        suspend fun fromDataAppointment(dataAppointment: DataAppointment): Appointment {
            return Appointment(
                id = dataAppointment.id,
                patientId = dataAppointment.patientId,
                doctorId = dataAppointment.doctorId,
                doctorName = FirebaseData.getDoctorNameForAppointment(dataAppointment),
                status = AppointmentStatus.fromDataStatus(dataAppointment.status),
                symptoms = dataAppointment.symptoms,
                description = dataAppointment.description,
                doctorResponse = DoctorResponse.fromDataResponse(dataAppointment.doctorResponse),
                createdAt = dataAppointment.createdAt,
                updatedAt = dataAppointment.updatedAt
            )
        }
    }
}

object AppointmentStatusColors {
    val PendingBackground = Color(0xFFFFF3CD)
    val PendingText = Color(0xFF856404)
    val AcceptedBackground = Color(0xFFD4EDDA)
    val AcceptedText = Color(0xFF155724)
    val DeclinedBackground = Color(0xFFF8D7DA)
    val DeclinedText = Color(0xFF721C24)
    val CompletedBackground = Color(0xFFD1ECF1)
    val CompletedText = Color(0xFF0C5460)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen() {
    var selectedAppointment by remember { mutableStateOf<Appointment?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val currentPatient by produceState<Patient?>(initialValue = null) {
        value = FirebaseData.getCurrentPatient()
    }

    val appointments by produceState<List<Appointment>>(initialValue = emptyList(), currentPatient) {
        if (currentPatient != null) {
            val rawData = FirebaseData.getAppointmentsForPatient(currentPatient!!.id)
            value = rawData.map { Appointment.fromDataAppointment(it) }
        } else {
            value = emptyList()
        }
    }

    if (selectedAppointment != null) {
        ModalBottomSheet(
            onDismissRequest = { selectedAppointment = null },
            sheetState = sheetState,
            containerColor = PatientHomeColors.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            AppointmentDetailContent(
                appointment = selectedAppointment!!,
                onDismiss = {
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
        AppointmentsHeader()

        if (appointments.isEmpty()) {
            EmptyAppointmentsView()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(appointments) { appointment ->
                    AppointmentCard(
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
fun AppointmentsHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "My Appointments",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PatientHomeColors.TextDark
        )

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(PatientHomeColors.PrimaryLight)
                .clickable { },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "Filter",
                tint = PatientHomeColors.Primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun EmptyAppointmentsView() {
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
                tint = PatientHomeColors.TextLightGray,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No Appointments",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = PatientHomeColors.TextGray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your appointments will appear here",
                fontSize = 14.sp,
                color = PatientHomeColors.TextLightGray
            )
        }
    }
}

@Composable
fun AppointmentCard(
    appointment: Appointment,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PatientHomeColors.White),
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
                StatusBadge(status = appointment.status)
                Text(
                    text = formatDate(appointment.createdAt),
                    fontSize = 12.sp,
                    color = PatientHomeColors.TextLightGray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.MedicalServices,
                    contentDescription = null,
                    tint = PatientHomeColors.Primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = appointment.symptoms,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PatientHomeColors.TextDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = appointment.description,
                fontSize = 13.sp,
                color = PatientHomeColors.TextGray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (appointment.doctorName != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(PatientHomeColors.PrimaryLight)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = PatientHomeColors.Primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = appointment.doctorName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = PatientHomeColors.Primary
                    )

                    if (appointment.doctorResponse?.scheduledTime != null) {
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = PatientHomeColors.Primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = appointment.doctorResponse.scheduledTime,
                            fontSize = 12.sp,
                            color = PatientHomeColors.Primary
                        )
                    }
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF5F5F5))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.HourglassEmpty,
                        contentDescription = null,
                        tint = PatientHomeColors.TextLightGray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Awaiting doctor assignment",
                        fontSize = 13.sp,
                        color = PatientHomeColors.TextLightGray
                    )
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: AppointmentStatus) {
    val (backgroundColor, textColor, icon, text) = when (status) {
        AppointmentStatus.PENDING -> listOf(
            AppointmentStatusColors.PendingBackground,
            AppointmentStatusColors.PendingText,
            Icons.Default.HourglassEmpty,
            "Pending"
        )
        AppointmentStatus.ACCEPTED -> listOf(
            AppointmentStatusColors.AcceptedBackground,
            AppointmentStatusColors.AcceptedText,
            Icons.Default.CheckCircle,
            "Accepted"
        )
        AppointmentStatus.DECLINED -> listOf(
            AppointmentStatusColors.DeclinedBackground,
            AppointmentStatusColors.DeclinedText,
            Icons.Default.Close,
            "Declined"
        )
        AppointmentStatus.COMPLETED -> listOf(
            AppointmentStatusColors.CompletedBackground,
            AppointmentStatusColors.CompletedText,
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
fun AppointmentDetailContent(
    appointment: Appointment,
    onDismiss: () -> Unit
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
                color = PatientHomeColors.TextDark
            )
            StatusBadge(status = appointment.status)
        }

        Spacer(modifier = Modifier.height(24.dp))

        DetailSection(
            icon = Icons.Default.MedicalServices,
            title = "Symptoms",
            content = appointment.symptoms
        )

        Spacer(modifier = Modifier.height(16.dp))

        DetailSection(
            icon = Icons.Default.Description,
            title = "Description",
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
                        text = "Created",
                        fontSize = 13.sp,
                        color = PatientHomeColors.TextGray
                    )
                    Text(
                        text = formatDateTime(appointment.createdAt),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = PatientHomeColors.TextDark
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Last Updated",
                        fontSize = 13.sp,
                        color = PatientHomeColors.TextGray
                    )
                    Text(
                        text = formatDateTime(appointment.updatedAt),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = PatientHomeColors.TextDark
                    )
                }
            }
        }

        if (appointment.doctorName != null) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Assigned Doctor",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = PatientHomeColors.TextDark
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = PatientHomeColors.PrimaryLight)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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
                            painter = painterResource(id = R.drawable.ic_doctor_placeholder),
                            contentDescription = "Doctor",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = appointment.doctorName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PatientHomeColors.TextDark
                        )
                        if (appointment.doctorResponse?.scheduledDate != null) {
                            Text(
                                text = "Scheduled: ${appointment.doctorResponse.scheduledDate} at ${appointment.doctorResponse.scheduledTime ?: "TBD"}",
                                fontSize = 13.sp,
                                color = PatientHomeColors.Primary
                            )
                        }
                    }
                }
            }
        }

        if (appointment.doctorResponse != null) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Doctor's Response",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = PatientHomeColors.TextDark
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when (appointment.status) {
                        AppointmentStatus.ACCEPTED -> AppointmentStatusColors.AcceptedBackground.copy(alpha = 0.5f)
                        AppointmentStatus.DECLINED -> AppointmentStatusColors.DeclinedBackground.copy(alpha = 0.5f)
                        AppointmentStatus.COMPLETED -> AppointmentStatusColors.CompletedBackground.copy(alpha = 0.5f)
                        else -> Color(0xFFF5F5F5)
                    }
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = appointment.doctorResponse.message,
                        fontSize = 14.sp,
                        color = PatientHomeColors.TextDark,
                        lineHeight = 20.sp
                    )

                    if (appointment.doctorResponse.notes != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "Note: ",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = PatientHomeColors.TextGray
                            )
                            Text(
                                text = appointment.doctorResponse.notes,
                                fontSize = 13.sp,
                                color = PatientHomeColors.TextGray,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                        }
                    }
                }
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
                text = "Close",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Composable
fun DetailSection(
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
                tint = PatientHomeColors.Primary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = PatientHomeColors.TextGray
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = content,
            fontSize = 15.sp,
            color = PatientHomeColors.TextDark,
            lineHeight = 22.sp
        )
    }
}

fun formatDate(isoDate: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val date = inputFormat.parse(isoDate)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        isoDate
    }
}

fun formatDateTime(isoDate: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, yyyy 'at' h:mm a", Locale.getDefault())
        val date = inputFormat.parse(isoDate)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        isoDate
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
fun PreviewAppointmentsScreen() {
    MaterialTheme {
        AppointmentsScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAppointmentCard() {
    MaterialTheme {
        AppointmentCard(
            appointment = Appointment(
                id = "1",
                patientId = "patient123",
                doctorId = "doctor456",
                doctorName = "Dr. Chloe Kelly",
                status = AppointmentStatus.ACCEPTED,
                symptoms = "Headache, Dizziness",
                description = "I've been experiencing severe headaches for the past week.",
                doctorResponse = DoctorResponse(
                    message = "Please come in for a consultation.",
                    scheduledDate = "2024-12-10",
                    scheduledTime = "10:00 AM"
                ),
                createdAt = "2024-12-01T10:30:00Z",
                updatedAt = "2024-12-02T14:20:00Z"
            ),
            onClick = {}
        )
    }
}
