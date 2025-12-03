package com.example.klinik_app.ui.patient

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.klinik_app.R
import com.example.klinik_app.ui.patient.doctorprofile.components.BookAppointmentButton
import com.example.klinik_app.ui.patient.doctorprofile.components.ChooseTimesSection
import com.example.klinik_app.ui.patient.doctorprofile.components.DoctorBiographySection
import com.example.klinik_app.ui.patient.doctorprofile.components.DoctorInfoSection
import com.example.klinik_app.ui.patient.doctorprofile.components.DoctorProfileTopBar
import com.example.klinik_app.ui.patient.doctorprofile.components.DoctorTagsRow
import com.example.klinik_app.ui.patient.doctorprofile.components.RequirementsSection
import com.example.klinik_app.ui.patient.doctorprofile.components.SchedulesSection
import com.example.klinik_app.ui.patient.doctorprofile.models.ScheduleUtils
import com.example.klinik_app.ui.patient.doctorprofile.models.TimeOfDay

/**
 * Main doctor profile screen displaying doctor information and appointment booking.
 */
@Composable
fun DoctorProfileScreen(
    doctor: Doctor,
    onBackClick: () -> Unit = {},
    onBookAppointment: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    
    // Schedule state
    var selectedDayIndex by remember { mutableIntStateOf(0) }
    var selectedTimeOfDay by remember { mutableStateOf(TimeOfDay.AFTERNOON) }
    var selectedTimeSlotIndex by remember { mutableIntStateOf(0) }
    
    // Requirements state
    var symptoms by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    
    // Generate schedule days and time slots using utility functions
    val scheduleDays = ScheduleUtils.generateScheduleDays(selectedIndex = selectedDayIndex)
    val timeSlots = ScheduleUtils.getTimeSlotsForTimeOfDay(selectedTimeOfDay)
    
    // Doctor tags
    val doctorTags = listOf("Neurolist", "Neuromedicine", "Medicine")
    
    DoctorProfileContent(
        doctor = doctor,
        doctorTags = doctorTags,
        scheduleDays = scheduleDays,
        selectedDayIndex = selectedDayIndex,
        onDaySelected = { selectedDayIndex = it },
        selectedTimeOfDay = selectedTimeOfDay,
        onTimeOfDaySelected = { selectedTimeOfDay = it },
        timeSlots = timeSlots,
        selectedTimeSlotIndex = selectedTimeSlotIndex,
        onTimeSlotSelected = { selectedTimeSlotIndex = it },
        symptoms = symptoms,
        onSymptomsChange = { symptoms = it },
        description = description,
        onDescriptionChange = { description = it },
        onBackClick = onBackClick,
        onBookAppointment = onBookAppointment,
        scrollState = scrollState
    )
}

/**
 * Stateless content composable for the doctor profile screen.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun DoctorProfileContent(
    doctor: Doctor,
    doctorTags: List<String>,
    scheduleDays: List<com.example.klinik_app.ui.patient.doctorprofile.models.ScheduleDay>,
    selectedDayIndex: Int,
    onDaySelected: (Int) -> Unit,
    selectedTimeOfDay: TimeOfDay,
    onTimeOfDaySelected: (TimeOfDay) -> Unit,
    timeSlots: List<String>,
    selectedTimeSlotIndex: Int,
    onTimeSlotSelected: (Int) -> Unit,
    symptoms: String,
    onSymptomsChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onBookAppointment: () -> Unit,
    scrollState: androidx.compose.foundation.ScrollState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PatientHomeColors.White)
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
    ) {
        // Top Bar
        DoctorProfileTopBar(onBackClick = onBackClick)
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Doctor Info Section
            DoctorInfoSection(doctor = doctor)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Doctor Tags
            DoctorTagsRow(tags = doctorTags)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Doctor Biography
            DoctorBiographySection()
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Schedules Section
            SchedulesSection(
                scheduleDays = scheduleDays,
                selectedDayIndex = selectedDayIndex,
                onDaySelected = onDaySelected
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Choose Times Section
            ChooseTimesSection(
                selectedTimeOfDay = selectedTimeOfDay,
                onTimeOfDaySelected = onTimeOfDaySelected,
                timeSlots = timeSlots,
                selectedTimeSlotIndex = selectedTimeSlotIndex,
                onTimeSlotSelected = onTimeSlotSelected
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Requirements Section
            RequirementsSection(
                symptoms = symptoms,
                onSymptomsChange = onSymptomsChange,
                description = description,
                onDescriptionChange = onDescriptionChange
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Book Appointment Button
            BookAppointmentButton(
                price = "$50.99",
                onClick = onBookAppointment
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
fun PreviewDoctorProfileScreen() {
    val sampleDoctor = Doctor(
        firstName = "Eion",
        lastName = "Morgan",
        field = "Neurology",
        position = "Senior Consultant",
        rating = 4.5,
        appointments = 2530,
        imageRes = R.drawable.ic_doctor_placeholder,
        sex = "Male",
        age = 45
    )
    
    MaterialTheme {
        DoctorProfileScreen(
            doctor = sampleDoctor,
            onBackClick = {}
        )
    }
}
