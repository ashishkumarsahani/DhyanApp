package com.epilepto.dhyanapp.presentation.presentation.screens.home.sessionRecords


import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.CardDefaults
import androidx.wear.compose.material.CircularProgressIndicator


import androidx.wear.compose.material.Text
import com.epilepto.dhyanapp.presentation.domain.models.sessionData.Score
import com.epilepto.dhyanapp.presentation.domain.models.sessionData.SessionDetails
import com.epilepto.dhyanapp.presentation.presentation.screens.scoreScreen.SharedSessionDetailViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun SessionRecordsScreen(
    navigateToSessionDetails: () -> Unit,
//    navigateToSessionDetails:(dhyan:Float?,asna:Float?,prana:Float?,duration:Float?,time:String?,date:String?)->Unit,
    loadingState:Boolean,
    scores:List<Score>,
    sessionDetailViewModel: SharedSessionDetailViewModel
) {
    val datesArray = ArrayList<String>()
    val onlyDatesArray = ArrayList<String>()
    val onlyTimeArray = ArrayList<String>()

    val listState = rememberScalingLazyListState()

    val dateTimes = scores.mapNotNull { score ->
        score?.calender?.let { convertEpochToDateTime(it) }
    }
    val dates = scores.mapNotNull { score ->
        score?.calender?.let { convertEpochToDate(it) }
    }
    val time = scores.mapNotNull { score ->
        score?.calender?.let { convertEpochToTime(it) }
    }

    // Now dateTimes contains the corresponding date-time strings
    dateTimes.forEach { dateTime ->
        datesArray.add(dateTime)
    }
    dates.forEach { date ->
        onlyDatesArray.add(date)
    }
    time.forEach { time ->
        onlyTimeArray.add(time)
    }

    AnimatedVisibility(visible = loadingState) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                indicatorColor = Color(0XFFF38686),
                trackColor = Color.Black
            )
        }
    }

    AnimatedVisibility(visible = !loadingState && scores.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No sessions recorded yet.")
        }
    }

    AnimatedVisibility(
        visible = scores.isNotEmpty(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize(),
                autoCentering = AutoCenteringParams(itemIndex = 0),
                state = listState
            ) {
                itemsIndexed(scores) { index, item ->
                    val type = scores[index].type
                    Card(
                        onClick= {
                            val sessionDetails = SessionDetails(
                                type = scores[index].type,
                                dhyan = scores[index].dhyan,
                                asana = scores[index].aasana,
                                prana = scores[index].prana,
                                duration = scores[index].duration,
                                time = onlyTimeArray[index],
                                date = onlyDatesArray[index]
                            )
                            Log.d("Scores", " $scores")
                            Log.d("prana", "${scores[index].prana}")
                            sessionDetailViewModel.updateSessionDetail(sessionDetails)
                            navigateToSessionDetails()
//                                    navigateToSessionDetails(scores[index]?.dhyan,scores[index].aasana,scores[index]?.prana,scores[index]?.duration,onlyTimeArray[index],onlyDatesArray[index])
                                 },
                        backgroundPainter = CardDefaults.cardBackgroundPainter(startBackgroundColor =  Color.Black, endBackgroundColor = if(type=="Meditation") Color(0xFF023E8A) else if(type=="Pranayam") Color(0x5066BB6A) else Color.DarkGray, gradientDirection = LayoutDirection.Ltr),
                        modifier = Modifier,
                    ) {
                        val sessionIndex = index + 1
                        val dhyaan_normal = (scores[index].dhyan)?.div(100)
                        val aasana_normal = (scores[index].aasana)?.div(100)
                        val prana_normal = (scores[index].prana)?.div(100)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Row {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "$sessionIndex",
                                            modifier = Modifier.padding(5.dp),
                                            color = Color.White,
                                            textAlign = TextAlign.Start,
                                            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                        )
                                        Text(text = onlyTimeArray[index],modifier = Modifier
                                            .padding(1.dp),
                                            color = Color(0XFFF2B8B5),
                                            textAlign = TextAlign.End,
                                            style = TextStyle(fontSize = 12.sp))
                                    }
                                    Text(text = onlyDatesArray[index],modifier = Modifier
                                        .padding(5.dp)
                                        .align(Alignment.CenterHorizontally),
                                        color = Color(0XFFF2B8B5),
                                        textAlign = TextAlign.Start,
                                        style = TextStyle(fontSize = 12.sp))
                                }
                                Column {
                                    Box(modifier = Modifier,
                                    ){

                                        if (dhyaan_normal != null && !(scores[index].dhyan)?.isNaN()!!) {
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
                                        if (aasana_normal != null && !(scores[index].aasana)?.isNaN()!!) {
                                            CircularProgressIndicator(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(8.dp),
                                                startAngle = 300.5f,
                                                endAngle = 240.5f,
                                                progress = aasana_normal, // Ensure progress is within [0, 1]
                                                strokeWidth = 5.dp,
                                                indicatorColor = Color.Yellow,
                                                trackColor = Color.Black
                                            )
                                        }
                                        if (prana_normal != null && !(scores[index].prana)?.isNaN()!! ) {
                                            CircularProgressIndicator(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(15.dp),
                                                startAngle = 305.5f,
                                                endAngle = 235.5f,
                                                progress = prana_normal,
                                                strokeWidth = 5.dp,
                                                indicatorColor = Color.Green,
                                                trackColor = Color.Black
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
    }



}
fun convertEpochToDateTime(epoch: Long): String {
    val instant = Instant.ofEpochMilli(epoch)
    val formatter = DateTimeFormatter.ofPattern("dd-MMM-yy HH:mm").withZone(ZoneId.of("UTC")) // Set the timezone if needed
    return formatter.format(instant)
}
fun convertEpochToDate(epoch: Long): String {
    val instant = Instant.ofEpochMilli(epoch)
    val formatter = DateTimeFormatter.ofPattern("dd-MMM-yy").withZone(ZoneId.of("UTC+05:30")) // Set the timezone if needed
    return formatter.format(instant)
}
fun convertEpochToTime(epoch: Long): String {
    val instant = Instant.ofEpochMilli(epoch)
//    val formatter = DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.of("UTC")) // Set the timezone if needed
    val formatter = DateTimeFormatter.ofPattern("hh:mm a").withLocale(Locale.US).withZone(ZoneId.of("UTC+05:30"))
    return formatter.format(instant)
}

