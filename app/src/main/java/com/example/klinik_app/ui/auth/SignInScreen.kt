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
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    onSignInSuccess: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // bg
        BackgroundBlobs()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(80.dp))

            // header - centered logo
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

            // subtxt
            Text(
                text = "Health in your pocket",
                color = KlinikGlassColors.TextGray,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // login card
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
                        modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
                    )

                    // email and pass
                    var email by remember { mutableStateOf("") }
                    GlassTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "Email Address",
                        icon = Icons.Default.Email
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    var password by remember { mutableStateOf("") }
                    var isVisible by remember { mutableStateOf(false) }
                    GlassTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "Password",
                        icon = Icons.Default.Lock,
                        isPassword = true,
                        isVisible = isVisible,
                        onToggleVisibility = { isVisible = !isVisible }
                    )

                    // forgot pass
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
                            modifier = Modifier.clickable { }
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // login button
                    Button(
                        onClick = { onSignInSuccess() },
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
                }
            }

            // footer
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
                SocialIconGlass(iconRes = R.drawable.ic_facebook)
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
        }
    }
}

// components

@Composable
fun BackgroundBlobs() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        // Top Left Cyan
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(KlinikGlassColors.Cyan.copy(alpha = 0.4f), Color.Transparent),
                center = Offset(0f, 0f), radius = 900f
            ),
            center = Offset(0f, 0f), radius = 900f
        )
        // Center Right Blue
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(KlinikGlassColors.Blue.copy(alpha = 0.3f), Color.Transparent),
                center = Offset(size.width, size.height * 0.4f), radius = 700f
            ),
            center = Offset(size.width, size.height * 0.4f), radius = 700f
        )
        // Bottom Left Purple
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

@Preview(showBackground = true, heightDp = 900)
@Composable
fun PreviewKlinikFinal() {
    MaterialTheme {
        KlinikSignInScreen()
    }
}