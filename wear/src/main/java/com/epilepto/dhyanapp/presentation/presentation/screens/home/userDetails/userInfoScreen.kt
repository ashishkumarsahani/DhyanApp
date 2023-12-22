package com.epilepto.dhyanapp.presentation.presentation.screens.home.userDetails

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Text
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.epilepto.dhyanapp.R
import com.epilepto.dhyanapp.presentation.presentation.theme.UserDetails

@Composable
fun UserInfoScreen() {
    val male_avatar by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.male_avatar))
    val female_avatar by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.female_avatar))
    val listState = rememberScalingLazyListState()
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
            Log.d("", "UserDetails1:$userDetails")
        }
    }
       Column(modifier = Modifier.fillMaxSize(),
           verticalArrangement = Arrangement.Center,
           horizontalAlignment = Alignment.CenterHorizontally
       ) {
           if (userDetailsAvailable) {
               userDetails?.let {
                   ScalingLazyColumn(
                       modifier = Modifier.fillMaxSize(),
                       state = listState
                   ){
                       item {
                           LottieAnimation(
                           modifier = Modifier
                               .size(90.dp)
                               .align(Alignment.CenterHorizontally),
                           composition = if (it.gender=="Male") male_avatar else female_avatar,
                           iterations = LottieConstants.IterateForever,
                           speed = 0.8f
                       ) }
                       item {
                           Column(modifier = Modifier.fillMaxSize(),
                               verticalArrangement = Arrangement.Center,
                               horizontalAlignment = Alignment.CenterHorizontally) {
                               Text("${it.username}",color= Color(0XFFF2B8B5))
                               Text("${it.userEmail}",color= Color(0XFFF2B8B5), fontSize = 11.sp)
                               Spacer(modifier = Modifier.height(10.dp))
                           }
                       }
                       item {
                           Text("Age : ${it.age}")
                       }
                       item {
                           Text("Gender : ${it.gender}")
                       }
                       item {
                           Text("Goal : ${it.goal}")
                       }
                   }
               }
           } else {
               ScalingLazyColumn(
                   modifier = Modifier.fillMaxSize(),
                   state = listState
               ){
                   
               }
           }
       }
}