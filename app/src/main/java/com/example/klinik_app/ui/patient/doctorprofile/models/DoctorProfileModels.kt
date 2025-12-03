package com.example.klinik_app.ui.patient.doctorprofile.models

import java.time.LocalDate

/**
 * Represents a day in the schedule picker.
 */
data class ScheduleDay(
    val date: LocalDate,
    val dayOfWeek: String,
    val dayOfMonth: Int,
    val isSelected: Boolean = false
)

/**
 * Represents a time slot for booking.
 */
data class TimeSlot(
    val time: String,
    val isSelected: Boolean = false
)

/**
 * Enum representing time of day periods.
 */
enum class TimeOfDay(val displayName: String) {
    MORNING("Morning"),
    AFTERNOON("Afternoon"),
    EVENING("Evening")
}

/**
 * State holder for the doctor profile screen.
 */
data class DoctorProfileState(
    val selectedDayIndex: Int = 0,
    val selectedTimeOfDay: TimeOfDay = TimeOfDay.AFTERNOON,
    val selectedTimeSlotIndex: Int = 0,
    val symptoms: String = "",
    val description: String = ""
)

/**
 * Utility object for generating schedule data.
 */
object ScheduleUtils {
    /**
     * Generates a list of schedule days starting from today.
     */
    fun generateScheduleDays(
        daysCount: Int = 7,
        selectedIndex: Int = 0
    ): List<ScheduleDay> {
        val today = LocalDate.now()
        return (0 until daysCount).map { offset ->
            val date = today.plusDays(offset.toLong())
            ScheduleDay(
                date = date,
                dayOfWeek = date.dayOfWeek.getDisplayName(
                    java.time.format.TextStyle.SHORT,
                    java.util.Locale.ENGLISH
                ),
                dayOfMonth = date.dayOfMonth,
                isSelected = offset == selectedIndex
            )
        }
    }

    /**
     * Returns time slots based on time of day.
     */
    fun getTimeSlotsForTimeOfDay(timeOfDay: TimeOfDay): List<String> {
        return when (timeOfDay) {
            TimeOfDay.MORNING -> listOf("08-09 AM", "09-10 AM", "10-11 AM", "11-12 PM")
            TimeOfDay.AFTERNOON -> listOf("09-10 AM", "10-11 AM", "11-12 AM", "12-01 PM")
            TimeOfDay.EVENING -> listOf("05-06 PM", "06-07 PM", "07-08 PM", "08-09 PM")
        }
    }
}
