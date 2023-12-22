package com.epilepto.dhyanapp.presentation.presentation.screens.home.pranayamScreen.balanced_session

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.CountDownTimer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import com.epilepto.dhyanapp.presentation.domain.models.sensor.SensorData
import com.epilepto.dhyanapp.presentation.navigation.Routes
import com.epilepto.dhyanapp.presentation.presentation.screens.scoreScreen.SharedScoreViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import java.util.Calendar

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PranayamaBalancedModeStopwatch(
    pranayamaMode: String,
    pranayamaSession: Int,
    IEratio: String,
    onSuccess: (SensorData) -> Unit,
    scoreViewModel: SharedScoreViewModel
) {

    var currentBreathState by remember { mutableStateOf("") }

    var breathPerMin by remember { mutableIntStateOf(0) }
    var eachBreathTime by remember { mutableIntStateOf(0) }

    //Calculating the the number of breath
    val parts = IEratio.split(":")
    val inhalation_Time = parts[0].toInt()
    val inhaleHold_Time = parts[1].toInt()
    val exhalation_Time = parts[2].toInt()
    val exhaleHold_Time = parts[3].toInt()
    eachBreathTime = (inhalation_Time + exhalation_Time + inhaleHold_Time + exhaleHold_Time)  //Time for each breah in seconds
    breathPerMin = 60/eachBreathTime         //Number of breaths in minute

    val duration = (pranayamaSession * eachBreathTime) * 1000    //Total duration of session, i.e number of session x time for each session
    var timeRemaining by remember { mutableIntStateOf(duration) }    //Variable to store time remaining, it is updated in countdown timer disposable




    val ctx = LocalContext.current
//    val mMediaPlayer = remember { MediaPlayer.create(ctx, R.raw.omchants) }   //Initializing the media player with OM Sound

    val sensorManager: SensorManager = ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager   //Creating the object sesnorManager from SensorManager

    val haptic = LocalHapticFeedback.current     //For Vibrations

    //For SensorsID and SensorManager
    val spo2: Sensor?
    val ppg: Sensor?
    val deviceSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)    //For getting the sensorID of PPG and SPO2
    val heartrate: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)   //For getting the sensorID of Heartrate
    val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)    //For getting the sensorID of Accelometer
    val gyroscope: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)   //For getting the sensorID of GyroScope
    val heartrateData = remember { mutableStateOf("Heartrate Data:\n") }     //Remembering the heartrate data which can be passed to another function

    //Variables for Accelerometer and GyroScope
    var ax: Float     // For accelerometer Ax
    var ay: Float;    // For accelerometer Ay
    var az: Float;    // For accelerometer Az
    var gx: Float;    // For accelerometer Gx
    var gy: Float;    // For accelerometer Gy
    var gz: Float;    // For accelerometer Gz


    val bpmArray = ArrayList<Float>()
    val ppgArray = ArrayList<Float>()
    val spo2Array = ArrayList<Float>()
    val axArray = ArrayList<Float>()
    val ayArray = ArrayList<Float>()
    val azArray = ArrayList<Float>()
    val gxArray = ArrayList<Float>()
    val gyArray = ArrayList<Float>()
    val gzArray = ArrayList<Float>()
    val breath_withBPM = ArrayList<String>()
    val asanaScore = 0f
    val ms_bpm = ArrayList<Int>()
    val ms_ppg = ArrayList<Int>()
    val ms_spo2 = ArrayList<Int>()
    val ms_imu = ArrayList<Int>()


    val pranaScore = 0f
    val dhyanScore = 0.0
    fun initSensor(sensorType: String): Sensor? {
        return deviceSensors.find { it.stringType.contains(sensorType, ignoreCase = true) }
            ?.let { sensorManager.getDefaultSensor(it.type) }
    }
    val SPO2_SENSOR_TYPE = "spo2"
    val PPG_SENSOR_TYPE = "ppg"
    spo2 = initSensor(SPO2_SENSOR_TYPE)
    ppg = initSensor(PPG_SENSOR_TYPE)


    //Heartrate
    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val calendar = Calendar.getInstance()
                val new = event.values[0]
                if (new.toInt() != 0) {
                    bpmArray.add(new)
                    breath_withBPM.add(currentBreathState)
                    val time =
                        calendar.get(Calendar.MILLISECOND) + calendar.get(Calendar.SECOND) * 1000 + calendar.get(
                            Calendar.MINUTE
                        ) * 1000 * 60
                    ms_bpm.add(time)
                }
                heartrateData.value += new
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            }
        }
        sensorManager.registerListener(listener, heartrate, SensorManager.SENSOR_DELAY_FASTEST)
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
                val calendar1 = Calendar.getInstance()
                ax = event.values[0]
                ay = event.values[1]
                az = event.values[2]
                axArray.add(ax)
                ayArray.add(ay)
                azArray.add(az)
                val time =
                    calendar1.get(Calendar.MILLISECOND) + calendar1.get(Calendar.SECOND) * 1000 + calendar1.get(
                        Calendar.MINUTE
                    ) * 1000 * 60
                ms_imu.add(time)
//                Log.d(ax.toString(), "onSensorChanged: AX")
            }


            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Handle accuracy changes if needed
            }
        }

        sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_FASTEST)

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

        sensorManager.registerListener(listener, gyroscope, SensorManager.SENSOR_DELAY_NORMAL)

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    // Launch the countdown timer
    DisposableEffect(Unit) {
        val countdown = object : CountDownTimer(timeRemaining.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeRemaining = millisUntilFinished.toInt()

                //Give a small haptic feedback for every minute of meditation completed.
                if ((timeRemaining / 1000).toInt() % 60 == 0) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress);
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
                    arrayInhaleExhale = breath_withBPM,
                    breaths = breathPerMin,
                    IEratio = IEratio,
                    pranayamSession = pranayamaSession
                )
                Routes.PranayamSessionScore.passSensorData(sensorData)
                scoreViewModel.updateSensorData(sensorData)
                onSuccess(sensorData)
            }
        }
        countdown.start()
//        mMediaPlayer.start()
        // Cancel the timer when the composable is disposed
        onDispose {
            countdown.cancel()
//            mMediaPlayer.stop()
//            mMediaPlayer.release()
        }
    }
    val temp = Bal_SW_UI(IEratio = IEratio, totalTime = duration, pranayamaSession = pranayamaSession, timeRemainingInSeconds = (timeRemaining/1000))

    currentBreathState = temp
}








