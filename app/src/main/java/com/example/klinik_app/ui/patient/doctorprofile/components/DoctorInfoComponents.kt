package com.example.klinik_app.ui.patient.doctorprofile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.klinik_app.ui.patient.Doctor
import com.example.klinik_app.ui.patient.PatientHomeColors

/**
 * Top app bar for the doctor profile screen.
 */
@Composable
fun DoctorProfileTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = PatientHomeColors.TextDark
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Text(
            text = "Doctor Profile",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = PatientHomeColors.TextDark
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Placeholder for symmetry
        Spacer(modifier = Modifier.width(48.dp))
    }
}

/**
 * Section displaying doctor's main information including photo, name, and rating.
 */
@Composable
fun DoctorInfoSection(doctor: Doctor) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Doctor Image
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color(0xFFF3F4F6))
        ) {
            Image(
                painter = painterResource(id = doctor.imageRes),
                contentDescription = doctor.fullName,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column {
            Text(
                text = "Dr. ${doctor.fullName}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PatientHomeColors.TextDark
            )
            
            Text(
                text = "MBBS, MD (${doctor.field})",
                fontSize = 14.sp,
                color = PatientHomeColors.TextGray,
                modifier = Modifier.padding(top = 2.dp)
            )
            
            // Sex and Age Row
            Text(
                text = "${doctor.sex}, ${doctor.age} years old",
                fontSize = 13.sp,
                color = PatientHomeColors.TextLightGray,
                modifier = Modifier.padding(top = 2.dp)
            )
            
            // Rating Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = PatientHomeColors.StarYellow,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${doctor.rating} (${doctor.appointments})",
                    fontSize = 14.sp,
                    color = PatientHomeColors.TextGray
                )
            }
        }
    }
}

/**
 * Row of doctor specialty tags.
 */
@Composable
fun DoctorTagsRow(tags: List<String>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tags.forEach { tag ->
            DoctorTagChip(text = tag)
        }
    }
}

/**
 * Individual tag chip for doctor specialties.
 */
@Composable
fun DoctorTagChip(text: String) {
    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Color(0xFFE5E7EB),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = PatientHomeColors.TextGray
        )
    }
}

/**
 * Section displaying doctor's biography.
 */
@Composable
fun DoctorBiographySection(biography: String = DEFAULT_BIOGRAPHY) {
    Column {
        Text(
            text = "Doctor Biography",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = PatientHomeColors.TextDark
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = biography,
            fontSize = 14.sp,
            color = PatientHomeColors.TextGray,
            lineHeight = 22.sp
        )
    }
}

private const val DEFAULT_BIOGRAPHY = "Eion Morgan is a dedicated pediatrician with over 15 years of experience in caring for children's health. She is passionate about ensuring the well-being of your little ones and believes in a holistic approach."
