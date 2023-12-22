package com.epilepto.dhyanapp.presentation.presentation.theme

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.epilepto.dhyanapp.R
import com.epilepto.dhyanapp.presentation.data.services.SharedPreferencesManager
import com.epilepto.dhyanapp.presentation.presentation.screens.scoreScreen.SharedUserInfoViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson


@Composable
fun StartAnimation(
    navigateToMain: () -> Unit,
    SharedUserInfoViewModel: SharedUserInfoViewModel
) {
    val animationTime = 5000
    var timeRemaining by remember { mutableIntStateOf(animationTime) }
    var Userstate by remember { mutableStateOf("Guest") }
    val context = LocalContext.current
    val userID = SharedPreferencesManager.getUserId(context)
    LogoAnimation()
    try {
        val db = Firebase.firestore
        val user = userID?.let { db.collection("users").document(it) }

        if (user != null) {

            user.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
    //                    val mPrefs: SharedPreferences = getSharedPreferences("",MODE_PRIVATE)
                        val username = document.get("username").toString()
                        val userEmail = document.get("userEmail").toString()
                        val gender = document.get("gender").toString()
                        val addedAt = document.get("addedAt").toString()
                        val goal = document.get("goal").toString()
                        val userId = document.get("userId").toString()
                        val age = document.get("age").toString()
                        val userDetails = UserDetails(username, userEmail, gender, addedAt, goal, userId, age)
                        Log.d("userDetails", "userDetails:$userDetails ")
                        val userDetailsJson = UserDetails.toJson(userDetails)
                        val sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
                        Userstate = username

                        SharedUserInfoViewModel.updateUserInfo(userDetails)

                        // Save userDetailsJson to SharedPreferences
                        val editor = sharedPreferences.edit()
                        editor.putString("user_details", userDetailsJson)
                        editor.apply()
                    } else {
                        Log.d("", "document is empty")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("error in loading userData", "$exception ")
                }
        }
    }catch (e: Exception) {
        Log.e("Error in loading userdata", e.message, e)
    }

    DisposableEffect(Unit) {
        val countdown = object : CountDownTimer(timeRemaining.toLong(), 100) {
            override fun onTick(millisUntilFinished: Long) {
                timeRemaining = millisUntilFinished.toInt()
                Log.d( "onTick: Time for animation",timeRemaining.toString())
            }

            override fun onFinish() {
                Toast.makeText(
                    context,

                    "Login as $Userstate",
                    Toast.LENGTH_SHORT
                ).show()
                navigateToMain()
            }
        }
        countdown.start()

        // Cancel the timer when the composable is disposed
        onDispose {
            countdown.cancel()
        }
    }

}

@Composable
fun LogoAnimation() {
    val splash by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash))
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            modifier = Modifier
                .size(150.dp)
                .fillMaxWidth()
                .fillMaxSize(),
            composition = splash, iterations = LottieConstants.IterateForever, speed = 0.7f,
        )
        Text(text = "Dhyan App")
    }
}

data class UserDetails(
    val username: String?,
    val userEmail: String?,
    val gender: String?,
    val addedAt: String?,
    val goal: String?,
    val userId: String?,
    val age: String?
) {
    companion object {
        fun fromJson(json: String): UserDetails {
            return Gson().fromJson(json, UserDetails::class.java)
        }

        fun toJson(userDetails: UserDetails): String {
            return Gson().toJson(userDetails)
        }
    }
}

