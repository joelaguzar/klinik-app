package com.example.klinik_app.ui.patient.doctorprofile.components

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
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
import com.example.klinik_app.ui.patient.doctorprofile.models.ScheduleDay
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SchedulesSection(
    scheduleDays: List<ScheduleDay>,
    selectedDayIndex: Int,
    onDaySelected: (Int) -> Unit,
    onMonthPickerClick: () -> Unit = {}
) {
    Column {
        ScheduleHeader(onMonthPickerClick = onMonthPickerClick)
        
        Spacer(modifier = Modifier.height(16.dp))

        ScheduleDaysRow(
            scheduleDays = scheduleDays,
            selectedDayIndex = selectedDayIndex,
            onDaySelected = onDaySelected
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ScheduleHeader(onMonthPickerClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Schedules",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = PatientHomeColors.TextDark
        )
        
        MonthSelector(onClick = onMonthPickerClick)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun MonthSelector(onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        val currentMonth = LocalDate.now().month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
        val currentYear = LocalDate.now().year
        Text(
            text = "$currentMonth $currentYear",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = PatientHomeColors.TextDark
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = "Select month",
            tint = PatientHomeColors.TextDark,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun ScheduleDaysRow(
    scheduleDays: List<ScheduleDay>,
    selectedDayIndex: Int,
    onDaySelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        scheduleDays.forEachIndexed { index, day ->
            ScheduleDayItem(
                day = day,
                isSelected = index == selectedDayIndex,
                onClick = { onDaySelected(index) }
            )
        }
    }
}

@Composable
fun ScheduleDayItem(
    day: ScheduleDay,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(width = 50.dp, height = 70.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) PatientHomeColors.Primary
                else Color.Transparent
            )
            .border(
                width = if (isSelected) 0.dp else 1.dp,
                color = if (isSelected) Color.Transparent else Color(0xFFE5E7EB),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = day.dayOfMonth.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) PatientHomeColors.White else PatientHomeColors.TextDark
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = day.dayOfWeek,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) PatientHomeColors.White.copy(alpha = 0.8f) else PatientHomeColors.TextGray
            )
        }
    }
}
