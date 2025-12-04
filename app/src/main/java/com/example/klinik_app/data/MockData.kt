package com.example.klinik_app.data

import com.example.klinik_app.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

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
    var id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val sex: Sex,
    val birthdate: String, // ISO date format (YYYY-MM-DD)
    val height: String, // e.g., "175 cm"
    val weight: String, // e.g., "70 kg"
    val bloodType: String,
    val imageRes: Int = R.drawable.ic_user_placeholder
) {
    val fullName: String get() = "$firstName $lastName"
    
    // Calculate age from birthdate
    val age: Int get() {
        return try {
            val birthYear = birthdate.substring(0, 4).toInt()
            val currentYear = 2024 // You could use actual current year
            currentYear - birthYear
        } catch (e: Exception) {
            0
        }
    }
}

data class Doctor(
    var id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val sex: Sex,
    val birthdate: String, // ISO date format (YYYY-MM-DD)
    val position: String,
    val field: String,
    val tags: List<String>,
    val description: String,
    val ratings: Double,
    val totalReviews: Int = 0,
    val imageRes: Int = R.drawable.ic_doctor_placeholder
) {
    val fullName: String get() = "Dr. $firstName $lastName"
    
    val age: Int get() {
        return try {
            val birthYear = birthdate.substring(0, 4).toInt()
            val currentYear = 2025  // I really would love to use LocalDate.now() but i'm on Java 24 not 26 soo
            currentYear - birthYear
        } catch (e: Exception) {
            0
        }
    }
}

data class DoctorResponse(
    val doctorId: String,
    val diagnosis: String,
    val recommendations: String,
    val respondedAt: String // ISO date format
)

data class Appointment(
    var id: String,
    val patientId: String,
    val doctorId: String?,
    val status: AppointmentStatus,
    val symptoms: String,
    val description: String,
    val doctorResponse: DoctorResponse?,
    val createdAt: String, ///TODO: Change to com.google.firebase.Timestamp
    val updatedAt: String  ///TODO: Change to com.google.firebase.Timestamp
)

// ==================== MOCK DATA OBJECT ====================

object MockData {
    
    // ==================== HELPER FUNCTIONS ====================
    suspend fun authenticate(email: String, password: String): AuthResult? {

        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()

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

        if (isPatient) { return AuthResult(UserType.PATIENT, uid) }
        else if (isDoctor) { return AuthResult(UserType.DOCTOR, uid) }

        return null;
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

    // get doctor name for an appointment
    suspend fun getDoctorNameForAppointment(appointment: Appointment): String? {
        val doctorId = appointment.doctorId ?: return null
        return getDoctorById(doctorId)?.fullName
    }
    
    // get patient name for an appointment
    suspend fun getPatientNameForAppointment(appointment: Appointment): String? {
        return getPatientById(appointment.patientId)?.fullName
    }

    ///TODO: Replace with Firebase Auth current user management
    /// Use Firebase.auth.currentUser to get logged in user
    /// Store user type in Firestore users collection
    /// Consider using StateFlow for reactive user state
    ///
    /// class UserSessionManager {
    ///     private val auth = Firebase.auth
    ///     private val firestore = Firebase.firestore
    ///
    ///     val currentUserFlow: StateFlow<User?>
    ///     val currentUserTypeFlow: StateFlow<UserType?>
    ///
    ///     suspend fun getCurrentPatient(): Patient? {
    ///         val uid = auth.currentUser?.uid ?: return null
    ///         return firestore.collection("patients").document(uid).get().await().toObject<Patient>()
    ///     }
    ///
    ///     suspend fun getCurrentDoctor(): Doctor? {
    ///         val uid = auth.currentUser?.uid ?: return null
    ///         return firestore.collection("doctors").document(uid).get().await().toObject<Doctor>()
    ///     }
    /// }

    // Default logged-in user (for demo purposes - Patient 1)
    var currentUserId: String = "patient_001"
    var currentUserType: UserType = UserType.PATIENT
    
    fun getCurrentPatient(): Patient? = 
        if (currentUserType == UserType.PATIENT) getPatientById(currentUserId) else null
    
    fun getCurrentDoctor(): Doctor? = 
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
