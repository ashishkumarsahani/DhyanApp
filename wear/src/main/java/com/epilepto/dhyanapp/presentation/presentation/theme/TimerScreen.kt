package com.epilepto.dhyanapp.presentation.presentation.theme

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.epilepto.dhyanapp.R
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.audio.AudioOutput
import com.google.android.horologist.audio.SystemAudioRepository
import com.google.android.horologist.audio.VolumeState
import kotlinx.coroutines.flow.StateFlow


@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalHorologistApi::class)
@Composable
fun SetTimerScreen(
    session: String,
    navigateToTimerAnimation: (duration:Int) -> Unit,
    navigatetoTest: (duration:Int) -> Unit
) {
    val context = LocalContext.current
    val packageManager = context.packageManager
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    var value by remember { mutableIntStateOf(1) }
    var isTimerRunning by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current
    val itemList = List(100) { index -> "${(index) + 1}" }
    val green_leaf by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.green_leaf))
    //Checking Bluetooth Connection
//    val hasBluetoothA2DP by remember {
//        mutableStateOf(
//            audioOutputAvailable(
//                audioManager,
//                packageManager,
//                AudioDeviceInfo.TYPE_BLUETOOTH_A2DP
//            )
//        )
//    }
    val ctx = LocalContext.current
    val audioRepository = SystemAudioRepository.fromContext(ctx)
    val volumeState: StateFlow<VolumeState> = audioRepository.volumeState
    val audioOutput: StateFlow<AudioOutput> = audioRepository.audioOutput
    val output = audioOutput.value


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            modifier = Modifier
                .size(70.dp), composition = green_leaf, iterations = LottieConstants.IterateForever
        )
        Text(text = if (session!="{Guided Meditation}") "Choose the session duration" else "", fontSize = 15.sp, color = Color.DarkGray)
        //This handles everything and navigates to the stopwatch page
        if (session!="{Guided Meditation}"){
            LazyRow {
                items(itemList) { item ->
                    Text(
                        text = item,
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
//                            navigatetoTest(item.toInt())
                                navigateToTimerAnimation(item.toInt())
                            },
                        fontSize = 30.sp,
                        color = Color(0XFFF38686)
                    )
                }
            }
        } else {
            val availableDevices = remember { mutableStateOf<List<AudioOutput>>(emptyList()) }
            LaunchedEffect(true) {
                val outputFlow = audioRepository.audioOutput
                if (output is AudioOutput.WatchSpeaker) {
                    Toast.makeText(
                        context,
                        "Please Connect with a Headset",
                        Toast.LENGTH_SHORT
                    ).show()
                } else Log.d("", "TestAudioRepo:${output.name} ")
            }
            if(!audioOutputAvailable(AudioDeviceInfo.TYPE_BLUETOOTH_A2DP)){
                Chip(
                    onClick = {
                        // Connect to the selected audio device
                        val intent = with (Intent(Settings.ACTION_BLUETOOTH_SETTINGS)) {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            putExtra("EXTRA_CONNECTION_ONLY", true)
                            putExtra("EXTRA_CLOSE_ON_CONNECT", true)
                            putExtra("android.bluetooth.devicepicker.extra.FILTER_TYPE", 1)
                        }
                        startActivity(context,intent, Bundle.EMPTY)
                    },
                    colors = ChipDefaults.secondaryChipColors(),
                    label = { Text( "Choose the Device", fontSize = 12.sp, color = Color.LightGray) },
                )
            } else {
                Toast.makeText(
                    context,
                    "${output.name} is Connected",
                    Toast.LENGTH_SHORT
                ).show()
            }
            Spacer(modifier = Modifier.height(5.dp))
            Chip(
                modifier = Modifier.size(50.dp),
                onClick ={} ,
                colors = ChipDefaults.secondaryChipColors(),
                label = {
                    IconButton(
                        onClick = {navigateToTimerAnimation(12)}//Guided Meditation audio of 12 min
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                            contentDescription = "next"
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun audioOutputAvailable(type: Int): Boolean {
    val context= LocalContext.current
    val packageManager = context.packageManager
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    if (!packageManager.hasSystemFeature(PackageManager.FEATURE_AUDIO_OUTPUT)) {
        return false
    }
    return audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS).any { it.type==type}
}