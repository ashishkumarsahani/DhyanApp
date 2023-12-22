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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.epilepto.dhyanapp.R
import com.epilepto.dhyanapp.presentation.domain.models.sensor.SensorData
import com.epilepto.dhyanapp.presentation.navigation.Routes
import com.epilepto.dhyanapp.presentation.navigation.Routes.MeditationSessionScore.duration
import com.epilepto.dhyanapp.presentation.presentation.screens.scoreScreen.SharedScoreViewModel
import com.epilepto.dhyanapp.presentation.presentation.theme.Score_calculator
import com.epilepto.dhyanapp.presentation.presentation.theme.WearMessageSender

@Composable
fun SuccessAnimationScreen(
    sharedScoreViewModel: SharedScoreViewModel,
    onFinish: (SensorData) -> Unit,
) {
    var sensorData: SensorData? = null
    val animationTime = 2000
    var timeRemaining by remember { mutableIntStateOf(animationTime) }
    val data = WearMessageSender(LocalContext.current)
    var onlyCalledOnce by remember { mutableStateOf(true) }
    data.sendMessage("/session_stated", "Session Has ended".toByteArray())
    TickMarkAnimation()
    if(sharedScoreViewModel.sensorData.value?.pranayamSession == -1){
        if(onlyCalledOnce) {
            val (dhyan, asan, prana) = Score_calculator(
                duration = duration,
                bpm = Routes.MeditationSessionScore.arrayBpm,
                ax = Routes.MeditationSessionScore.arrayAx,
                ay = Routes.MeditationSessionScore.arrayAy,
                az = Routes.MeditationSessionScore.arrayAz,
                gx = Routes.MeditationSessionScore.arrayGx,
                gy = Routes.MeditationSessionScore.arrayGy,
                gz = Routes.MeditationSessionScore.arrayGz,
                spo2 = Routes.MeditationSessionScore.arraySPO2
            )
            sensorData = SensorData(
                duration = duration.toFloat(),
                dhyanScore = dhyan.toFloat(),
                asanaScore = asan,
                pranaScore = prana,
                arrayBpm = Routes.MeditationSessionScore.arrayBpm,
                arrayMsBpm = Routes.MeditationSessionScore.arrayMsBpm,
                arraySPO2 = Routes.MeditationSessionScore.arraySPO2,
                arrayMsSPO2 = Routes.MeditationSessionScore.arrayMsSPO2,
                arrayAx = Routes.MeditationSessionScore.arrayAx,
                arrayAy = Routes.MeditationSessionScore.arrayAy,
                arrayAz = Routes.MeditationSessionScore.arrayAz,
                arrayGx = Routes.MeditationSessionScore.arrayGx,
                arrayGy = Routes.MeditationSessionScore.arrayGy,
                arrayGz = Routes.MeditationSessionScore.arrayGz,
                IEratio = "",
                arrayInhaleExhale = Routes.MeditationSessionScore.arrayInhaleExhale,
                breaths = -1,
                pranayamSession = -1
            )
            Log.d("", "Meditation Analysis")
            Log.d("sensor data", "$sensorData")
            onlyCalledOnce = false
        }
    }
    else {
        if(onlyCalledOnce) {
            onlyCalledOnce = false
            val (dhyan, asan, prana) = Score_calculator(
                duration = Routes.PranayamSessionScore.duration,
                bpm = Routes.PranayamSessionScore.arrayBpm,
                ax = Routes.PranayamSessionScore.arrayAx,
                ay = Routes.PranayamSessionScore.arrayAy,
                az = Routes.PranayamSessionScore.arrayAz,
                gx = Routes.PranayamSessionScore.arrayGx,
                gy = Routes.PranayamSessionScore.arrayGy,
                gz = Routes.PranayamSessionScore.arrayGz,
                spo2 = Routes.PranayamSessionScore.arraySPO2
            )
            sensorData = SensorData(
                duration = Routes.PranayamSessionScore.duration,
                dhyanScore = dhyan.toFloat(),
                asanaScore = asan,
                pranaScore = prana,
                arrayBpm = Routes.PranayamSessionScore.arrayBpm,
                arrayMsBpm = Routes.PranayamSessionScore.arrayMsBpm,
                arraySPO2 = Routes.PranayamSessionScore.arraySPO2,
                arrayMsSPO2 = Routes.PranayamSessionScore.arrayMsSPO2,
                arrayAx = Routes.PranayamSessionScore.arrayAx,
                arrayAy = Routes.PranayamSessionScore.arrayAz,
                arrayAz = Routes.PranayamSessionScore.arrayAz,
                arrayGx = Routes.PranayamSessionScore.arrayGx,
                arrayGy = Routes.PranayamSessionScore.arrayGy,
                arrayGz = Routes.PranayamSessionScore.arrayGz,
                IEratio = Routes.PranayamSessionScore.IEratio,
                arrayInhaleExhale = Routes.PranayamSessionScore.arrayInhaleExhale,
                breaths = Routes.PranayamSessionScore.breaths,
                pranayamSession = Routes.PranayamSessionScore.pranayamSession
            )
            Log.d("", "Pranayam Analysis")
        }
    }
    Log.d("Flag for score", "$onlyCalledOnce")
    DisposableEffect(Unit) {
        val countdown = object : CountDownTimer(timeRemaining.toLong(), 100) {
            override fun onTick(millisUntilFinished: Long) {
                timeRemaining = millisUntilFinished.toInt()
            }

            override fun onFinish() {
                if (sensorData != null) {
                    onFinish(sensorData)
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
fun TickMarkAnimation() {
    val success by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.sucess))
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            modifier = Modifier
                .size(150.dp),
            composition = success,
            iterations = LottieConstants.IterateForever
        )
    }
}
