package com.example.klinik_app.ui.auth

import android.util.Patterns

class SignUpValidator(private val state: SignUpFormState) {

    fun validateFirstName(): Boolean {
        state.firstNameError = when {
            state.firstName.isBlank() -> "First name is required"
            state.firstName.length < 2 -> "First name must be at least 2 characters"
            !state.firstName.all { it.isLetter() || it.isWhitespace() } -> "First name can only contain letters"
            else -> null
        }
        return state.firstNameError == null
    }

    fun validateLastName(): Boolean {
        state.lastNameError = when {
            state.lastName.isBlank() -> "Last name is required"
            state.lastName.length < 2 -> "Last name must be at least 2 characters"
            !state.lastName.all { it.isLetter() || it.isWhitespace() } -> "Last name can only contain letters"
            else -> null
        }
        return state.lastNameError == null
    }

    fun validateEmail(): Boolean {
        state.emailError = when {
            state.email.isBlank() -> "Email is required"
            !Patterns.EMAIL_ADDRESS.matcher(state.email).matches() -> "Please enter a valid email address"
            else -> null
        }
        return state.emailError == null
    }

    fun validatePassword(): Boolean {
        state.passwordError = when {
            state.password.isBlank() -> "Password is required"
            state.password.length < 8 -> "Password must be at least 8 characters"
            !state.password.any { it.isUpperCase() } -> "Password must contain at least one uppercase letter"
            !state.password.any { it.isLowerCase() } -> "Password must contain at least one lowercase letter"
            !state.password.any { it.isDigit() } -> "Password must contain at least one number"
            else -> null
        }
        return state.passwordError == null
    }

    fun validateConfirmPassword(): Boolean {
        state.confirmPasswordError = when {
            state.confirmPassword.isBlank() -> "Please confirm your password"
            state.confirmPassword != state.password -> "Passwords do not match"
            else -> null
        }
        return state.confirmPasswordError == null
    }

    fun validateUserType(): Boolean {
        state.userTypeError = if (state.selectedUserType == null) "Please select account type" else null
        return state.userTypeError == null
    }

    fun validateTerms(): Boolean {
        state.termsError = if (!state.termsAccepted) "You must accept the Terms and Privacy Policy" else null
        return state.termsError == null
    }

    fun clearTermsError() {
        state.termsError = null
    }

    fun validateStep1(): Boolean {
        val results = listOf(
            validateFirstName(),
            validateLastName(),
            validateEmail(),
            validatePassword(),
            validateConfirmPassword(),
            validateUserType(),
            validateTerms()
        )
        return results.all { it }
    }

    fun validateSex(): Boolean {
        state.sexError = if (state.selectedSex == null) "Please select your sex" else null
        return state.sexError == null
    }

    fun validateBirthdate(): Boolean {
        state.birthdateError = if (state.birthdate == null) "Please select your birthdate" else null
        return state.birthdateError == null
    }

    fun validateHeight(): Boolean {
        state.heightError = when {
            state.height.isBlank() -> "Height is required"
            state.height.toFloatOrNull() == null -> "Please enter a valid number"
            state.height.toFloat() < 50 || state.height.toFloat() > 300 -> "Please enter a valid height (50-300 cm)"
            else -> null
        }
        return state.heightError == null
    }

    fun validateWeight(): Boolean {
        state.weightError = when {
            state.weight.isBlank() -> "Weight is required"
            state.weight.toFloatOrNull() == null -> "Please enter a valid number"
            state.weight.toFloat() < 10 || state.weight.toFloat() > 500 -> "Please enter a valid weight (10-500 kg)"
            else -> null
        }
        return state.weightError == null
    }

    fun validateBloodType(): Boolean {
        state.bloodTypeError = if (state.bloodType.isBlank()) "Please select your blood type" else null
        return state.bloodTypeError == null
    }

    fun validateStep2Patient(): Boolean {
        val results = listOf(
            validateSex(),
            validateBirthdate(),
            validateHeight(),
            validateWeight(),
            validateBloodType()
        )
        return results.all { it }
    }

    fun validateTitle(): Boolean {
        state.titleError = when {
            state.title.isBlank() -> "Title/Position is required"
            state.title.length < 2 -> "Title must be at least 2 characters"
            else -> null
        }
        return state.titleError == null
    }

    fun validateField(): Boolean {
        state.fieldError = if (state.field.isBlank()) "Please select your field" else null
        return state.fieldError == null
    }

    fun validateIntroduction(): Boolean {
        state.introductionError = when {
            state.shortIntroduction.isBlank() -> "Short introduction is required"
            state.shortIntroduction.length < 20 -> "Introduction must be at least 20 characters"
            state.shortIntroduction.length > 500 -> "Introduction must be less than 500 characters"
            else -> null
        }
        return state.introductionError == null
    }

    fun validateStep2Doctor(): Boolean {
        val results = listOf(
            validateSex(),
            validateBirthdate(),
            validateTitle(),
            validateField(),
            validateIntroduction()
        )
        return results.all { it }
    }

    fun clearUserTypeError() {
        state.userTypeError = null
    }

    fun clearSexError() {
        state.sexError = null
    }

    fun clearBirthdateError() {
        state.birthdateError = null
    }

    fun clearBloodTypeError() {
        state.bloodTypeError = null
    }

    fun clearFieldError() {
        state.fieldError = null
    }
}
