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

    // Date picker dialog
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

            // Header with logo and back button
            SignUpHeader(
                currentStep = formState.currentStep,
                onBackClick = formState::goToStep1
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Step indicator
            StepIndicator(
                currentStep = formState.currentStep,
                totalSteps = TOTAL_STEPS
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Main content card
            GlassCard {
                SignUpContent(
                    formState = formState,
                    validator = validator
                )
            }

            // Footer (only on step 1)
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
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = KlinikGlassColors.TextDark
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.klinik_logo),
            contentDescription = "Logo",
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
                    // Handle successful patient sign up
                }
            }
        )
        UserType.DOCTOR -> SignUpStep2DoctorContent(
            state = formState,
            validator = validator,
            onCreateAccount = {
                if (validator.validateStep2Doctor()) {
                    // Handle successful doctor sign up
                }
            }
        )
        null -> { /* Should not happen */ }
    }
}

@Composable
private fun SignUpFooter(onNavigateToSignIn: () -> Unit) {
    Spacer(modifier = Modifier.height(18.dp))

    // Divider with text
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

    // Social login buttons
    Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
        SocialIconGlass(iconRes = R.drawable.ic_facebook)
        SocialIconGlass(iconRes = R.drawable.ic_google)
    }

    Spacer(modifier = Modifier.height(18.dp))

    // Sign in link
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
