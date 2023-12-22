package com.epilepto.dhyanapp.presentation

import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.epilepto.dhyanapp.presentation.navigation.DhayanNavigation
import com.epilepto.dhyanapp.presentation.presentation.HeartRateViewModel
import com.epilepto.dhyanapp.presentation.presentation.HeartRateViewModelFactory
import com.epilepto.dhyanapp.presentation.presentation.theme.WearMessageSender

class MainActivity : ComponentActivity() {

    private lateinit var messageSender: WearMessageSender
    private lateinit var viewModel: HeartRateViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sensorManager: SensorManager = applicationContext.getSystemService(android.content.Context.SENSOR_SERVICE) as SensorManager
        val factory = HeartRateViewModelFactory(sensorManager)
        viewModel = ViewModelProvider(this, factory)[HeartRateViewModel::class.java]
        setContent {
            val navController = rememberSwipeDismissableNavController()
            DhayanNavigation(navController = navController)
        }
    }
}


