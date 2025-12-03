package com.example.klinik_app.ui.patient.doctorprofile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.klinik_app.ui.patient.PatientHomeColors

@Composable
fun RequirementsSection(
    symptoms: String,
    onSymptomsChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit
) {
    Column {
        Text(
            text = "Requirements",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = PatientHomeColors.TextDark
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        SymptomsInput(
            symptoms = symptoms,
            onSymptomsChange = onSymptomsChange
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        DescriptionInput(
            description = description,
            onDescriptionChange = onDescriptionChange
        )
    }
}

@Composable
fun SymptomsInput(
    symptoms: String,
    onSymptomsChange: (String) -> Unit
) {
    Column {
        Text(
            text = "Symptoms",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = PatientHomeColors.TextDark
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = symptoms,
            onValueChange = onSymptomsChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Enter your symptoms (e.g., headache, fever, dizziness)",
                    fontSize = 14.sp,
                    color = PatientHomeColors.TextLightGray
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PatientHomeColors.Primary,
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            singleLine = true
        )
    }
}

@Composable
fun DescriptionInput(
    description: String,
    onDescriptionChange: (String) -> Unit
) {
    Column {
        Text(
            text = "Description",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = PatientHomeColors.TextDark
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            placeholder = {
                Text(
                    text = "Describe your condition in detail...",
                    fontSize = 14.sp,
                    color = PatientHomeColors.TextLightGray
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PatientHomeColors.Primary,
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            maxLines = 5
        )
    }
}

@Composable
fun BookAppointmentButton(
    price: String,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PatientHomeColors.Primary
        ),
        contentPadding = PaddingValues(horizontal = 24.dp)
    ) {
        Text(
            text = "Book Appointment ($price)",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = PatientHomeColors.White
        )
    }
}
