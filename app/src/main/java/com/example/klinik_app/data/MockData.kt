package com.example.klinik_app.data

import com.example.klinik_app.R

///TODO: FIREBASE SETUP
/// 1. Add Firebase dependencies to build.gradle.kts:
///    - implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
///    - implementation("com.google.firebase:firebase-auth-ktx")
///    - implementation("com.google.firebase:firebase-firestore-ktx")
///    - implementation("com.google.firebase:firebase-storage-ktx")
/// 2. Add google-services.json to app/ folder
/// 3. Add plugin: id("com.google.gms.google-services") to plugins block

///TODO: FIREBASE FIRESTORE COLLECTIONS STRUCTURE
/// - users (collection)
///   - {userId} (document)
///     - userType: "patient" | "doctor"
///     - email: String
///     - createdAt: Timestamp
///
/// - patients (collection)
///   - {patientId} (document)
///     - firstName, lastName, email, sex, birthdate, height, weight, bloodType, imageUrl
///
/// - doctors (collection)
///   - {doctorId} (document)
///     - firstName, lastName, email, sex, birthdate, position, field, tags[], description, ratings, totalReviews, imageUrl
///
/// - appointments (collection)
///   - {appointmentId} (document)
///     - patientId, doctorId, status, symptoms, description, doctorResponse (map), createdAt, updatedAt

///TODO: Create Repository classes for Firebase operations:
/// - AuthRepository: Handle Firebase Authentication
/// - PatientRepository: CRUD operations for patients collection
/// - DoctorRepository: CRUD operations for doctors collection
/// - AppointmentRepository: CRUD operations for appointments collection

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

///TODO: Add Firestore serialization support to Patient class
/// - Add @DocumentId annotation for id field
/// - Add toMap() function for writing to Firestore
/// - Add companion object with fromFirestore(DocumentSnapshot) factory method
/// - Consider using kotlinx.serialization or manual mapping
data class Patient(
    val id: String,
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
    val id: String,
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
            val currentYear = 2024
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

///TODO: Add Firestore serialization support to Appointment class
/// - Add @DocumentId annotation for id field
/// - Add toMap() function for writing to Firestore
/// - Add companion object with fromFirestore(DocumentSnapshot) factory method
/// - Use Timestamp instead of String for createdAt/updatedAt
data class Appointment(
    val id: String,
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

///TODO: Replace MockData object with Firebase repositories
/// Create the following repository classes:
///
/// class AuthRepository {
///     private val auth = Firebase.auth
///     suspend fun signIn(email: String, password: String): Result<FirebaseUser>
///     suspend fun signUp(email: String, password: String): Result<FirebaseUser>
///     suspend fun signOut()
///     fun getCurrentUser(): FirebaseUser?
///     fun isUserLoggedIn(): Boolean
/// }
///
/// class PatientRepository {
///     private val firestore = Firebase.firestore
///     private val patientsCollection = firestore.collection("patients")
///     suspend fun createPatient(patient: Patient): Result<String>
///     suspend fun getPatientById(id: String): Result<Patient?>
///     suspend fun updatePatient(patient: Patient): Result<Unit>
///     fun getPatientFlow(id: String): Flow<Patient?>
/// }
///
/// class DoctorRepository {
///     private val firestore = Firebase.firestore
///     private val doctorsCollection = firestore.collection("doctors")
///     suspend fun createDoctor(doctor: Doctor): Result<String>
///     suspend fun getDoctorById(id: String): Result<Doctor?>
///     suspend fun getAllDoctors(): Result<List<Doctor>>
///     fun getDoctorsFlow(): Flow<List<Doctor>>
/// }
///
/// class AppointmentRepository {
///     private val firestore = Firebase.firestore
///     private val appointmentsCollection = firestore.collection("appointments")
///     suspend fun createAppointment(appointment: Appointment): Result<String>
///     suspend fun updateAppointmentStatus(id: String, status: AppointmentStatus): Result<Unit>
///     suspend fun getAppointmentsForPatient(patientId: String): Result<List<Appointment>>
///     suspend fun getAppointmentsForDoctor(doctorId: String): Result<List<Appointment>>
///     suspend fun getPendingAppointments(): Result<List<Appointment>>
///     fun getAppointmentsFlow(userId: String, userType: UserType): Flow<List<Appointment>>
/// }
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

    ///TODO: Replace with Firebase Authentication
    /// suspend fun authenticate(email: String, password: String): Result<AuthResult> {
    ///     return try {
    ///         val authResult = auth.signInWithEmailAndPassword(email, password).await()
    ///         val userId = authResult.user?.uid ?: throw Exception("User not found")
    ///         val userDoc = firestore.collection("users").document(userId).get().await()
    ///         val userType = UserType.valueOf(userDoc.getString("userType") ?: "PATIENT")
    ///         Result.success(AuthResult(userType, userId))
    ///     } catch (e: Exception) {
    ///         Result.failure(e)
    ///     }
    /// }
    fun authenticate(email: String, password: String): AuthResult? {
        // Check patients
        val patient = patients.find { it.email == email && it.password == password }
        if (patient != null) {
            return AuthResult(UserType.PATIENT, patient.id)
        }
        
        // Check doctors
        val doctor = doctors.find { it.email == email && it.password == password }
        if (doctor != null) {
            return AuthResult(UserType.DOCTOR, doctor.id)
        }
        
        return null
    }
    
    ///TODO: Replace with Firestore query:
    /// suspend fun getPatientById(id: String): Patient? {
    ///     return firestore.collection("patients").document(id).get().await().toObject<Patient>()
    /// }
    fun getPatientById(id: String): Patient? = patients.find { it.id == id }
    
    ///TODO: Replace with Firestore query:
    /// suspend fun getDoctorById(id: String): Doctor? {
    ///     return firestore.collection("doctors").document(id).get().await().toObject<Doctor>()
    /// }
    fun getDoctorById(id: String): Doctor? = doctors.find { it.id == id }
    
    ///TODO: Replace with Firestore query:
    /// suspend fun getAppointmentsForPatient(patientId: String): List<Appointment> {
    ///     return firestore.collection("appointments")
    ///         .whereEqualTo("patientId", patientId)
    ///         .orderBy("createdAt", Query.Direction.DESCENDING)
    ///         .get().await().toObjects<Appointment>()
    /// }
    fun getAppointmentsForPatient(patientId: String): List<Appointment> =
        appointments.filter { it.patientId == patientId }
    
    ///TODO: Replace with Firestore query:
    /// suspend fun getAppointmentsForDoctor(doctorId: String): List<Appointment> {
    ///     return firestore.collection("appointments")
    ///         .whereEqualTo("doctorId", doctorId)
    ///         .orderBy("createdAt", Query.Direction.DESCENDING)
    ///         .get().await().toObjects<Appointment>()
    /// }
    fun getAppointmentsForDoctor(doctorId: String): List<Appointment> =
        appointments.filter { it.doctorId == doctorId }
    
    ///TODO: Replace with Firestore query:
    /// suspend fun getPendingAppointments(): List<Appointment> {
    ///     return firestore.collection("appointments")
    ///         .whereEqualTo("status", AppointmentStatus.PENDING.name)
    ///         .orderBy("createdAt", Query.Direction.DESCENDING)
    ///         .get().await().toObjects<Appointment>()
    /// }
    fun getPendingAppointments(): List<Appointment> =
        appointments.filter { it.status == AppointmentStatus.PENDING }
    
    // get doctor name for an appointment
    fun getDoctorNameForAppointment(appointment: Appointment): String? {
        val doctorId = appointment.doctorId ?: return null
        return getDoctorById(doctorId)?.fullName
    }
    
    // get patient name for an appointment
    fun getPatientNameForAppointment(appointment: Appointment): String? {
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
