package com.epilepto.dhyanapp.presentation.presentation.screens.home.meditationScreen


import android.media.MediaPlayer
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.epilepto.dhyanapp.presentation.domain.models.sensor.SensorData

@Composable
fun TestStopwatchScreen(
    mediaPlayer: MediaPlayer,
    duration: Int,
    session: String,
    onSuccess: (SensorData) -> Unit
) {

    var timeRemaining by remember { mutableIntStateOf(duration) }
    // Display the recorded heart rates


// Launch the countdown timer
    DisposableEffect(Unit) {
        val countdown = object : CountDownTimer(timeRemaining.toLong(), 100) {
            override fun onTick(millisUntilFinished: Long) {
                timeRemaining = millisUntilFinished.toInt()
                Log.d("", "onTick: $timeRemaining")
            }

            override fun onFinish() {
//                onSuccess(sensorData)
            }
        }
        countdown.start()
        mediaPlayer.start()
        // Cancel the timer when the composable is disposed
        onDispose {
            countdown.cancel()
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }



}








