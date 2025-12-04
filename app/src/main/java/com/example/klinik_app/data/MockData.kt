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
    
    // ==================== 3 PATIENTS ====================
    
    val patients = listOf(
        Patient(
            id = "patient_001",
            firstName = "John",
            lastName = "Williamson",
            email = "john.williamson@email.com",
            password = "password123",
            sex = Sex.MALE,
            birthdate = "1996-05-15",
            height = "175 cm",
            weight = "70 kg",
            bloodType = "O+"
        ),
        Patient(
            id = "patient_002",
            firstName = "Sarah",
            lastName = "Johnson",
            email = "sarah.johnson@email.com",
            password = "password123",
            sex = Sex.FEMALE,
            birthdate = "1992-08-22",
            height = "165 cm",
            weight = "58 kg",
            bloodType = "A+"
        ),
        Patient(
            id = "patient_003",
            firstName = "Michael",
            lastName = "Chen",
            email = "michael.chen@email.com",
            password = "password123",
            sex = Sex.MALE,
            birthdate = "1988-03-10",
            height = "180 cm",
            weight = "82 kg",
            bloodType = "B-"
        )
    )
    
    // ==================== 3 DOCTORS ====================
    
    val doctors = listOf(
        Doctor(
            id = "doctor_001",
            firstName = "Chloe",
            lastName = "Kelly",
            email = "chloe.kelly@klinik.com",
            password = "doctor123",
            sex = Sex.FEMALE,
            birthdate = "1986-07-20",
            position = "Senior Consultant",
            field = "Neurology",
            tags = listOf("Brain Specialist", "Migraine Expert", "Neurological Disorders"),
            description = "Experienced neurologist with over 10 years of practice specializing in treating complex neurological conditions, migraines, and brain disorders. Committed to providing compassionate care and the latest treatment options.",
            ratings = 4.8,
            totalReviews = 2530
        ),
        Doctor(
            id = "doctor_002",
            firstName = "Lauren",
            lastName = "Hemp",
            email = "lauren.hemp@klinik.com",
            password = "doctor123",
            sex = Sex.FEMALE,
            birthdate = "1982-11-05",
            position = "Chief Surgeon",
            field = "Spinal Surgery",
            tags = listOf("Spine Specialist", "Orthopedic Surgery", "Minimally Invasive"),
            description = "Board-certified spinal surgeon with expertise in minimally invasive techniques. Over 15 years of experience in treating spinal disorders, including herniated discs, spinal stenosis, and scoliosis.",
            ratings = 4.9,
            totalReviews = 1850
        ),
        Doctor(
            id = "doctor_003",
            firstName = "James",
            lastName = "Anderson",
            email = "james.anderson@klinik.com",
            password = "doctor123",
            sex = Sex.MALE,
            birthdate = "1979-02-14",
            position = "Head of Cardiology",
            field = "Cardiology",
            tags = listOf("Heart Specialist", "Cardiac Imaging", "Preventive Cardiology"),
            description = "Distinguished cardiologist with expertise in cardiac imaging and preventive cardiology. Dedicated to helping patients maintain heart health through comprehensive diagnosis and personalized treatment plans.",
            ratings = 4.7,
            totalReviews = 3100
        )
    )
    
    // ==================== APPOINTMENTS ====================
    
    val appointments = listOf(
        // Patient 1 (John Williamson) appointments
        Appointment(
            id = "apt_001",
            patientId = "patient_001",
            doctorId = "doctor_001",
            status = AppointmentStatus.ACCEPTED,
            symptoms = "Severe Headache, Dizziness",
            description = "I've been experiencing severe headaches and occasional dizziness for the past week. The pain is mostly on the left side of my head and gets worse in the morning.",
            doctorResponse = DoctorResponse(
                doctorId = "doctor_001",
                diagnosis = "Tension headache with possible migraine component",
                recommendations = "I've reviewed your symptoms. Please come in for a consultation. We'll perform a neurological exam and may order an MRI to rule out other conditions. In the meantime, avoid bright lights and loud sounds.",
                respondedAt = "2024-12-02T14:20:00Z"
            ),
            createdAt = "2024-12-01T10:30:00Z",
            updatedAt = "2024-12-02T14:20:00Z"
        ),
        Appointment(
            id = "apt_002",
            patientId = "patient_001",
            doctorId = null,
            status = AppointmentStatus.PENDING,
            symptoms = "Back Pain, Muscle Stiffness",
            description = "Lower back pain that has been persistent for 2 weeks. Pain increases when sitting for long periods or bending over.",
            doctorResponse = null,
            createdAt = "2024-12-03T09:15:00Z",
            updatedAt = "2024-12-03T09:15:00Z"
        ),
        
        // Patient 2 (Sarah Johnson) appointments
        Appointment(
            id = "apt_003",
            patientId = "patient_002",
            doctorId = "doctor_002",
            status = AppointmentStatus.COMPLETED,
            symptoms = "Neck Pain, Numbness in Arms",
            description = "Experiencing sharp neck pain radiating to both arms. Numbness and tingling in fingers. Had a car accident 3 months ago.",
            doctorResponse = DoctorResponse(
                doctorId = "doctor_002",
                diagnosis = "Cervical disc herniation at C5-C6",
                recommendations = "Treatment completed successfully. The MRI confirmed disc herniation. We've started you on physical therapy and prescribed pain management. Follow-up in 4 weeks to assess progress. Consider surgery if symptoms persist.",
                respondedAt = "2024-11-25T16:30:00Z"
            ),
            createdAt = "2024-11-20T11:00:00Z",
            updatedAt = "2024-11-25T16:30:00Z"
        ),
        Appointment(
            id = "apt_004",
            patientId = "patient_002",
            doctorId = "doctor_003",
            status = AppointmentStatus.ACCEPTED,
            symptoms = "Chest Pain, Shortness of Breath",
            description = "Occasional chest tightness and shortness of breath during exercise. Family history of heart disease.",
            doctorResponse = DoctorResponse(
                doctorId = "doctor_003",
                diagnosis = "Pending cardiac evaluation",
                recommendations = "Given your family history, we need to perform a comprehensive cardiac evaluation. Please fast for 12 hours before your appointment. We'll do an ECG, stress test, and echocardiogram.",
                respondedAt = "2024-12-03T10:00:00Z"
            ),
            createdAt = "2024-12-02T16:45:00Z",
            updatedAt = "2024-12-03T10:00:00Z"
        ),
        
        // Patient 3 (Michael Chen) appointments
        Appointment(
            id = "apt_005",
            patientId = "patient_003",
            doctorId = "doctor_001",
            status = AppointmentStatus.DECLINED,
            symptoms = "Skin Rash, Itching",
            description = "Developed a rash on arms and torso. Red, itchy patches that appeared suddenly.",
            doctorResponse = DoctorResponse(
                doctorId = "doctor_001",
                diagnosis = "Referred to dermatology",
                recommendations = "I apologize, but this condition requires a dermatologist's expertise. I've referred you to our dermatology department. Please book an appointment with Dr. Martinez for proper evaluation.",
                respondedAt = "2024-11-29T09:00:00Z"
            ),
            createdAt = "2024-11-28T16:45:00Z",
            updatedAt = "2024-11-29T09:00:00Z"
        ),
        Appointment(
            id = "apt_006",
            patientId = "patient_003",
            doctorId = "doctor_003",
            status = AppointmentStatus.COMPLETED,
            symptoms = "High Blood Pressure, Fatigue",
            description = "Recent health checkup showed elevated blood pressure (150/95). Feeling tired most of the time.",
            doctorResponse = DoctorResponse(
                doctorId = "doctor_003",
                diagnosis = "Stage 1 Hypertension",
                recommendations = "Your blood pressure readings confirm Stage 1 hypertension. We've started you on a low-dose ACE inhibitor. Please monitor your BP daily, reduce sodium intake, and increase physical activity. Follow-up in 2 weeks.",
                respondedAt = "2024-11-30T14:00:00Z"
            ),
            createdAt = "2024-11-25T09:00:00Z",
            updatedAt = "2024-11-30T14:00:00Z"
        ),
        Appointment(
            id = "apt_007",
            patientId = "patient_003",
            doctorId = null,
            status = AppointmentStatus.PENDING,
            symptoms = "Memory Issues, Difficulty Concentrating",
            description = "Having trouble remembering recent events and difficulty focusing at work. This has been going on for about a month.",
            doctorResponse = null,
            createdAt = "2024-12-03T11:45:00Z",
            updatedAt = "2024-12-03T11:45:00Z"
        )
    )
    
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
