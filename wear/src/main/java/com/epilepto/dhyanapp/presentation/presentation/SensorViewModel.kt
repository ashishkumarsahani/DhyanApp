package com.epilepto.dhyanapp.presentation.presentation

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HeartRateViewModel(private val sensorManager: SensorManager) : ViewModel() {

    private val _heartRateData = MutableLiveData<Int>()
    val heartRateData: LiveData<Int> = _heartRateData

    private val _recordedHeartRates = MutableLiveData<List<Int>>()
    val recordedHeartRates: LiveData<List<Int>> = _recordedHeartRates

    private var isRecording = false
    private val heartRateValues = mutableListOf<Int>()

    private lateinit var sensorEventListener: SensorEventListener

    init {
        initializeSensor()
    }

    private fun initializeSensor() {
        sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val heartRate = event.values[0].toInt()
                _heartRateData.postValue(heartRate)
                if (isRecording) {
                    heartRateValues.add(heartRate)
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        val heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        sensorManager.registerListener(sensorEventListener, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun startRecording() {
        heartRateValues.clear()
        isRecording = true
    }

    fun stopRecording() {
        isRecording = false
        _recordedHeartRates.postValue(heartRateValues.toList())
    }

    override fun onCleared() {
        super.onCleared()
        sensorManager.unregisterListener(sensorEventListener)
    }
}

class HeartRateViewModelFactory(private val sensorManager: SensorManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HeartRateViewModel::class.java)) {
            return HeartRateViewModel(sensorManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
