package com.example.klinik_app.ui.auth

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.klinik_app.KlinikGlassColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val ErrorColor = Color(0xFFDC2626)

@Composable
fun StepIndicator(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (step in 1..totalSteps) {
            val isActive = step <= currentStep
            val backgroundColor by animateColorAsState(
                targetValue = if (isActive) KlinikGlassColors.Blue else Color.LightGray.copy(alpha = 0.5f),
                animationSpec = tween(durationMillis = 300),
                label = "stepColor"
            )

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = step.toString(),
                    color = if (isActive) Color.White else KlinikGlassColors.TextGray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (step < totalSteps) {
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(3.dp)
                        .background(
                            if (currentStep > step) KlinikGlassColors.Blue
                            else Color.LightGray.copy(alpha = 0.5f)
                        )
                )
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

// ==================== Text Fields ====================

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
