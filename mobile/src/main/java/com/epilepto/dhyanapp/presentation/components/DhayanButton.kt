package com.epilepto.dhyanapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.epilepto.dhyanapp.theme.onPrimary
import com.epilepto.dhyanapp.theme.primaryColor
import com.epilepto.dhyanapp.utils.SignInUtils

@Composable
fun GradientButton(
    title:String,
    onClick:()->Unit
) {
    Box(
        modifier = Modifier
            .width(100.dp)
            .height(50.dp)
            .clip(ShapeDefaults.Medium)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color(0xFFFEA53C),
                        Color(0xFFFB6A4F)
                    )
                )
            ).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}