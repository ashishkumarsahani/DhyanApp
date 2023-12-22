package com.epilepto.dhyanapp.presentation.presentation.theme

import android.util.Log
import androidx.compose.runtime.Composable
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun Score_calculator(
    duration: Float,
    bpm:ArrayList<Float>,
    ax:ArrayList<Float>,
    ay:ArrayList<Float>,
    az:ArrayList<Float>,
    gx:ArrayList<Float>,
    gy:ArrayList<Float>,
    gz:ArrayList<Float>,
    spo2:ArrayList<Float>,): Triple<Double, Float, Float> {

    var moving_avg_heart = 0f
    var moving_avg_hrv = 0f
    val avgD_bpm = ArrayList<Float>()
    var dhyanScore_1 : Double = 0.0
    var pranaScore_1 = 0f
    for (bpm in bpm) {
        var temp: Float;
        moving_avg_heart += bpm;
        if (bpm.toInt() != 0) {
            temp = (60 * 1000) / bpm
            avgD_bpm.add(temp)
        }
    }
    moving_avg_heart /= bpm.size
    for (hrv in avgD_bpm) {
        moving_avg_hrv += hrv
    }
    moving_avg_hrv /= avgD_bpm.size
    for (num in avgD_bpm) {
        dhyanScore_1 += (num - moving_avg_hrv).toDouble().pow(2.0)
    }
    dhyanScore_1 = (sqrt(dhyanScore_1 / avgD_bpm.size))

    //Prana Score Calculation
    spo2.forEach {
        if (it.toInt() != 0) {
            pranaScore_1 += it
        }
    }
    pranaScore_1 /= spo2.size
    //Accelerometer data calculation
    //Ax Mean calculation
    var moving_axArray = 0f
    var moving_ayArray = 0f
    var moving_azArray = 0f
    val filtered_ax = ArrayList<Float>()
    val filtered_ay = ArrayList<Float>()
    val filtered_az = ArrayList<Float>()
    var asanaScore_1 = 0f;
    for (i in ax) {
        moving_axArray += i;
    }
    moving_axArray /= ax.size

    //Ax getting filtered value
    for (_ax in ax) {
        var temp: Float = _ax - moving_axArray
        temp *= temp
        filtered_ax.add(temp)
    }
    //Ay Mean calculation
    for (_ay in ay) {
        moving_ayArray += _ay;
    }
    moving_ayArray = moving_axArray / ay.size

    //Ay getting filtered value
    for (_ay in ay) {
        var temp: Float = _ay - moving_ayArray
        temp *= temp
        filtered_ay.add(temp)
    }

    //Az Mean calculation
    for (_az in az) {
        moving_azArray += _az;
    }
    moving_azArray /= az.size

    //Az getting filtered value
    for (_az in az) {
        var temp = 0f
        temp = _az - moving_azArray
        temp *= temp
        filtered_az.add(temp)
    }
    filtered_ax.forEachIndexed { index, value ->
        asanaScore_1  += value + filtered_ay[index] + filtered_az[index]
    }
    asanaScore_1 /= filtered_ax.size
    asanaScore_1 = sqrt(asanaScore_1.toDouble()).toFloat()
    Log.d("", "Score_calculator: $dhyanScore_1`")
    return Triple(dhyanScore_1, asanaScore_1, pranaScore_1)
}