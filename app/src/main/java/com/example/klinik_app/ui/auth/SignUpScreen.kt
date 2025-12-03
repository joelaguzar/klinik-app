package com.example.klinik_app.ui.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.klinik_app.BackgroundBlobs
import com.example.klinik_app.GlassCard
import com.example.klinik_app.KlinikGlassColors
import com.example.klinik_app.R
import com.example.klinik_app.SocialIconGlass

///TODO: FIREBASE AUTHENTICATION - SIGN UP
/// 1. Create SignUpViewModel to handle registration state
/// 2. Implement Firebase Auth registration:
///    - auth.createUserWithEmailAndPassword(email, password).await()
/// 3. After successful auth, create user document in Firestore:
///    - firestore.collection("users").document(uid).set(mapOf("userType" to userType, "email" to email))
///    - firestore.collection(if (isPatient) "patients" else "doctors").document(uid).set(userData)
/// 4. For profile image upload:
///    - Use Firebase Storage: storage.reference.child("profile_images/$uid.jpg").putFile(imageUri)
///    - Get download URL and store in user document
/// 5. Implement email verification: auth.currentUser?.sendEmailVerification()
/// 6. Handle errors: duplicate email, weak password, network issues

private const val TOTAL_STEPS = 2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KlinikSignUpScreen(
    onNavigateToSignIn: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val formState = remember { SignUpFormState() }
    val validator = remember { SignUpValidator(formState) }
    val datePickerState = rememberDatePickerState()

    if (formState.showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { formState.showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        formState.birthdate = datePickerState.selectedDateMillis
                        validator.clearBirthdateError()
                        formState.showDatePicker = false
                    }
                ) {
                    Text("OK", color = KlinikGlassColors.Blue)
                }
            },
            dismissButton = {
                TextButton(onClick = { formState.showDatePicker = false }) {
                    Text("Cancel", color = KlinikGlassColors.TextGray)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        BackgroundBlobs()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            SignUpHeader(
                currentStep = formState.currentStep,
                onBackClick = formState::goToStep1
            )

            Spacer(modifier = Modifier.height(16.dp))

            StepIndicator(
                currentStep = formState.currentStep,
                totalSteps = TOTAL_STEPS
            )

            Spacer(modifier = Modifier.height(20.dp))

            GlassCard {
                SignUpContent(
                    formState = formState,
                    validator = validator
                )
            }

            if (formState.currentStep == 1) {
                SignUpFooter(onNavigateToSignIn = onNavigateToSignIn)
            } else {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun SignUpHeader(
    currentStep: Int,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        if (currentStep == 2) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(40.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to previous step",
                    tint = KlinikGlassColors.TextDark
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.klinik_logo),
            contentDescription = "Klinik Logo",
            modifier = Modifier.size(80.dp)
        )
    }

    Text(
        text = "Health in your pocket",
        color = KlinikGlassColors.TextGray,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.5.sp,
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Composable
private fun SignUpContent(
    formState: SignUpFormState,
    validator: SignUpValidator
) {
    AnimatedContent(
        targetState = formState.currentStep,
        transitionSpec = {
            if (targetState > initialState) {
                slideInHorizontally { it } + fadeIn() togetherWith 
                    slideOutHorizontally { -it } + fadeOut()
            } else {
                slideInHorizontally { -it } + fadeIn() togetherWith 
                    slideOutHorizontally { it } + fadeOut()
            }
        },
        label = "stepTransition"
    ) { step ->
        when (step) {
            1 -> SignUpStep1Content(
                state = formState,
                validator = validator,
                onContinue = {
                    if (validator.validateStep1()) {
                        formState.goToStep2()
                    }
                }
            )
            2 -> Step2Content(formState = formState, validator = validator)
        }
    }
}

@Composable
private fun Step2Content(
    formState: SignUpFormState,
    validator: SignUpValidator
) {
    when (formState.selectedUserType) {
        UserType.PATIENT -> SignUpStep2PatientContent(
            state = formState,
            validator = validator,
            onCreateAccount = {
                if (validator.validateStep2Patient()) {
                    ///TODO: Implement Firebase patient registration
                    /// viewModel.registerPatient(
                    ///     email = formState.email,
                    ///     password = formState.password,
                    ///     patientData = PatientData(
                    ///         firstName = formState.firstName,
                    ///         lastName = formState.lastName,
                    ///         sex = formState.selectedSex,
                    ///         birthdate = formState.birthdate,
                    ///         height = formState.height,
                    ///         weight = formState.weight,
                    ///         bloodType = formState.bloodType
                    ///     )
                    /// ) { result ->
                    ///     result.onSuccess { onNavigateToSignIn() }
                    ///     result.onFailure { showError(it.message) }
                    /// }
                }
            }
        )
        UserType.DOCTOR -> SignUpStep2DoctorContent(
            state = formState,
            validator = validator,
            onCreateAccount = {
                if (validator.validateStep2Doctor()) {
                    ///TODO: Implement Firebase doctor registration
                    /// viewModel.registerDoctor(
                    ///     email = formState.email,
                    ///     password = formState.password,
                    ///     doctorData = DoctorData(
                    ///         firstName = formState.firstName,
                    ///         lastName = formState.lastName,
                    ///         sex = formState.selectedSex,
                    ///         birthdate = formState.birthdate,
                    ///         title = formState.title,
                    ///         field = formState.field,
                    ///         tags = formState.tags.toList(),
                    ///         description = formState.shortIntroduction,
                    ///         ratings = 0.0,
                    ///         totalReviews = 0
                    ///     )
                    /// ) { result ->
                    ///     result.onSuccess { onNavigateToSignIn() }
                    ///     result.onFailure { showError(it.message) }
                    /// }
                }
            }
        )
        null -> { }
    }
}

@Composable
private fun SignUpFooter(onNavigateToSignIn: () -> Unit) {
    Spacer(modifier = Modifier.height(18.dp))

    Row(verticalAlignment = Alignment.CenterVertically) {
        HorizontalDivider(
            modifier = Modifier.width(60.dp),
            color = Color.LightGray.copy(alpha = 0.5f)
        )
        Text(
            text = "Or sign up with",
            fontSize = 12.sp,
            color = KlinikGlassColors.TextGray,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        HorizontalDivider(
            modifier = Modifier.width(60.dp),
            color = Color.LightGray.copy(alpha = 0.5f)
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
        SocialIconGlass(iconRes = R.drawable.ic_facebook)
        SocialIconGlass(iconRes = R.drawable.ic_google)
    }

    Spacer(modifier = Modifier.height(18.dp))

    Row(modifier = Modifier.padding(bottom = 20.dp)) {
        Text(
            text = "Already have an account? ",
            color = KlinikGlassColors.TextGray,
            fontSize = 14.sp
        )
        Text(
            text = "Sign In",
            color = KlinikGlassColors.Blue,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.clickable { onNavigateToSignIn() }
        )
    }
}

@Preview(showBackground = true, heightDp = 1000)
@Composable
fun PreviewKlinikSignUp() {
    MaterialTheme {
        KlinikSignUpScreen()
    }
}
