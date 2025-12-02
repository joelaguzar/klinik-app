package com.example.klinik_app.ui.auth

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.klinik_app.BackgroundBlobs
import com.example.klinik_app.GlassCard
import com.example.klinik_app.KlinikGlassColors
import com.example.klinik_app.R
import com.example.klinik_app.SocialIconGlass

enum class UserType {
    PATIENT, DOCTOR
}

@Composable
fun KlinikSignUpScreen(
    onNavigateToSignIn: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    // form state
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedUserType by remember { mutableStateOf<UserType?>(null) }

    // visibility state
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    // Validation states
    var firstNameError by remember { mutableStateOf<String?>(null) }
    var lastNameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var userTypeError by remember { mutableStateOf<String?>(null) }

    // validationsss
    fun validateFirstName(): Boolean {
        firstNameError = when {
            firstName.isBlank() -> "First name is required"
            firstName.length < 2 -> "First name must be at least 2 characters"
            !firstName.all { it.isLetter() || it.isWhitespace() } -> "First name can only contain letters"
            else -> null
        }
        return firstNameError == null
    }

    fun validateLastName(): Boolean {
        lastNameError = when {
            lastName.isBlank() -> "Last name is required"
            lastName.length < 2 -> "Last name must be at least 2 characters"
            !lastName.all { it.isLetter() || it.isWhitespace() } -> "Last name can only contain letters"
            else -> null
        }
        return lastNameError == null
    }

    fun validateEmail(): Boolean {
        val emailPattern = android.util.Patterns.EMAIL_ADDRESS
        emailError = when {
            email.isBlank() -> "Email is required"
            !emailPattern.matcher(email).matches() -> "Please enter a valid email address"
            else -> null
        }
        return emailError == null
    }

    fun validatePassword(): Boolean {
        passwordError = when {
            password.isBlank() -> "Password is required"
            password.length < 8 -> "Password must be at least 8 characters"
            !password.any { it.isUpperCase() } -> "Password must contain at least one uppercase letter"
            !password.any { it.isLowerCase() } -> "Password must contain at least one lowercase letter"
            !password.any { it.isDigit() } -> "Password must contain at least one number"
            else -> null
        }
        return passwordError == null
    }

    fun validateConfirmPassword(): Boolean {
        confirmPasswordError = when {
            confirmPassword.isBlank() -> "Please confirm your password"
            confirmPassword != password -> "Passwords do not match"
            else -> null
        }
        return confirmPasswordError == null
    }

    fun validateUserType(): Boolean {
        userTypeError = if (selectedUserType == null) "Please select account type" else null
        return userTypeError == null
    }

    fun validateAll(): Boolean {
        val isFirstNameValid = validateFirstName()
        val isLastNameValid = validateLastName()
        val isEmailValid = validateEmail()
        val isPasswordValid = validatePassword()
        val isConfirmPasswordValid = validateConfirmPassword()
        val isUserTypeValid = validateUserType()

        return isFirstNameValid && isLastNameValid && isEmailValid &&
                isPasswordValid && isConfirmPasswordValid && isUserTypeValid
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Background blobs from sign in screen
        BackgroundBlobs()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // header section - centered logo
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.klinik_logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(100.dp)
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

            Spacer(modifier = Modifier.height(28.dp))

            // card section
            GlassCard {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Create Account",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = KlinikGlassColors.TextDark
                    )
                    Text(
                        text = "Join us and take control of your health.",
                        fontSize = 14.sp,
                        color = KlinikGlassColors.TextGray,
                        modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
                    )

                    // user type selectio
                    Text(
                        text = "I am a",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = KlinikGlassColors.TextGray,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        UserTypeButton(
                            text = "Patient",
                            icon = Icons.Outlined.PersonOutline,
                            isSelected = selectedUserType == UserType.PATIENT,
                            onClick = {
                                selectedUserType = UserType.PATIENT
                                userTypeError = null
                            },
                            modifier = Modifier.weight(1f)
                        )
                        UserTypeButton(
                            text = "Doctor",
                            icon = Icons.Outlined.LocalHospital,
                            isSelected = selectedUserType == UserType.DOCTOR,
                            onClick = {
                                selectedUserType = UserType.DOCTOR
                                userTypeError = null
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // user type error message
                    if (userTypeError != null) {
                        Text(
                            text = userTypeError!!,
                            color = Color(0xFFDC2626),
                            fontSize = 12.sp,
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(top = 4.dp, start = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // First Name
                    SignUpTextField(
                        value = firstName,
                        onValueChange = {
                            firstName = it
                            if (firstNameError != null) validateFirstName()
                        },
                        placeholder = "First Name",
                        icon = Icons.Default.Person,
                        errorMessage = firstNameError,
                        keyboardType = KeyboardType.Text
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Last Name
                    SignUpTextField(
                        value = lastName,
                        onValueChange = {
                            lastName = it
                            if (lastNameError != null) validateLastName()
                        },
                        placeholder = "Last Name",
                        icon = Icons.Default.Person,
                        errorMessage = lastNameError,
                        keyboardType = KeyboardType.Text
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Email
                    SignUpTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            if (emailError != null) validateEmail()
                        },
                        placeholder = "Email Address",
                        icon = Icons.Default.Email,
                        errorMessage = emailError,
                        keyboardType = KeyboardType.Email
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password
                    SignUpTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            if (passwordError != null) validatePassword()
                            if (confirmPassword.isNotEmpty()) validateConfirmPassword()
                        },
                        placeholder = "Password",
                        icon = Icons.Default.Lock,
                        isPassword = true,
                        isVisible = isPasswordVisible,
                        onToggleVisibility = { isPasswordVisible = !isPasswordVisible },
                        errorMessage = passwordError
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Confirm Password
                    SignUpTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            if (confirmPasswordError != null) validateConfirmPassword()
                        },
                        placeholder = "Confirm Password",
                        icon = Icons.Default.Lock,
                        isPassword = true,
                        isVisible = isConfirmPasswordVisible,
                        onToggleVisibility = { isConfirmPasswordVisible = !isConfirmPasswordVisible },
                        errorMessage = confirmPasswordError
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Sign Up Button
                    Button(
                        onClick = {
                            if (validateAll()) {
                                // Handle successful sign up
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .shadow(12.dp, RoundedCornerShape(16.dp), spotColor = KlinikGlassColors.Cyan),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(KlinikGlassColors.Cyan, KlinikGlassColors.Blue)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Create Account",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // footer section
            Spacer(modifier = Modifier.height(18.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(modifier = Modifier.width(60.dp), color = Color.LightGray.copy(alpha = 0.5f))
                Text(
                    text = "Or sign up with",
                    fontSize = 12.sp,
                    color = KlinikGlassColors.TextGray,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                HorizontalDivider(modifier = Modifier.width(60.dp), color = Color.LightGray.copy(alpha = 0.5f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                SocialIconGlass(iconRes = R.drawable.ic_facebook)
                SocialIconGlass(iconRes = R.drawable.ic_google)
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Already have account
            Row(modifier = Modifier.padding(bottom = 20.dp)) {
                Text(
                    "Already have an account? ",
                    color = KlinikGlassColors.TextGray,
                    fontSize = 14.sp
                )
                Text(
                    "Sign In",
                    color = KlinikGlassColors.Blue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onNavigateToSignIn() }
                )
            }
        }
    }
}

// Components

@Composable
fun SignUpTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    isPassword: Boolean = false,
    isVisible: Boolean = false,
    onToggleVisibility: () -> Unit = {},
    errorMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    placeholder,
                    color = KlinikGlassColors.TextGray.copy(alpha = 0.6f)
                )
            },
            leadingIcon = {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = when {
                        errorMessage != null -> Color(0xFFDC2626)
                        value.isNotEmpty() -> KlinikGlassColors.Blue
                        else -> KlinikGlassColors.TextGray
                    }
                )
            },
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = onToggleVisibility) {
                        Icon(
                            if (isVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (isVisible) "Hide password" else "Show password",
                            tint = KlinikGlassColors.TextGray
                        )
                    }
                }
            } else null,
            visualTransformation = if (isPassword && !isVisible) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else keyboardType),
            singleLine = true,
            isError = errorMessage != null,
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = KlinikGlassColors.InputWhite,
                unfocusedContainerColor = KlinikGlassColors.InputWhite,
                disabledContainerColor = KlinikGlassColors.InputWhite,
                errorContainerColor = KlinikGlassColors.InputWhite,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                cursorColor = KlinikGlassColors.Blue
            ),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = if (errorMessage != null) Color(0xFFDC2626) else Color.Black.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(16.dp)
                )
        )

        // Error message
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color(0xFFDC2626),
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp, start = 16.dp)
            )
        }
    }
}

@Composable
fun UserTypeButton(
    text: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) KlinikGlassColors.Blue else Color.White,
        animationSpec = tween(durationMillis = 200),
        label = "backgroundColor"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else KlinikGlassColors.TextDark,
        animationSpec = tween(durationMillis = 200),
        label = "contentColor"
    )

    Button(
        onClick = onClick,
        modifier = modifier
            .height(56.dp)
            .then(
                if (isSelected) {
                    Modifier.shadow(8.dp, RoundedCornerShape(16.dp), spotColor = KlinikGlassColors.Cyan)
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (isSelected) {
                        Brush.horizontalGradient(
                            colors = listOf(KlinikGlassColors.Cyan, KlinikGlassColors.Blue)
                        )
                    } else {
                        Brush.horizontalGradient(
                            colors = listOf(Color.White, Color.White)
                        )
                    }
                )
                .border(
                    width = 1.dp,
                    color = if (isSelected) Color.Transparent else KlinikGlassColors.TextGray.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = text,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColor
                )
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 1000)
@Composable
fun PreviewKlinikSignUp() {
    MaterialTheme {
        KlinikSignUpScreen()
    }
}
