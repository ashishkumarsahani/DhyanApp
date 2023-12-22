package com.epilepto.dhyanapp.presentation.presentation.screens.scoreScreen

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.CardDefaults
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Text
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.epilepto.dhyanapp.R
import com.epilepto.dhyanapp.presentation.data.services.SharedPreferencesManager
import com.epilepto.dhyanapp.presentation.presentation.theme.UserDetails


@Composable
fun ScoresScreen(
    onSave: () -> Unit,
    onDone: () -> Unit,
    navigateToPranayamSession: () -> Unit,
    ScoreviewModel: SharedScoreViewModel,
    UserInfoViewModel: SharedUserInfoViewModel
) {
    val userID = SharedPreferencesManager.getUserId(LocalContext.current)      //Reading the UserID from the Shared Preferences from Data layer
    val sensorData = ScoreviewModel.sensorData.observeAsState().value
    val listState = rememberScalingLazyListState()
    val male_avatar by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.male_avatar))
    val female_avatar by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.female_avatar))
    val done by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.sucess))
    val records by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.records))
    var dhyaan_normal = ScoreviewModel.sensorData.value?.dhyanScore?.div(100)
    var aasana_normal = ScoreviewModel.sensorData.value?.asanaScore?.div(100)
    var prana_normal = ScoreviewModel.sensorData.value?.pranaScore?.div(100)
    var dhyaan_local = ScoreviewModel.sensorData.value?.dhyanScore
    var asana_local = ScoreviewModel.sensorData.value?.asanaScore
    var prana_local = ScoreviewModel.sensorData.value?.pranaScore
    var duration = ScoreviewModel.sensorData.value?.duration?.div(1000)
    val totalTime = duration?.let { formatTime(it.toInt()) }


    val context = LocalContext.current
    var userDetails by remember { mutableStateOf<UserDetails?>(null) }
    val sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
    val userDetailsJson = sharedPreferences.getString("user_details", null)
    if (userDetailsJson != null) {
        userDetails = UserDetails.fromJson(userDetailsJson)
    }
    //To go back to HomePage
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState
    ) {
        //User Profile
        item {
            Row{
                LottieAnimation(
                    modifier = Modifier
                        .size(50.dp),
                    composition = if (userDetails?.gender =="Male") male_avatar else female_avatar,
                    iterations = LottieConstants.IterateForever,
                    speed = 0.8f,
                    alignment = Alignment.Center
                )
                Column {
                    Spacer(modifier = Modifier.size(15.dp))
//                    Text(userId ?: "Harsh")
                    userDetails?.username?.let { Text(it, fontSize = 12.sp) }
                }
            }
        }

        //Duration
        item {
            Card(
                onClick = { },
                contentColor = Color(0XF2B8B5),
                backgroundPainter = CardDefaults.cardBackgroundPainter(startBackgroundColor =  Color.Black, endBackgroundColor = Color.DarkGray, gradientDirection = LayoutDirection.Ltr),
            ) {
                Row {
                    Text(text = "Duration : ", fontSize = 12.sp)
                    Text(text = "$totalTime Minutes", color = Color.LightGray, fontSize = 12.sp)
                }
            }

        }

        //Dhyana Score
        item {
            Card(
                onClick = navigateToPranayamSession,
                contentColor = Color(0XF2B8B5),
                backgroundPainter = CardDefaults.cardBackgroundPainter(startBackgroundColor =  Color.Black, endBackgroundColor = Color.DarkGray, gradientDirection = LayoutDirection.Ltr),
            ) {
                Row {
                    Column {
                        Text(text = "Dhyaan Score", fontSize = 12.sp)
                        Text(text = "$dhyaan_local", color = Color.LightGray, fontSize = 12.sp)
                    }
                    Column {
//                       Box(modifier = Modifier){
                        if (dhyaan_normal != null && !ScoreviewModel.sensorData.value?.dhyanScore?.isNaN()!!) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(1.dp),
                                startAngle = 295.5f,
                                endAngle = 245.5f,
                                progress = dhyaan_normal, // Ensure progress is within [0, 1]
                                strokeWidth = 5.dp,
                                indicatorColor = Color(0xFFF38686),
                                trackColor = Color.Black
                            )
                        }
//                       }
                    }

                }
            }
        }

        //Aasna Score
        item {
            Card(
                onClick = navigateToPranayamSession,
                contentColor = Color(0XF2B8B5),
                backgroundPainter = CardDefaults.cardBackgroundPainter(startBackgroundColor =  Color.Black, endBackgroundColor = Color.DarkGray, gradientDirection = LayoutDirection.Ltr),
            ) {

                Row {
                    Column {
                        Text(text = "Aasana Score", fontSize = 12.sp)
                        Text(text = "$asana_local", color = Color.LightGray, fontSize = 12.sp)
                    }
                    Column {
//                        Box(modifier = Modifier){
                        if (aasana_normal != null && !ScoreviewModel.sensorData.value?.asanaScore?.isNaN()!!) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(1.dp),
                                startAngle = 295.5f,
                                endAngle = 245.5f,
                                progress = aasana_normal, // Ensure progress is within [0, 1]
                                strokeWidth = 5.dp,
                                indicatorColor = Color.Yellow,
                                trackColor = Color.Black
                            )
                        }
//                        }
                    }

                }

            }
        }

        //Prana Score
        item {
            Card(
                onClick = navigateToPranayamSession,
                contentColor = Color(0XF2B8B5),
                backgroundPainter = CardDefaults.cardBackgroundPainter(startBackgroundColor =  Color.Black, endBackgroundColor = Color.DarkGray, gradientDirection = LayoutDirection.Ltr),
            ) {
                Row {
                    Column {
                        Text(text = "Prana Score", fontSize = 12.sp)
                        Text(text = "$prana_local", color = Color.LightGray, fontSize = 12.sp)
                    }
                    Column {
//                        Box(modifier = Modifier){
                        if (prana_normal != null && !ScoreviewModel.sensorData.value?.pranaScore?.isNaN()!!) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(1.dp),
                                startAngle = 295.5f,
                                endAngle = 245.5f,
                                progress = prana_normal,// Ensure progress is within [0, 1]
                                strokeWidth = 5.dp,
                                indicatorColor = Color.Green,
                                trackColor = Color.Black
                            )
                        }
//                        }
                    }

                }
            }
        }

        //Saving to Firebase
        item {
            Chip(
                onClick = onSave,
                colors = ChipDefaults.secondaryChipColors(),
                label = { Text("Save the session") },
                icon = {
                    LottieAnimation(
                        modifier = Modifier.size(50.dp),
                        composition = records,
                        iterations = LottieConstants.IterateForever
                    )
                })
        }

        //Done
        item {
            Chip(
                onClick = onDone,
                colors = ChipDefaults.secondaryChipColors(),
                label = { Text("Done") },
                icon = {
                    LottieAnimation(
                        modifier = Modifier.size(50.dp),
                        composition = done,
                        iterations = LottieConstants.IterateForever
                    )
                })
        }
    }
}

fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}