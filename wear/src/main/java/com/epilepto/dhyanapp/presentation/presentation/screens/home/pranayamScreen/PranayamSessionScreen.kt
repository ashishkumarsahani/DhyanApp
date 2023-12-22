package com.epilepto.dhyanapp.presentation.presentation.screens.home.pranayamScreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.Text
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.epilepto.dhyanapp.R

@Composable
fun PranayamSessionScreen(
    navigateToPranayamTimer: (pranayamaMode: String, pranayamaSession: Int) -> Unit
) {
    val listState = rememberScalingLazyListState()
    var pranayamaSession = 0
    val easy by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.easy))
    val balanced by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.balanced))
    var pranayamaMode: PranayamSessionType

    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        autoCentering = AutoCenteringParams(itemIndex = 0),
        state = listState
    ) {
        item {
            Card(onClick = {
                pranayamaSession = pranayamaSession
                pranayamaMode = PranayamSessionType.LIGHT_MODE
                navigateToPranayamTimer(pranayamaMode.name,pranayamaSession )
            }, contentColor = Color(0XFFF2B8B5)) {
                LottieAnimation(
                    modifier = Modifier.size(30.dp),
                    composition = easy,
                    iterations = LottieConstants.IterateForever,
                    speed = 0.5f
                )
                Text(text = "Easy Mode", fontSize = 18.sp)
                Text(text = "Inhale:Exhale", color = Color.LightGray, fontSize = 12.sp)
            }

        }
        item {
            Card(
                onClick = {
                    pranayamaSession = pranayamaSession
                    pranayamaMode = PranayamSessionType.BALANCE_MODE
                    navigateToPranayamTimer(pranayamaMode.name, pranayamaSession)
                },
                contentColor = Color(0xFFF2B8B5)
            ) {
                LottieAnimation(
                    modifier = Modifier
                        .size(30.dp)
                        .fillMaxWidth()
                        .fillMaxSize(),
                    composition = balanced,
                    iterations = LottieConstants.IterateForever,
                    speed = 0.5f
                )
                Text(text = "Balanced Mode", fontSize = 18.sp)
                Text(text = "Inhale:Hold:Exhale:Hold", color = Color.LightGray, fontSize = 12.sp)
            }

        }
    }
}

enum class PranayamSessionType {
    LIGHT_MODE,
    BALANCE_MODE
}