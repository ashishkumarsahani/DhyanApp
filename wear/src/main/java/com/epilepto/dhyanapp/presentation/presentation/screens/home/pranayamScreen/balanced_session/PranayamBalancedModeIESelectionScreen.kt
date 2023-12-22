package com.epilepto.dhyanapp.presentation.presentation.screens.home.pranayamScreen.balanced_session

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Text
import com.epilepto.dhyanapp.R

@Composable
fun PranayamBalancedModeIESelectionScreen(
    pranayamaMode: String,
    navigateToStopWatch:(IERatio:String)->Unit
) {
    val listState = rememberScalingLazyListState()
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        autoCentering = AutoCenteringParams(itemIndex = 0),
        state = listState
    ) {
        item {
            Text(text = pranayamaMode, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        //3:3:6:3 seconds
        item {
            Chip(
                onClick = {
                    navigateToStopWatch("3:3:6:3")
                },
                colors = ChipDefaults.secondaryChipColors(),
                label = { Text("3:3:6:3 seconds", color = Color.LightGray) },
                icon = {
                    Image(
                        painter = painterResource(id = R.drawable.yoga_ratio),
                        contentDescription = "IE Ratio Icon",
                        modifier = Modifier.size(25.dp)
                    )
                })
        }

        //4:4:8:4 seconds
        item {
            Chip(
                onClick = {
                    navigateToStopWatch("4:4:8:4")
                },
                colors = ChipDefaults.secondaryChipColors(),
                label = { Text("4:4:8:4 seconds", color = Color.LightGray) },
                icon = {
                    Image(
                        painter = painterResource(id = R.drawable.yoga_ratio),
                        contentDescription = "IE Ratio Icon",
                        modifier = Modifier.size(25.dp)
                    )
                })
        }
    }
}