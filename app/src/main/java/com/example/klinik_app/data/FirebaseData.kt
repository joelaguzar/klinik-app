package com.example.klinik_app.data

import com.example.klinik_app.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import java.time.LocalDate

enum class Sex {
    MALE, FEMALE, OTHER;
    
    fun displayName(): String = name.lowercase().replaceFirstChar { it.uppercase() }
}

enum class AppointmentStatus {
    PENDING,
    ACCEPTED,
    DECLINED,
    COMPLETED
}

// ==================== DATA CLASSES ====================

data class Patient(
    var id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val sex: Sex = Sex.MALE,
    val birthdate: String = "", // ISO date format (YYYY-MM-DD)
    val height: String = "", // e.g., "175 cm"
    val weight: String = "", // e.g., "70 kg"
    val bloodType: String = "",
    val imageRes: Int = R.drawable.ic_user_placeholder
) {
    val fullName: String get() = "$firstName $lastName"
    
    // Calculate age from birthdate
    val age: Int get() {
        return try {
            val birthYear = birthdate.substring(0, 4).toInt()
            val currentYear = 2025
            currentYear - birthYear
        } catch (e: Exception) {
            0
        }
    }
}

data class Doctor(
    var id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val sex: Sex = Sex.MALE,
    val birthdate: String = "", // ISO date format (YYYY-MM-DD)
    val position: String = "",
    val field: String = "",
    val tags: List<String> = emptyList<String>(),
    val description: String = "",
    val ratings: Double = 0.0,
    val totalReviews: Int = 0,
    val imageRes: Int = R.drawable.ic_doctor_placeholder
) {
    val fullName: String get() = "Dr. $firstName $lastName"
    
    val age: Int get() {
        return try {
            val birthYear = birthdate.substring(0, 4).toInt()
            val currentYear = 2025
            currentYear - birthYear
        } catch (e: Exception) {
            0
        }
    }
}

data class DoctorResponse(
    val doctorId: String = "",
    val diagnosis: String = "",
    val recommendations: String = "",
    val respondedAt: String = ""
)

data class Appointment(
    var id: String = "",
    val patientId: String = "",
    val doctorId: String? = "",
    val status: AppointmentStatus = AppointmentStatus.PENDING,
    val symptoms: String = "",
    val description: String = "",
    val doctorResponse: DoctorResponse? = null,
    val createdAt: String = "",
    val updatedAt: String = ""
)

object FirebaseData {

    private var currentUserId: String = "";
    private var currentUserType: UserType = UserType.PATIENT;
    
    suspend fun authenticate(email: String, password: String, selectedType: String): AuthResult? {

        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()

        try {
            val user = auth.signInWithEmailAndPassword(email, password).await()
            val uid = user.user?.uid.toString()

            // check if it's either in patient or doctor collection
            val isPatient = firestore
                .collection("patients")
                .document(uid)
                .get()
                .await()
                .exists()

            val isDoctor = firestore
                .collection("doctors")
                .document(uid)
                .get()
                .await()
                .exists()

            if (isPatient && selectedType == "patient") {
                return AuthResult(UserType.PATIENT, uid)
            } else if (isDoctor && selectedType == "doctor") {
                return AuthResult(UserType.DOCTOR, uid)
            }

            return null;

        } catch (e: Exception) {
            return null;
        }
    }
    
    suspend fun getPatientById(id: String): Patient? {
        val firestore = FirebaseFirestore.getInstance()

        val patient = firestore
            .collection("patients")
            .document(id)
            .get()
            .await()
            .toObject<Patient>() ?: return null

        patient.id = id;
        return patient;
    }

    suspend fun getDoctorById(id: String): Doctor? {

        val firestore = FirebaseFirestore.getInstance();

        val doctor = firestore
            .collection("doctors")
            .document(id)
            .get()
            .await()
            .toObject<Doctor>() ?: return null

        doctor.id = id
        return doctor
    }

    suspend fun getAppointmentsForPatient(patientId: String): List<Appointment> {

        val firestore = FirebaseFirestore.getInstance()
        val documents = firestore
            .collection("appointments")
            .whereEqualTo("patientId", patientId)
            .get()
            .await()


        val appointments: MutableList<Appointment> = mutableListOf();
        for (document in documents) {
            val appointment = document.toObject<Appointment>()
            appointment.id = document.id
            appointments.add(appointment)
        }

        return appointments
    }


    suspend fun getAppointmentsForDoctor(doctorId: String): List<Appointment>{

        val firestore = FirebaseFirestore.getInstance()
        val docs = firestore
            .collection("appointments")
            .whereEqualTo("doctorId", doctorId)
            .get()
            .await()

        val appointments: MutableList<Appointment> = mutableListOf();
        for (document in docs) {
            val appointment = document.toObject<Appointment>()
            appointment.id = document.id
            appointments.add(appointment)
        }

        return appointments
    }


    suspend fun getPendingAppointments(): List<Appointment> {

        val firestore = FirebaseFirestore.getInstance()
        val docs = firestore
            .collection("appointments")
            .whereEqualTo("status", "PENDING")
            .get()
            .await()

        val appointments: MutableList<Appointment> = mutableListOf();
        for (document in docs) {
            val appointment = document.toObject<Appointment>()
            appointment.id = document.id
            appointments.add(appointment)
        }

        return appointments
    }

    suspend fun bookAppointment(
        doctorId: String,
        patientId: String,
        description: String,
        symptoms: String
    ): String? {

        val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
            .format(java.util.Date())

        val appointment = mapOf(
            "patientId" to patientId,
            "doctorId" to doctorId,
            "status" to "PENDING",
            "symptoms" to symptoms,
            "description" to description,
            "createdAt" to today,
            "updatedAt" to today
        )

        val firestore = FirebaseFirestore.getInstance()

        return try {
            val docRef = firestore.collection("appointments")
                .add(appointment)
                .await()

            docRef.id

        } catch (e: Exception) {
            null
        }
    }

    suspend fun acceptAppointment(appointmentId: String): Boolean {
        val firestore = FirebaseFirestore.getInstance()

        return try {
            firestore.collection("appointments")
                .document(appointmentId)
                .update("status", "ACCEPTED")
                .await()

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun declineAppointment(appointmentId: String): Boolean {
        val firestore = FirebaseFirestore.getInstance()

        return try {
            firestore.collection("appointments")
                .document(appointmentId)
                .update("status", "DECLINED")
                .await()

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }



    // get doctor name for an appointment
    suspend fun getDoctorNameForAppointment(appointment: Appointment): String? {
        val doctorId = appointment.doctorId ?: return null
        return getDoctorById(doctorId)?.fullName
    }
    
    // get patient name for an appointment
    suspend fun getPatientNameForAppointment(appointment: Appointment): String? {
        return getPatientById(appointment.patientId)?.fullName
    }

    suspend fun getCurrentPatient(): Patient? =
        if (currentUserType == UserType.PATIENT) getPatientById(currentUserId) else null
    
    suspend fun getCurrentDoctor(): Doctor? =
        if (currentUserType == UserType.DOCTOR) getDoctorById(currentUserId) else null
    
    fun setCurrentUser(userId: String, userType: UserType) {
        currentUserId = userId
        currentUserType = userType
    }
}

enum class UserType {
    PATIENT, DOCTOR
}

data class AuthResult(
    val userType: UserType,
    val userId: String
)
