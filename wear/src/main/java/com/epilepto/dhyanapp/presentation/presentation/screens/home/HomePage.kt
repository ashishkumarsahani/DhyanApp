package com.epilepto.dhyanapp.presentation.presentation.screens.home

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.Text
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.epilepto.dhyanapp.R
import com.epilepto.dhyanapp.presentation.presentation.theme.UserDetails
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomePage(
    navigateToUserInfo: () -> Unit,
    navigateToMeditation: () -> Unit,
    navigateToPranayam: () -> Unit,
    navigateToSessionRecords: () -> Unit,
) {
    val permissionState = rememberPermissionState(permission = Manifest.permission.BODY_SENSORS)
    val lifecycleOwner = LocalLifecycleOwner.current
    Log.d("", "Time Animation")
    DisposableEffect(key1 = lifecycleOwner, effect = {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    permissionState.launchPermissionRequest()
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    })
    val listState = rememberScalingLazyListState()
    val meditation by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.meditation))
    val yoga by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.yoga))
    val records by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.records))
    val circular_waves by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.circular_waves))
    val pranayam_wind by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.pranayam_bg))

    var userDetails by remember { mutableStateOf<UserDetails?>(null) }
    var userDetailsAvailable by remember { mutableStateOf(false) }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
//        val context = LocalContext.current
        val sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        val userDetailsJson = sharedPreferences.getString("user_details", null)

        if (userDetailsJson != null) {
            userDetails = UserDetails.fromJson(userDetailsJson)
            userDetailsAvailable = true
        }
    }

    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        autoCentering = AutoCenteringParams(itemIndex = 0),
        state = listState
    ) {
        item {
            Card(onClick = navigateToUserInfo,
                contentColor = Color.White
            ) {
                if (userDetailsAvailable) {
                    userDetails?.let {
                        Text("Hi, ${it.username}!!")
                    }
                } else {
                    Text("Hi, Guest!!")
                }
            }
        }
        item {
            Card(
                onClick = navigateToMeditation,
                contentColor = Color(0XF2B8B5)
            ) {
                Box(modifier = Modifier.fillMaxSize()){
                    LottieAnimation(modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(50.dp)
                        .fillMaxWidth()
                        .fillMaxSize(), composition = circular_waves, iterations = LottieConstants.IterateForever, speed = 1f)
                    LottieAnimation(modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.BottomEnd), composition = yoga, iterations = LottieConstants.IterateForever)

                    Column {
                        Text(text = "Meditation", fontSize = 20.sp)
                        Text(text = "Music Meditation", color = Color.LightGray, fontSize = 12.sp)
                    }
                }

            }

        }
        item {
            Card(
                onClick = navigateToPranayam,
                contentColor = Color(0XF2B8B5)
            ) {
                Box(modifier = Modifier.fillMaxSize()){
                    LottieAnimation(modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.BottomEnd), composition = pranayam_wind, iterations = LottieConstants.IterateForever)
                    LottieAnimation(modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.BottomEnd), composition = meditation, iterations = LottieConstants.IterateForever, speed = 0.75f)

                    Column {
                        Text(text = "Pranayama", fontSize = 20.sp)
                        Text(text = "Breaths", color = Color.LightGray, fontSize = 12.sp)
                    }
                }
            }
        }

        item {
            Card(
                onClick = navigateToSessionRecords,
                contentColor = Color(0XF2B8B5)
            ) {
                Box(modifier = Modifier.fillMaxSize()){
                    LottieAnimation(modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.BottomEnd), composition = records, iterations = LottieConstants.IterateForever)
                    Column {
                        Text(text = "Statistics", fontSize = 20.sp)
                        Text(text = "Previous sessions", color = Color.LightGray, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}