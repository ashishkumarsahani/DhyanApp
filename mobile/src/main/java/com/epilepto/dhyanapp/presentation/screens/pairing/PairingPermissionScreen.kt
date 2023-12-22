package com.epilepto.dhyanapp.presentation.screens.pairing

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForwardIos
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.epilepto.dhyanapp.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PairingPermissionScreen(
    permissionState: MultiplePermissionsState,
    onGetPermissionClicked:()->Unit
) {

    Image(
        painter = painterResource(id = R.drawable.mindfullness_ui_bg),
        contentDescription = "background",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.namaste_vector),
            contentDescription = "namaste",
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.Center)
                .scale(1.4f)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.padding(24.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.calm_emoji),
                    contentDescription = "calm",
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = "Dhyaan",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.LightGray,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "Mindfulness\nand Stress Free",
                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }
        FloatingActionButton(
            shape = CircleShape,
            onClick = onGetPermissionClicked,
            containerColor = Color(0xff393939),
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowForwardIos,
                contentDescription = "next"
            )
        }
    }
}

