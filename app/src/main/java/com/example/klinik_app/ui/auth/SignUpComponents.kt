package com.example.klinik_app.ui.auth

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.klinik_app.KlinikGlassColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val ErrorColor = Color(0xFFDC2626)
private val SuccessColor = Color(0xFF16A34A)
private val WarningColor = Color(0xFFF59E0B)

@Composable
fun StepIndicator(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Step $currentStep of $totalSteps",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = KlinikGlassColors.TextGray,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (step in 1..totalSteps) {
                val isActive = step <= currentStep
                val isCompleted = step < currentStep
                val backgroundColor by animateColorAsState(
                    targetValue = if (isActive) KlinikGlassColors.Blue else Color.LightGray.copy(alpha = 0.5f),
                    animationSpec = tween(durationMillis = 300),
                    label = "stepColor"
                )

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(backgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Completed",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text(
                            text = step.toString(),
                            color = if (isActive) Color.White else KlinikGlassColors.TextGray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (step < totalSteps) {
                    val lineProgress by animateFloatAsState(
                        targetValue = if (currentStep > step) 1f else 0f,
                        animationSpec = tween(durationMillis = 400),
                        label = "lineProgress"
                    )
                    
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color.LightGray.copy(alpha = 0.3f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(lineProgress)
                                .height(4.dp)
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(KlinikGlassColors.Cyan, KlinikGlassColors.Blue)
                                    )
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
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
                text = text,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

enum class PasswordStrength(val label: String, val color: Color, val progress: Float) {
    WEAK("Weak", Color(0xFFDC2626), 0.25f),
    FAIR("Fair", Color(0xFFF59E0B), 0.5f),
    GOOD("Good", Color(0xFF3B82F6), 0.75f),
    STRONG("Strong", Color(0xFF16A34A), 1f)
}

fun calculatePasswordStrength(password: String): PasswordStrength {
    if (password.isEmpty()) return PasswordStrength.WEAK
    
    var score = 0
    if (password.length >= 8) score++
    if (password.any { it.isUpperCase() }) score++
    if (password.any { it.isLowerCase() }) score++
    if (password.any { it.isDigit() }) score++
    if (password.any { !it.isLetterOrDigit() }) score++
    if (password.length >= 12) score++
    
    return when {
        score <= 2 -> PasswordStrength.WEAK
        score <= 3 -> PasswordStrength.FAIR
        score <= 4 -> PasswordStrength.GOOD
        else -> PasswordStrength.STRONG
    }
}

@Composable
fun PasswordStrengthIndicator(
    password: String,
    modifier: Modifier = Modifier
) {
    if (password.isEmpty()) return
    
    val strength = calculatePasswordStrength(password)
    val animatedProgress by animateFloatAsState(
        targetValue = strength.progress,
        animationSpec = tween(durationMillis = 300),
        label = "strengthProgress"
    )
    val animatedColor by animateColorAsState(
        targetValue = strength.color,
        animationSpec = tween(durationMillis = 300),
        label = "strengthColor"
    )
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .animateContentSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Password strength",
                fontSize = 12.sp,
                color = KlinikGlassColors.TextGray
            )
            Text(
                text = strength.label,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = animatedColor
            )
        }
        
        Spacer(modifier = Modifier.height(6.dp))
        
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = animatedColor,
            trackColor = Color.LightGray.copy(alpha = 0.3f),
            strokeCap = StrokeCap.Round
        )
    }
}

@Composable
fun PasswordRequirementsHint(
    password: String,
    modifier: Modifier = Modifier
) {
    val requirements = listOf(
        "At least 8 characters" to (password.length >= 8),
        "One uppercase letter" to password.any { it.isUpperCase() },
        "One lowercase letter" to password.any { it.isLowerCase() },
        "One number" to password.any { it.isDigit() }
    )
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 4.dp)
            .animateContentSize()
    ) {
        requirements.forEach { (text, isMet) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 2.dp)
            ) {
                val iconColor by animateColorAsState(
                    targetValue = if (isMet) SuccessColor else KlinikGlassColors.TextGray.copy(alpha = 0.5f),
                    animationSpec = tween(200),
                    label = "reqIconColor"
                )
                Icon(
                    imageVector = if (isMet) Icons.Default.Check else Icons.Default.Info,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = text,
                    fontSize = 11.sp,
                    color = if (isMet) KlinikGlassColors.TextDark else KlinikGlassColors.TextGray.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun TermsAndConditionsCheckbox(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onCheckedChange(!isChecked) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = KlinikGlassColors.Blue,
                    uncheckedColor = if (errorMessage != null) ErrorColor else KlinikGlassColors.TextGray,
                    checkmarkColor = Color.White
                )
            )
            
            Text(
                text = buildAnnotatedString {
                    append("I agree to the ")
                    withStyle(SpanStyle(color = KlinikGlassColors.Blue, fontWeight = FontWeight.SemiBold)) {
                        append("Terms of Service")
                    }
                    append(" and ")
                    withStyle(SpanStyle(color = KlinikGlassColors.Blue, fontWeight = FontWeight.SemiBold)) {
                        append("Privacy Policy")
                    }
                },
                fontSize = 13.sp,
                color = KlinikGlassColors.TextGray,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
        
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = ErrorColor,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 48.dp, top = 2.dp)
            )
        }
    }
}

@Composable
fun WelcomePersonalization(
    firstName: String,
    modifier: Modifier = Modifier
) {
    if (firstName.isNotBlank()) {
        Text(
            text = buildAnnotatedString {
                append("Welcome, ")
                withStyle(SpanStyle(color = KlinikGlassColors.Blue, fontWeight = FontWeight.Bold)) {
                    append(firstName.trim().replaceFirstChar { it.uppercase() })
                }
                append("! 👋")
            },
            fontSize = 14.sp,
            color = KlinikGlassColors.TextGray,
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .animateContentSize()
        )
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

@Composable
fun SexButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else KlinikGlassColors.TextDark,
        animationSpec = tween(durationMillis = 200),
        label = "contentColor"
    )

    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(12.dp),
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
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = contentColor
            )
        }
    }
}

@Composable
fun SignUpTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    isVisible: Boolean = false,
    onToggleVisibility: () -> Unit = {},
    errorMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = modifier.fillMaxWidth()) {
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
                        errorMessage != null -> ErrorColor
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
            colors = signUpTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = if (errorMessage != null) ErrorColor else Color.Black.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(16.dp)
                )
        )

        ErrorText(errorMessage)
    }
}

@Composable
fun MultiLineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    minLines: Int = 3,
    maxLines: Int = 5,
    maxLength: Int = 500
) {
    Column(modifier = modifier.fillMaxWidth()) {
        TextField(
            value = value,
            onValueChange = { if (it.length <= maxLength) onValueChange(it) },
            placeholder = {
                Text(
                    placeholder,
                    color = KlinikGlassColors.TextGray.copy(alpha = 0.6f)
                )
            },
            minLines = minLines,
            maxLines = maxLines,
            isError = errorMessage != null,
            shape = RoundedCornerShape(16.dp),
            colors = signUpTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = if (errorMessage != null) ErrorColor else Color.Black.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(16.dp)
                )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = ErrorColor,
                    fontSize = 12.sp
                )
            } else {
                Spacer(modifier = Modifier.width(1.dp))
            }
            Text(
                text = "${value.length}/$maxLength",
                color = KlinikGlassColors.TextGray,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun TagInputField(
    value: String,
    onValueChange: (String) -> Unit,
    onAddTag: () -> Unit,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(modifier = Modifier.weight(1f)) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    Text(
                        "Add tag",
                        color = KlinikGlassColors.TextGray.copy(alpha = 0.6f)
                    )
                },
                leadingIcon = {
                    Icon(
                        leadingIcon,
                        contentDescription = null,
                        tint = if (value.isNotEmpty()) KlinikGlassColors.Blue else KlinikGlassColors.TextGray
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onAddTag() }),
                shape = RoundedCornerShape(16.dp),
                colors = signUpTextFieldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.Black.copy(alpha = 0.05f),
                        shape = RoundedCornerShape(16.dp)
                    )
            )
        }

        IconButton(
            onClick = onAddTag,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(KlinikGlassColors.Cyan, KlinikGlassColors.Blue)
                    )
                )
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add tag",
                tint = Color.White
            )
        }
    }
}

@Composable
fun DatePickerField(
    label: String,
    selectedDate: Long?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(KlinikGlassColors.InputWhite)
                .border(
                    width = 1.dp,
                    color = if (errorMessage != null) ErrorColor else Color.Black.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable { onClick() }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = when {
                        errorMessage != null -> ErrorColor
                        selectedDate != null -> KlinikGlassColors.Blue
                        else -> KlinikGlassColors.TextGray
                    }
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = selectedDate?.let { formatDate(it) } ?: label,
                    color = if (selectedDate != null) KlinikGlassColors.TextDark 
                           else KlinikGlassColors.TextGray.copy(alpha = 0.6f),
                    fontSize = 16.sp
                )
            }
        }

        ErrorText(errorMessage)
    }
}

@Composable
fun DropdownField(
    label: String,
    selectedValue: String,
    options: List<String>,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    errorMessage: String? = null
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(KlinikGlassColors.InputWhite)
                    .border(
                        width = 1.dp,
                        color = if (errorMessage != null) ErrorColor else Color.Black.copy(alpha = 0.05f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { expanded = true }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            icon,
                            contentDescription = null,
                            tint = when {
                                errorMessage != null -> ErrorColor
                                selectedValue.isNotEmpty() -> KlinikGlassColors.Blue
                                else -> KlinikGlassColors.TextGray
                            }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = selectedValue.ifEmpty { label },
                            color = if (selectedValue.isNotEmpty()) KlinikGlassColors.TextDark 
                                   else KlinikGlassColors.TextGray.copy(alpha = 0.6f),
                            fontSize = 16.sp
                        )
                    }
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = KlinikGlassColors.TextGray
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }

        ErrorText(errorMessage)
    }
}

@Composable
fun TagChip(
    text: String,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(KlinikGlassColors.Blue.copy(alpha = 0.1f))
            .border(
                width = 1.dp,
                color = KlinikGlassColors.Blue.copy(alpha = 0.3f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(start = 12.dp, end = 4.dp, top = 4.dp, bottom = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = text,
                color = KlinikGlassColors.Blue,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Remove tag",
                    tint = KlinikGlassColors.Blue,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun ErrorText(errorMessage: String?) {
    if (errorMessage != null) {
        Text(
            text = errorMessage,
            color = ErrorColor,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 4.dp, start = 16.dp)
        )
    }
}

@Composable
private fun signUpTextFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = KlinikGlassColors.InputWhite,
    unfocusedContainerColor = KlinikGlassColors.InputWhite,
    disabledContainerColor = KlinikGlassColors.InputWhite,
    errorContainerColor = KlinikGlassColors.InputWhite,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    errorIndicatorColor = Color.Transparent,
    cursorColor = KlinikGlassColors.Blue
)

private fun formatDate(timestamp: Long): String {
    return SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(timestamp))
}
