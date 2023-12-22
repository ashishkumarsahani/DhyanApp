package com.epilepto.dhyanapp.presentation.presentation.screens.home.pranayamScreen.light_session

import Light_SW_UI
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.CountDownTimer
import android.util.Log
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
import java.util.Calendar


@Composable
fun PranayamaLightModeStopwatchScreen(
    pranayamaMode: String,
    pranayamaSession: Int,
    IEratio: String,
    onSuccess: (SensorData) -> Unit,
    scoreViewModel: SharedScoreViewModel
) {

    var breathWithBPM by remember { mutableStateOf("") }

    var breathPerMin by remember { mutableIntStateOf(0) }
    var eachBreathTime by remember { mutableIntStateOf(0) }

    //Calculating the the number of breath
    val parts = IEratio.split(":")
    val inhalationTime = parts[0].toInt()
    val exhalationTime = parts[1].toInt()

    eachBreathTime = (inhalationTime + exhalationTime)  //Time for each breah in seconds
    breathPerMin = 60/eachBreathTime         //Number of breaths in minute

    val duration = (pranayamaSession * eachBreathTime) * 1000    //Total duration of session, i.e number of session x time for each session
    var timeRemaining by remember { mutableIntStateOf(duration) }    //Variable to store time remaining, it is updated in countdown timer disposable

    val ctx = LocalContext.current

    val sensorManager: SensorManager = ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager   //Creating the object sesnorManager from SensorManager

    val haptic = LocalHapticFeedback.current     //For Vibrations

    //For SensorsID and SensorManager
    val spo2: Sensor?
    val ppg: Sensor?
    val deviceSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)    //For getting the sensorID of PPG and SPO2
    val heartrate: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)   //For getting the sensorID of Heartrate
    val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)    //For getting the sensorID of Accelometer
    val gyroscope: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)   //For getting the sensorID of GyroScope
//    val heartrateData = remember { mutableStateOf("Heartrate Data:\n") }     //Remembering the heartrate data which can be passed to another function

    //Variables for Accelerometer and GyroScope
    var ax: Float;    // For accelerometer Ax
    var ay: Float;    // For accelerometer Ay
    var az: Float;    // For accelerometer Az
    var gx: Float;    // For accelerometer Gx
    var gy: Float;    // For accelerometer Gy
    var gz: Float;    // For accelerometer Gz
    val axArray = ArrayList<Float>()            //Storing Ax in Array
    val ayArray = ArrayList<Float>()            //Storing Ay in Array
    val azArray = ArrayList<Float>()            //Storing Az in Array
    val gxArray = ArrayList<Float>()            //Storing Gx in Array
    val gyArray = ArrayList<Float>()            //Storing Gx in Array
    val gzArray = ArrayList<Float>()            //Storing Gz in Array

    //Variable for HeartRate and SPO2
    val bpmArray = ArrayList<Float>()       //String BPM Value in Array
    val spo2Array = ArrayList<Float>()      //String SPO2 Value in Array
    val ppgArray = ArrayList<Float>()       //String PPG Value in Array
    val breath_withBPM = ArrayList<String>()     //Storing Breath State Instructions with very BPM Value
    val ms_bpm = ArrayList<Int>()       //For Storing BPM TimeStamp in ms
    val ms_ppg = ArrayList<Int>()       //For Storing PPG TimeStamp in ms
    val ms_spo2 = ArrayList<Int>()      //For Storing SPO2 TimeStamp in ms
    val ms_imu = ArrayList<Int>()       //For Storing IMU TimeStamp in ms

    //Initializing the scores value to 0 and passed as it is now, but this will be getting updated in Success Animations and updated
    val asanaScore = 0f;
    val pranaScore = 0f
    val dhyanScore = 0.0

    //Initializing the PPG And SPO2 SensorID
    fun initSensor(sensorType: String): Sensor? {
        return deviceSensors.find { it.stringType.contains(sensorType, ignoreCase = true) }
            ?.let { sensorManager.getDefaultSensor(it.type) }
    }
    val SPO2_SENSOR_TYPE = "spo2"
    val PPG_SENSOR_TYPE = "ppg"
    spo2 = initSensor(SPO2_SENSOR_TYPE)
    ppg = initSensor(PPG_SENSOR_TYPE)


    //Reading BPM Values and storing with timestamp with the current breath instruction from breathState
    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val calendar = Calendar.getInstance()           //For Calender used in timestamp
                val new = event.values[0]           //The values of sensor is in the event and reading current BPM Values
                if (new.toInt() != 0) {
                    bpmArray.add(new)                       //Adding current non zero BPM to the array
                    breath_withBPM.add(breathWithBPM)       // Adding the current breath instruction from breathState with every new BPM Value
                    val time = calendar.get(Calendar.MILLISECOND) + calendar.get(Calendar.SECOND) * 1000 + calendar.get(
                            Calendar.MINUTE
                        ) * 1000 * 60            //Converting the time format in the epoch value from where we can get the dates and time with single file
                    ms_bpm.add(time)             //Adding this epoch timestamp to the timestamp of BPM
                }
//                heartrateData.value += new   //We can use this to pass the current bpm to another functions
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            }
        }
        sensorManager.registerListener(listener, heartrate, SensorManager.SENSOR_DELAY_FASTEST)
        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }
    // Reading sp02 values and storing with timestamps
    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val calendar = Calendar.getInstance()            //For Calender used in timestamp
                val new = event.values[0]               //The values of sensor is in the event and reading current SPO2 Values
                if (new.toInt() != 0) {
                    spo2Array.add(new)              //Adding current non zero SPO2 to the array
                    val time =
                        calendar.get(Calendar.MILLISECOND) + calendar.get(Calendar.SECOND) * 1000 + calendar.get(
                            Calendar.MINUTE
                        ) * 1000 * 60           //Converting the time format in the epoch value from where we can get the dates and time with single file
                    ms_spo2.add(time)           //Adding this epoch timestamp to the timestamp of SPO2
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

    // Reading PPG data and storing with the timestamp
    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val calendar = Calendar.getInstance()       //For Calender used in timestamp
                val new = event.values[0]           //The values of sensor is in the event and reading current PPG Values
                ppgArray.add(new)                   //Adding current non zero PPG to the array
                val time =
                    calendar.get(Calendar.MILLISECOND) + calendar.get(Calendar.SECOND) * 1000 + calendar.get(
                        Calendar.MINUTE
                    ) * 1000 * 60               //Converting the time format in the epoch value from where we can get the dates and time with single file
                ms_ppg.add(time)                 //Adding this epoch timestamp to the timestamp of PPG
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

    // Reading Accelerometer Data (Ax,Ay,Az) and storing with timestamp
    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val calendar1 = Calendar.getInstance()         //For Calender used in timestamp
                ax = event.values[0]            //The value of Ax is in the event[0] and reading current Ax Values
                ay = event.values[1]            //The value of Ay is in the event[1] and reading current Ay Values
                az = event.values[2]            //The value of Az is in the event[2] and reading current Az Values
                axArray.add(ax)                 //Adding current non zero Ax to the Axarray
                ayArray.add(ay)                 //Adding current non zero Ay to the Ayarray
                azArray.add(az)                 //Adding current non zero Az to the Azarray
                val time =
                    calendar1.get(Calendar.MILLISECOND) + calendar1.get(Calendar.SECOND) * 1000 + calendar1.get(
                        Calendar.MINUTE
                    ) * 1000 * 60                //Converting the time format in the epoch value from where we can get the dates and time with single file
                ms_imu.add(time)                //Adding IMU Timestamps
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

    // Reading Gyroscope Data (Gx,Gy,Gz) and storing with timestamp
    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                gx = event.values[0]            //The value of Gx is in the event[0] and reading current Ax Values
                gy = event.values[1]            //The value of Gy is in the event[1] and reading current Ay Values
                gz = event.values[2]            //The value of Gz is in the event[2] and reading current Az Values
                gxArray.add(gx)                 //Adding current non zero Gx to the Gxarray
                gyArray.add(gy)                 //Adding current non zero Gy to the Gyarray
                gzArray.add(gz)                 //Adding current non zero Gz to the Gzarray
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

    // Launch the countdown timer and it is updating in every 1000ms
    DisposableEffect(Unit) {
        val countdown = object : CountDownTimer(timeRemaining.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeRemaining = millisUntilFinished.toInt()          //Time left in milliseconds and storing in timeRemaining

                //Give a small haptic feedback for every minute of meditation completed.
//                if ((timeRemaining / 1000) % 60 == 0) {
//                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
//                }
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
                    duration = (duration).toFloat(),
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
                Log.d("", "onFinish: Pranayam - $sensorData")
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
    val temp = Light_SW_UI(IEratio = IEratio, totalTime = duration, pranayamaSession= pranayamaSession, timeRemainingInSeconds = (timeRemaining/1000))

    breathWithBPM = temp
}

fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}








