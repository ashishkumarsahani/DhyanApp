package com.epilepto.dhyanapp.presentation.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.epilepto.dhyanapp.R
import com.epilepto.dhyanapp.theme.NunitoSans
import com.epilepto.dhyanapp.theme.onPrimary
import com.epilepto.dhyanapp.theme.primaryColor


@Composable
fun OnBoardingScreen(
    onClick:()->Unit
) {
    Image(
            painter = painterResource(id = R.drawable.onboarding_full_bg),
            contentDescription = "background",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Text(
            text = "Dhayan App",
            fontFamily = NunitoSans,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(12.dp),
            color = onPrimary
        )

        Image(
            painter = painterResource(id = R.drawable.ic_onboarding),
            contentDescription = "person",
            alignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth(0.9f),
            contentScale = ContentScale.Crop
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = "We are what we do",
                style = TextStyle(
                    fontFamily = NunitoSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Biofeedback based\nMeditation and Pranayama",
                style = TextStyle(
                    fontFamily = NunitoSans,
                    fontWeight = FontWeight.Light,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    color = Color.Gray
                ),
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(12.dp))


        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
        ) {
            Text(
                text = "Get Started",
                modifier = Modifier.padding(12.dp),
                color = onPrimary,
            )
        }
    }
}