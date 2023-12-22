package com.epilepto.dhyanapp.presentation.presentation.screens.home

import android.os.CountDownTimer
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.epilepto.dhyanapp.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TimerAnimation(
    animationTime: Int,
    navigateToStopwatch:()->Unit,
    navigateToPranayamStopwatch:()->Unit
) {
    val haptic = LocalHapticFeedback.current
    var timeRemaining by remember { mutableIntStateOf(animationTime) }

    CountdownAnimation((timeRemaining / 1000))
    DisposableEffect(Unit) {
        val countdown = object : CountDownTimer(timeRemaining.toLong(), 100) {
            override fun onTick(millisUntilFinished: Long) {
                timeRemaining = millisUntilFinished.toInt()
            }

            override fun onFinish() {
                if(animationTime == 3000){
                    navigateToStopwatch()
                }
                else if( animationTime == 3010){
                    Log.d("", "Planning to pranayam SW")
                    navigateToPranayamStopwatch()
                }
            }
        }
        countdown.start()

        // Cancel the timer when the composable is disposed
        onDispose {
            countdown.cancel()
        }
    }

}

@Composable
fun CountdownAnimation(remainingTime: Int) {
    val incrementedRemainingTime = remainingTime + 1
    val countdown by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.countdown))


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Log.d("Coutdown_animation: Animation chal rahi hai", countdown.toString())
        LottieAnimation(
            modifier = Modifier.size(550.dp),
            composition = countdown,
            iterations = LottieConstants.IterateForever,
            speed = 1.3f
        )

    }
}
