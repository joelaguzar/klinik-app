package com.example.klinik_app.ui.auth

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList

enum class UserType {
    PATIENT, DOCTOR
}

enum class Sex {
    MALE, FEMALE, OTHER;

    fun displayName(): String = name.lowercase().replaceFirstChar { it.uppercase() }
}

object BloodTypes {
    val options = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
}

object MedicalFields {
    val options = listOf(
        "Cardiology", "Dermatology", "Endocrinology", "Gastroenterology",
        "General Practice", "Neurology", "Oncology", "Ophthalmology",
        "Orthopedics", "Pediatrics", "Psychiatry", "Pulmonology",
        "Radiology", "Surgery", "Urology", "Other"
    )
}

@Stable
class SignUpFormState {
    var currentStep by mutableStateOf(1)

    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var selectedUserType by mutableStateOf<UserType?>(null)
    var termsAccepted by mutableStateOf(false)

    var isPasswordVisible by mutableStateOf(false)
    var isConfirmPasswordVisible by mutableStateOf(false)
    var showPasswordRequirements by mutableStateOf(false)

    var selectedSex by mutableStateOf<Sex?>(null)
    var birthdate by mutableStateOf<Long?>(null)
    var showDatePicker by mutableStateOf(false)

    var height by mutableStateOf("")
    var weight by mutableStateOf("")
    var bloodType by mutableStateOf("")

    //doctor specific
    var title by mutableStateOf("")
    var field by mutableStateOf("")
    val tags: SnapshotStateList<String> = mutableStateListOf()
    var currentTag by mutableStateOf("")
    var shortIntroduction by mutableStateOf("")

    var firstNameError by mutableStateOf<String?>(null)
    var lastNameError by mutableStateOf<String?>(null)
    var emailError by mutableStateOf<String?>(null)
    var passwordError by mutableStateOf<String?>(null)
    var confirmPasswordError by mutableStateOf<String?>(null)
    var userTypeError by mutableStateOf<String?>(null)
    var termsError by mutableStateOf<String?>(null)

    var sexError by mutableStateOf<String?>(null)
    var birthdateError by mutableStateOf<String?>(null)
    var heightError by mutableStateOf<String?>(null)
    var weightError by mutableStateOf<String?>(null)
    var bloodTypeError by mutableStateOf<String?>(null)
    var titleError by mutableStateOf<String?>(null)
    var fieldError by mutableStateOf<String?>(null)
    var introductionError by mutableStateOf<String?>(null)

    fun goToStep2() {
        currentStep = 2
    }

    fun goToStep1() {
        currentStep = 1
    }

    fun addTag() {
        val trimmedTag = currentTag.trim()
        if (trimmedTag.isNotBlank() && !tags.contains(trimmedTag)) {
            tags.add(trimmedTag)
            currentTag = ""
        }
    }

    fun removeTag(tag: String) {
        tags.remove(tag)
    }

    fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
    }

    fun toggleConfirmPasswordVisibility() {
        isConfirmPasswordVisible = !isConfirmPasswordVisible
    }

    fun togglePasswordRequirements() {
        showPasswordRequirements = !showPasswordRequirements
    }
}
