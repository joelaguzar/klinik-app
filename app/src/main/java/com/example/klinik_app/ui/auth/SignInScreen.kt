package com.example.klinik_app

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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.klinik_app.data.FirebaseData
import com.example.klinik_app.data.UserType
import kotlinx.coroutines.launch

///TODO: FIREBASE AUTHENTICATION - SIGN IN
/// 1. Create AuthViewModel to handle authentication state
/// 2. Replace MockData.authenticate() with Firebase Auth:
///    - auth.signInWithEmailAndPassword(email, password).await()
/// 3. Handle authentication states (loading, success, error)
/// 4. Implement "Forgot Password" with auth.sendPasswordResetEmail(email)
/// 5. Implement social login:
///    - Google Sign-In: Firebase.auth.signInWithCredential(GoogleAuthProvider.getCredential(idToken, null))
///    - Facebook Login: Firebase.auth.signInWithCredential(FacebookAuthProvider.getCredential(token))
/// 6. Use ViewModel state for error handling and loading indicators

object KlinikGlassColors {
    val Cyan = Color(0xFF06b6d4)
    val Blue = Color(0xFF2563EB)
    val Purple = Color(0xFF7c3aed)
    val TextDark = Color(0xFF0f172a)
    val TextGray = Color(0xFF64748B)

    val GlassWhite = Color.White.copy(alpha = 0.75f)
    val InputWhite = Color.White.copy(alpha = 0.95f)
    val TitleGradient = Brush.linearGradient(
        colors = listOf(Cyan, Blue, Purple)
    )
}

@Composable
fun KlinikSignInScreen(
    onNavigateToSignUp: () -> Unit = {},
    onSignInSuccess: (String) -> Unit = {}
) {
    val scrollState = rememberScrollState()
    var selectedRole by remember { mutableStateOf("patient") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // to handle async code
    val coroutineScope = rememberCoroutineScope()

    // for error handling messages
    val snackbarHostState = remember { SnackbarHostState() }

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

            Spacer(modifier = Modifier.height(80.dp))

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

            GlassCard {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Welcome Back",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = KlinikGlassColors.TextDark
                    )
                    Text(
                        text = "Please enter your details.",
                        fontSize = 14.sp,
                        color = KlinikGlassColors.TextGray,
                        modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
                    )

                    RoleSelector(
                        selectedRole = selectedRole,
                        onRoleSelected = { selectedRole = it }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    GlassTextField(
                        value = email,
                        onValueChange = { 
                            email = it
                            errorMessage = null
                        },
                        placeholder = "Email Address",
                        icon = Icons.Default.Email
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    GlassTextField(
                        value = password,
                        onValueChange = { 
                            password = it
                            errorMessage = null
                        },
                        placeholder = "Password",
                        icon = Icons.Default.Lock,
                        isPassword = true,
                        isVisible = isVisible,
                        onToggleVisibility = { isVisible = !isVisible }
                    )

                    if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            fontSize = 12.sp,
                            color = Color.Red,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = "Forgot Password?",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = KlinikGlassColors.Blue,
                            modifier = Modifier.clickable { 
                                ///TODO: Implement forgot password with Firebase
                                /// if (email.isNotBlank()) {
                                ///     viewModel.sendPasswordResetEmail(email) { success ->
                                ///         if (success) showMessage("Password reset email sent")
                                ///         else showError("Failed to send reset email")
                                ///     }
                                /// } else {
                                ///     showError("Please enter your email first")
                                /// }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val authResult = FirebaseData.authenticate(email, password, selectedRole)
                                if (authResult != null) {
                                    FirebaseData.setCurrentUser(authResult.userId, authResult.userType)
                                    val role = if (authResult.userType == UserType.DOCTOR) "doctor" else "patient"
                                    onSignInSuccess(role)
                                } else {
                                    snackbarHostState.showSnackbar("Login failed. Check details.")
                                }
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
                                text = "Sign In",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Demo: joel.aguzar@sample.com / JoelLazernieA1",
                        fontSize = 10.sp,
                        color = KlinikGlassColors.TextGray,
                        modifier = Modifier.clickable {
                            email = "joel.aguzar@sample.com"
                            password = "JoelLazernieA1"
                            selectedRole = "patient"
                        }
                    )
                    Text(
                        text = "Doctor: doktor.erix@klinik.com / ErixCrisostomoB2",
                        fontSize = 10.sp,
                        color = KlinikGlassColors.TextGray,
                        modifier = Modifier.clickable {
                            email = "doktor.erix@klinik.com "
                            password = "ErixCrisostomoB2"
                            selectedRole = "doctor"
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(modifier = Modifier.width(60.dp), color = Color.LightGray.copy(alpha = 0.5f))
                Text(
                    text = "Or continue with",
                    fontSize = 12.sp,
                    color = KlinikGlassColors.TextGray,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                HorizontalDivider(modifier = Modifier.width(60.dp), color = Color.LightGray.copy(alpha = 0.5f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                ///TODO: Implement Facebook Sign-In
                /// 1. Add Facebook SDK dependency
                /// 2. Configure Facebook app in Facebook Developer Console
                /// 3. Use LoginManager to get access token
                /// 4. Exchange for Firebase credential: FacebookAuthProvider.getCredential(token)
                SocialIconGlass(iconRes = R.drawable.ic_facebook)
                ///TODO: Implement Google Sign-In
                /// 1. Add Google Sign-In dependency: implementation("com.google.android.gms:play-services-auth:20.7.0")
                /// 2. Configure OAuth client in Google Cloud Console
                /// 3. Use GoogleSignIn to get ID token
                /// 4. Exchange for Firebase credential: GoogleAuthProvider.getCredential(idToken, null)
                SocialIconGlass(iconRes = R.drawable.ic_google)

            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(modifier = Modifier.padding(bottom = 20.dp)) {
                Text("Don't have an account? ", color = KlinikGlassColors.TextGray, fontSize = 14.sp)
                Text(
                    "Sign Up",
                    color = KlinikGlassColors.Blue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onNavigateToSignUp() }
                )
            }

            Spacer(modifier = Modifier.height(25.dp))
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun BackgroundBlobs() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(KlinikGlassColors.Cyan.copy(alpha = 0.4f), Color.Transparent),
                center = Offset(0f, 0f), radius = 900f
            ),
            center = Offset(0f, 0f), radius = 900f
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(KlinikGlassColors.Blue.copy(alpha = 0.3f), Color.Transparent),
                center = Offset(size.width, size.height * 0.4f), radius = 700f
            ),
            center = Offset(size.width, size.height * 0.4f), radius = 700f
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(KlinikGlassColors.Purple.copy(alpha = 0.2f), Color.Transparent),
                center = Offset(0f, size.height), radius = 800f
            ),
            center = Offset(0f, size.height), radius = 800f
        )
    }
}

@Composable
fun GlassCard(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(0.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(KlinikGlassColors.GlassWhite)
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White.copy(alpha = 0.8f), Color.White.copy(alpha = 0.1f))
                ),
                shape = RoundedCornerShape(32.dp)
            )
    ) {
        content()
    }
}

@Composable
fun GlassTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    isPassword: Boolean = false,
    isVisible: Boolean = false,
    onToggleVisibility: () -> Unit = {}
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = KlinikGlassColors.TextGray.copy(alpha = 0.6f)) },
        leadingIcon = {
            Icon(
                icon,
                contentDescription = null,
                tint = if (value.isNotEmpty()) KlinikGlassColors.Blue else KlinikGlassColors.TextGray
            )
        },
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = onToggleVisibility) {
                    Icon(
                        if (isVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null,
                        tint = KlinikGlassColors.TextGray
                    )
                }
            }
        } else null,
        visualTransformation = if (isPassword && !isVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Email),
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = KlinikGlassColors.InputWhite,
            unfocusedContainerColor = KlinikGlassColors.InputWhite,
            disabledContainerColor = KlinikGlassColors.InputWhite,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = KlinikGlassColors.Blue
        ),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Black.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
    )
}

@Composable
fun SocialIconGlass(iconRes: Int) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .border(1.dp, Color.White, CircleShape)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.7f))
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = "Social Login",
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
fun RoleSelector(
    selectedRole: String,
    onRoleSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF1F5F9))
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        RoleOption(
            text = "Patient",
            isSelected = selectedRole == "patient",
            onClick = { onRoleSelected("patient") },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        RoleOption(
            text = "Doctor",
            isSelected = selectedRole == "doctor",
            onClick = { onRoleSelected("doctor") },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun RoleOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (isSelected) {
                    Brush.horizontalGradient(
                        colors = listOf(KlinikGlassColors.Cyan, KlinikGlassColors.Blue)
                    )
                } else {
                    Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, Color.Transparent)
                    )
                }
            )
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isSelected) Color.White else KlinikGlassColors.TextGray
        )
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
fun PreviewKlinikFinal() {
    MaterialTheme {
        KlinikSignInScreen()
    }
}