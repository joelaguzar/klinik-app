package com.example.klinik_app.ui.patient.doctorprofile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.klinik_app.ui.patient.PatientHomeColors
import com.example.klinik_app.ui.patient.doctorprofile.models.TimeOfDay

/**
 * Section for selecting appointment time.
 */
@Composable
fun ChooseTimesSection(
    selectedTimeOfDay: TimeOfDay,
    onTimeOfDaySelected: (TimeOfDay) -> Unit,
    timeSlots: List<String>,
    selectedTimeSlotIndex: Int,
    onTimeSlotSelected: (Int) -> Unit
) {
    Column {
        Text(
            text = "Choose Times",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = PatientHomeColors.TextDark
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Time of Day Selection (Morning, Afternoon, Evening)
        TimeOfDaySelector(
            selectedTimeOfDay = selectedTimeOfDay,
            onTimeOfDaySelected = onTimeOfDaySelected
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Time Slot Label
        Text(
            text = "${selectedTimeOfDay.displayName} Schedule",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = PatientHomeColors.TextDark
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Time Slots Row
        TimeSlotsRow(
            timeSlots = timeSlots,
            selectedTimeSlotIndex = selectedTimeSlotIndex,
            onTimeSlotSelected = onTimeSlotSelected
        )
    }
}

/**
 * Segmented control for selecting time of day (Morning, Afternoon, Evening).
 */
@Composable
fun TimeOfDaySelector(
    selectedTimeOfDay: TimeOfDay,
    onTimeOfDaySelected: (TimeOfDay) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(
                width = 1.dp,
                color = Color(0xFFE5E7EB),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TimeOfDay.entries.forEach { timeOfDay ->
            TimeOfDayChip(
                text = timeOfDay.displayName,
                isSelected = selectedTimeOfDay == timeOfDay,
                onClick = { onTimeOfDaySelected(timeOfDay) }
            )
        }
    }
}

/**
 * Individual chip for time of day selection.
 */
@Composable
fun TimeOfDayChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isSelected) PatientHomeColors.Primary
                else Color.Transparent
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) PatientHomeColors.White else PatientHomeColors.TextGray
        )
    }
}

/**
 * Horizontally scrollable row of time slot chips.
 */
@Composable
fun TimeSlotsRow(
    timeSlots: List<String>,
    selectedTimeSlotIndex: Int,
    onTimeSlotSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        timeSlots.forEachIndexed { index, slot ->
            TimeSlotChip(
                time = slot,
                isSelected = index == selectedTimeSlotIndex,
                onClick = { onTimeSlotSelected(index) }
            )
        }
    }
}

/**
 * Individual chip for time slot selection.
 */
@Composable
fun TimeSlotChip(
    time: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isSelected) PatientHomeColors.Primary
                else Color.Transparent
            )
            .border(
                width = if (isSelected) 0.dp else 1.dp,
                color = if (isSelected) Color.Transparent else Color(0xFFE5E7EB),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = time,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) PatientHomeColors.White else PatientHomeColors.TextDark
        )
    }
}
