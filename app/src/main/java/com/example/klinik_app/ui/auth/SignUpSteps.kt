package com.example.klinik_app.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Bloodtype
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Tag
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.klinik_app.KlinikGlassColors


@Composable
fun SignUpStep1Content(
    state: SignUpFormState,
    validator: SignUpValidator,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        StepHeader(
            title = "Create Account",
            subtitle = "Join us and take control of your health."
        )

        // Personalized welcome message
        WelcomePersonalization(firstName = state.firstName)

        // User type selection
        SectionLabel(text = "I am a")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            UserTypeButton(
                text = "Patient",
                icon = Icons.Outlined.PersonOutline,
                isSelected = state.selectedUserType == UserType.PATIENT,
                onClick = {
                    state.selectedUserType = UserType.PATIENT
                    validator.clearUserTypeError()
                },
                modifier = Modifier.weight(1f)
            )
            UserTypeButton(
                text = "Doctor",
                icon = Icons.Outlined.LocalHospital,
                isSelected = state.selectedUserType == UserType.DOCTOR,
                onClick = {
                    state.selectedUserType = UserType.DOCTOR
                    validator.clearUserTypeError()
                },
                modifier = Modifier.weight(1f)
            )
        }

        ValidationError(state.userTypeError)

        Spacer(modifier = Modifier.height(20.dp))

        // Form fields
        SignUpTextField(
            value = state.firstName,
            onValueChange = {
                state.firstName = it
                if (state.firstNameError != null) validator.validateFirstName()
            },
            placeholder = "First Name",
            icon = Icons.Default.Person,
            errorMessage = state.firstNameError
        )

        FieldSpacer()

        SignUpTextField(
            value = state.lastName,
            onValueChange = {
                state.lastName = it
                if (state.lastNameError != null) validator.validateLastName()
            },
            placeholder = "Last Name",
            icon = Icons.Default.Person,
            errorMessage = state.lastNameError
        )

        FieldSpacer()

        SignUpTextField(
            value = state.email,
            onValueChange = {
                state.email = it
                if (state.emailError != null) validator.validateEmail()
            },
            placeholder = "Email Address",
            icon = Icons.Default.Email,
            errorMessage = state.emailError,
            keyboardType = KeyboardType.Email
        )

        FieldSpacer()

        // Password field with strength indicator
        Column(modifier = Modifier.fillMaxWidth()) {
            SignUpTextField(
                value = state.password,
                onValueChange = {
                    state.password = it
                    if (state.passwordError != null) validator.validatePassword()
                    if (state.confirmPassword.isNotEmpty()) validator.validateConfirmPassword()
                },
                placeholder = "Password",
                icon = Icons.Default.Lock,
                isPassword = true,
                isVisible = state.isPasswordVisible,
                onToggleVisibility = state::togglePasswordVisibility,
                errorMessage = state.passwordError
            )
            
            // Password strength indicator
            PasswordStrengthIndicator(password = state.password)
            
            // Password requirements hint
            if (state.password.isNotEmpty()) {
                PasswordRequirementsHint(password = state.password)
            }
        }

        FieldSpacer()

        SignUpTextField(
            value = state.confirmPassword,
            onValueChange = {
                state.confirmPassword = it
                if (state.confirmPasswordError != null) validator.validateConfirmPassword()
            },
            placeholder = "Confirm Password",
            icon = Icons.Default.Lock,
            isPassword = true,
            isVisible = state.isConfirmPasswordVisible,
            onToggleVisibility = state::toggleConfirmPasswordVisibility,
            errorMessage = state.confirmPasswordError
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Terms and Conditions
        TermsAndConditionsCheckbox(
            isChecked = state.termsAccepted,
            onCheckedChange = { 
                state.termsAccepted = it
                if (it) validator.clearTermsError()
            },
            errorMessage = state.termsError
        )

        Spacer(modifier = Modifier.height(24.dp))

        GradientButton(
            text = "Continue",
            onClick = onContinue
        )
    }
}

@Composable
fun SignUpStep2PatientContent(
    state: SignUpFormState,
    validator: SignUpValidator,
    onCreateAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StepHeader(
            title = "Personal Details",
            subtitle = "Help us personalize your experience."
        )

        SectionLabel(text = "Sex")

        SexSelectionRow(
            selectedSex = state.selectedSex,
            onSexSelected = {
                state.selectedSex = it
                validator.clearSexError()
            }
        )

        ValidationError(state.sexError)

        FieldSpacer()

        // Birthdate
        DatePickerField(
            label = "Birthdate",
            selectedDate = state.birthdate,
            onClick = { state.showDatePicker = true },
            errorMessage = state.birthdateError
        )

        FieldSpacer()

        // Height and Weight
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SignUpTextField(
                value = state.height,
                onValueChange = { input ->
                    if (input.all { it.isDigit() || it == '.' }) {
                        state.height = input
                        if (state.heightError != null) validator.validateHeight()
                    }
                },
                placeholder = "Height (cm)",
                icon = Icons.Default.Height,
                errorMessage = state.heightError,
                keyboardType = KeyboardType.Decimal,
                modifier = Modifier.weight(1f)
            )
            SignUpTextField(
                value = state.weight,
                onValueChange = { input ->
                    if (input.all { it.isDigit() || it == '.' }) {
                        state.weight = input
                        if (state.weightError != null) validator.validateWeight()
                    }
                },
                placeholder = "Weight (kg)",
                icon = Icons.Default.MonitorWeight,
                errorMessage = state.weightError,
                keyboardType = KeyboardType.Decimal,
                modifier = Modifier.weight(1f)
            )
        }

        FieldSpacer()

        // Blood Type
        DropdownField(
            label = "Blood Type",
            selectedValue = state.bloodType,
            options = BloodTypes.options,
            onValueChange = {
                state.bloodType = it
                validator.clearBloodTypeError()
            },
            icon = Icons.Outlined.Bloodtype,
            errorMessage = state.bloodTypeError
        )

        Spacer(modifier = Modifier.height(32.dp))

        GradientButton(
            text = "Create Account",
            onClick = onCreateAccount
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SignUpStep2DoctorContent(
    state: SignUpFormState,
    validator: SignUpValidator,
    onCreateAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StepHeader(
            title = "Professional Details",
            subtitle = "Tell patients about yourself."
        )

        // Sex selection
        SectionLabel(text = "Sex")

        SexSelectionRow(
            selectedSex = state.selectedSex,
            onSexSelected = {
                state.selectedSex = it
                validator.clearSexError()
            }
        )

        ValidationError(state.sexError)

        FieldSpacer()

        // Birthdate
        DatePickerField(
            label = "Birthdate",
            selectedDate = state.birthdate,
            onClick = { state.showDatePicker = true },
            errorMessage = state.birthdateError
        )

        FieldSpacer()

        // Title/Position
        SignUpTextField(
            value = state.title,
            onValueChange = {
                state.title = it
                if (state.titleError != null) validator.validateTitle()
            },
            placeholder = "Title/Position (e.g., MD, Specialist)",
            icon = Icons.Default.Work,
            errorMessage = state.titleError
        )

        FieldSpacer()

        // Field dropdown
        DropdownField(
            label = "Field of Practice",
            selectedValue = state.field,
            options = MedicalFields.options,
            onValueChange = {
                state.field = it
                validator.clearFieldError()
            },
            icon = Icons.Outlined.MedicalServices,
            errorMessage = state.fieldError
        )

        FieldSpacer()

        // Tags section
        Column(modifier = Modifier.fillMaxWidth()) {
            SectionLabel(text = "Specialization Tags")

            TagInputField(
                value = state.currentTag,
                onValueChange = { state.currentTag = it },
                onAddTag = {
                    state.addTag()
                    focusManager.clearFocus()
                },
                leadingIcon = Icons.Outlined.Tag
            )

            if (state.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.tags.forEach { tag ->
                        TagChip(
                            text = tag,
                            onRemove = { state.removeTag(tag) }
                        )
                    }
                }
            }
        }

        FieldSpacer()

        // Short Introduction
        MultiLineTextField(
            value = state.shortIntroduction,
            onValueChange = {
                state.shortIntroduction = it
                if (state.introductionError != null) validator.validateIntroduction()
            },
            placeholder = "Write a short introduction about yourself...",
            errorMessage = state.introductionError
        )

        Spacer(modifier = Modifier.height(32.dp))

        GradientButton(
            text = "Create Account",
            onClick = onCreateAccount
        )
    }
}

@Composable
private fun StepHeader(
    title: String,
    subtitle: String
) {
    Text(
        text = title,
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        color = KlinikGlassColors.TextDark
    )
    Text(
        text = subtitle,
        fontSize = 14.sp,
        color = KlinikGlassColors.TextGray,
        modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
    )
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = KlinikGlassColors.TextGray,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    )
}

@Composable
private fun SexSelectionRow(
    selectedSex: Sex?,
    onSexSelected: (Sex) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Sex.entries.forEach { sex ->
            SexButton(
                text = sex.displayName(),
                isSelected = selectedSex == sex,
                onClick = { onSexSelected(sex) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ValidationError(error: String?) {
    if (error != null) {
        Text(
            text = error,
            color = Color(0xFFDC2626),
            fontSize = 12.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, start = 4.dp)
        )
    }
}

@Composable
private fun FieldSpacer() {
    Spacer(modifier = Modifier.height(16.dp))
}
