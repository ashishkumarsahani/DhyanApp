package com.epilepto.dhyanapp.presentation.presentation.screens.utils

import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager

fun setupSensor(
    sensorManager: SensorManager,
    sensorType: Int,
    sensorDelay:Int,
    eventListener: SensorEventListener
): Sensor? {
    val sensor = sensorManager.getDefaultSensor(sensorType)
    sensor?.let {
        sensorManager.registerListener(eventListener, it, sensorDelay)
    }
    return sensor
}
