package com.epilepto.dhyanapp.presentation.presentation.screens.home.pranayamScreen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Text
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.epilepto.dhyanapp.R


@Composable
fun PranayamTimerScreen(
    pranayamaMode: String,
    navigateToMeditation:()->Unit,
    navigateToPranayamLightMode:(Int)->Unit,
    navigateToPranayamBalancedMode:(Int)->Unit
) {
    var pranayamaSession by remember { mutableIntStateOf(1) } //Updating the number of session in timer page session
    var isTimerRunning by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Stepper(
//            value = pranayamaSession,
//            onValueChange = { newValue ->
//                pranayamaSession = newValue.coerceIn(1, 30)
//                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
//                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
//
//                isTimerRunning = false
//            },
//            valueProgression = 1..30,
//            decreaseIcon = { Icon(StepperDefaults.Decrease, "Decrease") },
//            increaseIcon = { Icon(StepperDefaults.Increase, "Increase") })
//        {}
//    }

    PranayamaTimerCard(
        pranayamaSession = pranayamaSession,
        pranayamaMode = pranayamaMode,
        onStartTimer = { isTimerRunning = true },
        navigateToMeditation = navigateToMeditation,
        navigateToPranayamLightMode = navigateToPranayamLightMode,
        navigateToPranayamBalancedMode = navigateToPranayamBalancedMode
    )
//    PranayamCircularLoading(value = pranayamaSession)
}

@Composable
fun PranayamaTimerCard(
    pranayamaSession: Int,
    pranayamaMode: String,
    onStartTimer: () -> Unit,
    navigateToMeditation:()->Unit,
    navigateToPranayamLightMode:(Int)->Unit,
    navigateToPranayamBalancedMode:(Int)->Unit
) {
    var setTimer by remember { mutableStateOf(false) }
    val buddha by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.pranayama_timer))
    val haptic = LocalHapticFeedback.current
    val itemList = List(100) { index -> "${(index+pranayamaSession) + 1}" }
//    Log.d(pranayamaSession.toString(), "Session in timer")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(modifier = Modifier
            .size(70.dp), composition = buddha, iterations = LottieConstants.IterateForever)
        Text(text = "Choose the session duration", fontSize = 15.sp, color = Color.DarkGray)
//        Spacer(modifier = Modifier.size(5.dp))
        //This handles everything and navigates to the stopwatch page
        LazyRow {
            items(itemList) { item ->
                Text(
                    text = item,
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            when (pranayamaMode) {
                                PranayamSessionType.LIGHT_MODE.name -> {
                                    Log.d("", "Session at TIMER - $item")
                                    navigateToPranayamLightMode(item.toInt())
                                }

                                PranayamSessionType.BALANCE_MODE.name -> {
                                    navigateToPranayamBalancedMode(item.toInt())
                                }

                                else -> {
                                    navigateToMeditation()
                                }
                            }
                        },
                    fontSize = 30.sp,
                    color = Color(0xFFFFBD48)
                )
            }
        }
    }
}

@Composable
fun PranayamCircularLoading(value: Int) {
    val normalizedValue = value / 30.0f

    CircularProgressIndicator(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 1.dp),
        startAngle = 295.5f,
        endAngle = 245.5f,
        progress = normalizedValue.coerceIn(0f, 1f), // Ensure progress is within [0, 1]
        strokeWidth = 10.dp,
        indicatorColor = Color(0xFFFFBD48),
        trackColor = Color.Black
    )
}

