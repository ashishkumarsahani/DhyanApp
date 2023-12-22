package com.epilepto.dhyanapp.presentation.presentation.screens.home.pranayamScreen.light_session

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Text
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.epilepto.dhyanapp.R

@Composable
fun PranayamLightModeIESelectionScreen(
    pranayamaMode: String,
    pranayamaSession: Int,
    navigateToPranayamTimerAnimation:(IEratio:String)->Unit
) {
    val listState = rememberScalingLazyListState()
    var IEratio: String
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        autoCentering = AutoCenteringParams(itemIndex = 0),
        state = listState
    ) {
        item {
            Text(text = pranayamaMode, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        //Calm
        item {
            Chip(
                onClick = {
                    IEratio = "3:6"
                    navigateToPranayamTimerAnimation(IEratio)
                },
                colors = ChipDefaults.secondaryChipColors(),
                label = { Text("3:6 seconds", color = Color.LightGray) },
                icon = {
                    Image(
                        painter = painterResource(id = R.drawable.yoga_ratio),
                        contentDescription = "IE Ratio Icon",
                        modifier = Modifier.size(25.dp)
                    )
                })
        }
        //Calm
        item {
            Chip(
                onClick = {
                    IEratio = "4:8"
                    navigateToPranayamTimerAnimation(IEratio)
                },
                colors = ChipDefaults.secondaryChipColors(),
                label = { Text("4:8 seconds", color = Color.LightGray) },
                icon = {
                    Image(
                        painter = painterResource(id = R.drawable.yoga_ratio),
                        contentDescription = "IE Ratio Icon",
                        modifier = Modifier.size(25.dp)
                    )
                })
        }

        //Calm
        item {
            Chip(
                onClick = {
                    IEratio = "5:10"
                    navigateToPranayamTimerAnimation(IEratio)
                },
                colors = ChipDefaults.secondaryChipColors(),
                label = { Text("5:10 seconds", color = Color.LightGray) },
                icon = {
                    Image(
                        painter = painterResource(id = R.drawable.yoga_ratio),
                        contentDescription = "IE Ratio Icon",
                        modifier = Modifier.size(25.dp)
                    )
                })
        }
        Log.d("", "Session at IE - $pranayamaSession")
    }
}