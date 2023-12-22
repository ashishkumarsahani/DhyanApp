package com.epilepto.dhyanapp.presentation.presentation.screens.home.sessionRecords

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.AnchorType
import androidx.wear.compose.foundation.CurvedAlignment
import androidx.wear.compose.foundation.CurvedLayout
import androidx.wear.compose.foundation.curvedColumn
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.curvedText
import com.epilepto.dhyanapp.presentation.presentation.screens.scoreScreen.SharedSessionDetailViewModel

@Composable
fun SessionRecordsDetails(
    sessionDetailViewModel: SharedSessionDetailViewModel
){
    sessionDetailViewModel.sessionDetail.value?.dhyan
    val sessionDetails = sessionDetailViewModel.sessionDetail.observeAsState().value
    val dhyaan_normal = sessionDetailViewModel.sessionDetail.value?.dhyan?.div(100)
    val aasana_normal = sessionDetailViewModel.sessionDetail.value?.asana?.div(100)
    val prana_normal = sessionDetailViewModel.sessionDetail.value?.prana?.div(100)
    Log.d("prana normal", "${sessionDetailViewModel.sessionDetail.value?.prana}")
    val flag_1 = remember { Animatable(0f) }
    val flag_2 = remember { Animatable(0f) }
    val flag_3 = remember { Animatable(0f) }
    LaunchedEffect(Unit){
        if (dhyaan_normal != null && !sessionDetailViewModel.sessionDetail.value!!.dhyan?.isNaN()!!) {
            flag_1.animateTo(
                targetValue = dhyaan_normal,
                animationSpec = tween(1000, easing = LinearEasing)
            )
        }
    }
    LaunchedEffect(Unit){
        if (aasana_normal != null && !sessionDetailViewModel.sessionDetail.value!!.asana?.isNaN()!!) {
            flag_2.animateTo(
                targetValue = aasana_normal,
                animationSpec = tween(1000, easing = LinearEasing)
            )
        }
    }
    LaunchedEffect(Unit){
        if (prana_normal != null && !sessionDetailViewModel.sessionDetail.value!!.prana?.isNaN()!!) {
            flag_3.animateTo(
//                targetValue = prana_normal,
                targetValue = prana_normal,
                animationSpec = tween(1000, easing = LinearEasing)
            )
        }
    }
    Column {
        Box(modifier = Modifier,
        ){
            CurvedLayout(
                modifier = Modifier.fillMaxSize(),
                anchor = 270f,
                anchorType = AnchorType.Center,
                radialAlignment = CurvedAlignment.Radial.Center,
            ){
                curvedColumn(angularAlignment = CurvedAlignment.Angular.Center) {
                    curvedText(text = "Dhyaan", color = Color(0xFFF38686))
                    curvedText(text = "Asana", color = Color.Yellow)
                    curvedText(text = "Prana", color = Color.Green)
                }
            }
            if (dhyaan_normal != null && !sessionDetailViewModel.sessionDetail.value!!.dhyan?.isNaN()!!) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(1.dp),
                    startAngle = 295.5f,
                    endAngle = 245.5f,
                    progress = flag_1.value, // Ensure progress is within [0, 1]
                    strokeWidth = 15.dp,
                    indicatorColor = Color(0xFFF38686),
//                    trackColor = Color.DarkGray
//                    trackColor = Color(0x30FFD9E7)
                    trackColor = Color(0x30FFBDBB)
                )
            }
            if (aasana_normal != null && !sessionDetailViewModel.sessionDetail.value!!.asana?.isNaN()!!) {

                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp),
                    startAngle = 300.5f,
                    endAngle = 240.5f,
                    progress = flag_2.value, // Ensure progress is within [0, 1]
                    strokeWidth = 15.dp,
                    indicatorColor = Color.Yellow,
//                    trackColor = Color.DarkGray
                    trackColor = Color(0x40FFED73)
                )
            }
            if (prana_normal != null && !sessionDetailViewModel.sessionDetail.value!!.prana?.isNaN()!!) {
                Text(text = "Prana", modifier = Modifier)
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(35.dp),
                    startAngle = 305.5f,
                    endAngle = 235.5f,
//                  progress = prana_normal, // Ensure progress is within [0, 1]
                    progress = flag_3.value,
                    strokeWidth = 18.dp,
                    indicatorColor = Color.Green,
//                    trackColor = Color.DarkGray
                    trackColor = Color(0x4066BB6A)
                )
            }
            Column (modifier= Modifier.align(alignment = Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center){
                Text(text = "${sessionDetailViewModel.sessionDetail.value?.time}",modifier = Modifier
                    .padding(5.dp),
                    color = Color.White,
                    style = TextStyle(fontSize = 12.sp)
                )
                Text(text = "${sessionDetailViewModel.sessionDetail.value?.date}",modifier = Modifier
                    .padding(5.dp),
                    color = Color.White,
                    style = TextStyle(fontSize = 12.sp)
                )
            }
        }
    }
}