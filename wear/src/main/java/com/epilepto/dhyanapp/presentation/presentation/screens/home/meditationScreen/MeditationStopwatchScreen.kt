@file:JvmName("StopWatchKt")

package com.epilepto.dhyanapp.presentation.presentation.screens.home.meditationScreen


import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.AnchorType
import androidx.wear.compose.foundation.CurvedAlignment
import androidx.wear.compose.foundation.CurvedLayout
import androidx.wear.compose.foundation.curvedColumn
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.curvedText
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.epilepto.dhyanapp.presentation.domain.models.sensor.SensorData
import com.epilepto.dhyanapp.presentation.presentation.screens.scoreScreen.SharedScoreViewModel
import com.epilepto.dhyanapp.presentation.navigation.Routes
import com.epilepto.dhyanapp.presentation.presentation.screens.scoreScreen.formatTime
import com.epilepto.dhyanapp.presentation.presentation.screens.utils.setupSensor
import com.epilepto.dhyanapp.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.horologist.audio.SystemAudioRepository

import java.util.Calendar

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MeditationStopwatchScreen(
    mediaPlayer: MediaPlayer?,
    duration: Int,
    session: String,
    onSucess: (SensorData) -> Unit,
    sharedScoreViewModel: SharedScoreViewModel,
) {
    val SPO2_SENSOR_TYPE = "spo2"
    val PPG_SENSOR_TYPE = "ppg"

    var timeRemaining by remember { mutableIntStateOf(duration) }
    var vib_counter_med by remember { mutableIntStateOf(1) } //Counter to vibrate the watch at 4 intervals
    val spo2: Sensor?
    val ppg: Sensor?

    val ctx = LocalContext.current
    val sensorManager: SensorManager = ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    val haptic = LocalHapticFeedback.current
    val vibrator = LocalContext.current.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    val deviceSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)
    val heartrateData = remember { mutableStateOf("Heartrate Data:\n") }
    var x: Int;
    var ax: Float;
    var ay: Float;
    var az: Float;
    var gx: Float;
    var gy: Float;
    var gz: Float;
    var newData: Int
    val bpmArray = ArrayList<Float>()
    val ppgArray = ArrayList<Float>()
    val spo2Array = ArrayList<Float>()
    val axArray = ArrayList<Float>()
    val ayArray = ArrayList<Float>()
    val azArray = ArrayList<Float>()
    val gxArray = ArrayList<Float>()
    val gyArray = ArrayList<Float>()
    val gzArray = ArrayList<Float>()
    val asanaScore = 0f;
    val ms_bpm = ArrayList<Int>()
    val ms_ppg = ArrayList<Int>()
    val ms_spo2 = ArrayList<Int>()
    val ms_imu = ArrayList<Int>()
    val array_inhale_exhale = ArrayList<String>()   //Keeping it empty for mediation

    val pranaScore = 0f
    val dhyanScore = 0.0

    fun initSensor(sensorType: String): Sensor? {
        return deviceSensors.find { it.stringType.contains(sensorType, ignoreCase = true) }
            ?.let { sensorManager.getDefaultSensor(it.type) }
    }

    spo2 = initSensor(SPO2_SENSOR_TYPE)
    ppg = initSensor(PPG_SENSOR_TYPE)

    LaunchedEffect(key1 = mediaPlayer?.isPlaying){
        mediaPlayer?.start()
    }

//Heart Rate
    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val calendar = Calendar.getInstance()
                x = event.values[0].toInt()
                newData = x
                val new = event.values[0]
                if (new.toInt() != 0) {
                    bpmArray.add(new)
                    print(bpmArray.toString())
                    val time =
                        calendar.get(Calendar.MILLISECOND) + calendar.get(Calendar.SECOND) * 1000 + calendar.get(
                            Calendar.MINUTE
                        ) * 1000 * 60
                    ms_bpm.add(time)
                }
                heartrateData.value += newData
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Handle accuracy changes if needed
            }
        }
        val heartRateSensor = setupSensor(
            sensorManager,
            Sensor.TYPE_HEART_RATE,
            SensorManager.SENSOR_DELAY_FASTEST,
            listener
        )

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }
//sp02
    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val calendar = Calendar.getInstance()
                val new = event.values[0]
                if (new.toInt() != 0) {
                    //To add only non zero values
                    spo2Array.add(new)
                    val time =
                        calendar.get(Calendar.MILLISECOND) + calendar.get(Calendar.SECOND) * 1000 + calendar.get(
                            Calendar.MINUTE
                        ) * 1000 * 60
                    ms_spo2.add(time)
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Handle accuracy changes if needed
            }
        }
        sensorManager.registerListener(listener, spo2, SensorManager.SENSOR_DELAY_FASTEST)
        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

//ppg
    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val calendar = Calendar.getInstance()
                val new = event.values[0]
                ppgArray.add(new)
                val time =
                    calendar.get(Calendar.MILLISECOND) + calendar.get(Calendar.SECOND) * 1000 + calendar.get(
                        Calendar.MINUTE
                    ) * 1000 * 60
                ms_ppg.add(time)
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Handle accuracy changes if needed
            }
        }
        sensorManager.registerListener(listener, ppg, SensorManager.SENSOR_DELAY_FASTEST)
        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

//Accelerometer
    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val calendar = Calendar.getInstance()
                ax = event.values[0]
                ay = event.values[1]
                az = event.values[2]
                axArray.add(ax)
                ayArray.add(ay)
                azArray.add(az)
                val time =
                    calendar.get(Calendar.MILLISECOND) + calendar.get(Calendar.SECOND) * 1000 + calendar.get(
                        Calendar.MINUTE
                    ) * 1000 * 60
                ms_imu.add(time)
            }


            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Handle accuracy changes if needed
            }
        }
        val accelerometerSensor = setupSensor(
            sensorManager,
            Sensor.TYPE_ACCELEROMETER,
            SensorManager.SENSOR_DELAY_NORMAL,
            listener
        )

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

//Gyroscope
    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                gx = event.values[0]
                gy = event.values[1]
                gz = event.values[2]
                gxArray.add(gx)
                gyArray.add(gy)
                gzArray.add(gz)
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Handle accuracy changes if needed
            }
        }
        val gyroscopeSensor = setupSensor(
            sensorManager,
            Sensor.TYPE_GYROSCOPE,
            SensorManager.SENSOR_DELAY_NORMAL,
            listener
        )

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

// Launch the countdown timer
    DisposableEffect(Unit) {
        val countdown = object : CountDownTimer(timeRemaining.toLong(), 100) {
            override fun onTick(millisUntilFinished: Long) {
                timeRemaining = millisUntilFinished.toInt()
                val vibrationTimer = vib_counter_med * duration / 4.0

                if (((duration) - (vibrationTimer)).toInt() > timeRemaining && ((duration) - (vibrationTimer)).toInt() < timeRemaining + 100) {
                    when (vib_counter_med) {
                        1 -> {
                            val timings1: LongArray = longArrayOf(400)
                            val amplitudes1: IntArray = intArrayOf(100)
                            vibrator.vibrate(VibrationEffect.createWaveform(timings1, amplitudes1, -1))
                        }
                        2 -> {
                            val timings2: LongArray = longArrayOf(400, 400, 400)
                            val amplitudes2: IntArray = intArrayOf(100, 0, 100)
                            vibrator.vibrate(VibrationEffect.createWaveform(timings2, amplitudes2, -1))
                        }
                        3 -> {
                            val timings3: LongArray = longArrayOf(400, 400, 400, 400, 400)
                            val amplitudes3: IntArray = intArrayOf(100, 0, 100, 0, 100)
                            vibrator.vibrate(VibrationEffect.createWaveform(timings3, amplitudes3, -1))
                        }
                    }

//                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
//                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
//                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    vib_counter_med += 1
                }
            }

            override fun onFinish() {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                val sensorData = SensorData(
                    duration = duration.toFloat(),
                    dhyanScore = dhyanScore.toFloat(),
                    asanaScore = asanaScore,
                    pranaScore = pranaScore,
                    arrayBpm = bpmArray,
                    arrayMsBpm = ms_bpm,
                    arraySPO2 = spo2Array,
                    arrayMsSPO2 = ms_spo2,
                    arrayAx = axArray,
                    arrayAy = ayArray,
                    arrayAz = azArray,
                    arrayGx = gxArray,
                    arrayGy = gyArray,
                    arrayGz = gzArray,
                    IEratio = "",
                    arrayInhaleExhale = array_inhale_exhale,
                    breaths = -1,
                    pranayamSession = -1
                )
                sensorData?.let { Routes.MeditationSessionScore.passSensorData(sensorData) }
                sharedScoreViewModel.updateSensorData(sensorData)
                onSucess(sensorData)
                Log.d("Dhyan", "${sensorData.dhyanScore}")
            }
        }
        countdown.start()
//        mediaPlayer?.start()
        // Cancel the timer when the composable is disposed
        onDispose {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            countdown.cancel()

        }
    }
    val play: Boolean

    MeditationStopwatchCircularIndicator(
        timeRemaining = timeRemaining.toFloat(),
        duration = duration,
        session = session,
        mediaPlayer = mediaPlayer,
        playState = { play ->
        },
    )

}




@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MeditationStopwatchCircularIndicator(
    timeRemaining: Float,
    duration: Int,
    session: String,
    mediaPlayer: MediaPlayer?,
    playState: (Boolean) -> Unit
) {
//    var playState by remember { mutableStateOf(false) }
//    LaunchedEffect(key1 = playState){
//        if (mediaPlayer != null) {
//            if( playState){
//                mediaPlayer?.start()
//                Log.d("Playstate", "Play: ${mediaPlayer?.isPlaying}")
//            }
//            else if (!playState){
//                mediaPlayer?.pause()
//                Log.d("Playstate", "Pause: ${!mediaPlayer?.isPlaying!!}")
//            }
//        }
//    }
//    Log.d("State", "${mediaPlayer?.isPlaying}")
    val max = duration
    val min = 0
    val normalized_value = timeRemaining / max
    val meditation_cicle by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.meditation_circle))
    val music by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.music))
    val ctx = LocalContext.current
    val audioRepository = SystemAudioRepository.fromContext(ctx)
    audioRepository.volumeState
    val waves by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.guided_waves))

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier) {
            CurvedLayout(
                modifier = Modifier.fillMaxSize(),
                anchor = 270f,
                anchorType = AnchorType.Center,
                radialAlignment = CurvedAlignment.Radial.Center,
            ){
                curvedColumn(angularAlignment = CurvedAlignment.Angular.Center) {
                    curvedText(text = formatTime((timeRemaining/1000).toInt()),color = Color(0xFFF38686),fontSize = 14.sp)
                    curvedText(text = session, color = Color(0xFFF38686), fontSize = 12.sp)
                }
            }
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 1.dp),
                startAngle = 295.5f,
                endAngle = 245.5f,
                progress = normalized_value.coerceIn(0f, 1f), // Ensure progress is within [0, 1]
                strokeWidth = 15.dp,
                indicatorColor = Color(0xFFF38686),
                trackColor = Color.Black
            )
            if (session=="Guided Meditation") {
                LottieAnimation(modifier = Modifier
                    .align(Alignment.Center)
                    .size(150.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .fillMaxSize(), composition = waves, iterations = LottieConstants.IterateForever, speed = 1f)
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val audioRepository = SystemAudioRepository.fromContext(ctx)
                Row(
                    modifier=Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (session!="{Silent Meditation}") {
                        IconButton(
                            modifier = Modifier.size(50.dp),
                            onClick = {
                                audioRepository.decreaseVolume()
                                Log.d("", "TestAudioRepo:${audioRepository.available.value} ")
                        }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.volume_decrease),
                                contentDescription = "Decrease volume",
                                modifier=Modifier.size(30.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(1.dp))
                    if (session=="{Guided Meditation}") {
                        LottieAnimation(
                            modifier = Modifier
                                .size(65.dp),
//                            .padding(2.dp),
                            composition = music,
                            iterations = LottieConstants.IterateForever,
                            speed = 0.4f,
                        )
//                        Button(onClick = {
//                            playState = ! playState
//                            playState(playState)}) {
//                            if(playState == true){
//                                Text(text = "Pause")
//                            }
//                            else {
//                                Text("Play")
//                            }
//                        }
                    } else {
                        LottieAnimation(
                            modifier = Modifier
                                .size(70.dp),
//                            .padding(2.dp),
                            composition = meditation_cicle,
                            iterations = LottieConstants.IterateForever,
                            speed = 0.4f,
                        )
                    }
                    Spacer(modifier = Modifier.width(1.dp))
                    if (session!="{Silent Meditation}") {
                        IconButton(
                            modifier = Modifier.size(50.dp),
                            onClick = {
                                audioRepository.increaseVolume()
                        }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.volume_increase),
                                contentDescription = "Increase volume",
                                modifier=Modifier.size(30.dp)
                            )
                        }
                    }
                }
//                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Spacer(modifier = Modifier.height(10.dp))
                }
                Row {
                    Text(text = if (session!="{Silent Meditation}")"${audioRepository.volumeState.value.current}" else "", color = Color.White)
                }

            }
        }

    }
}









