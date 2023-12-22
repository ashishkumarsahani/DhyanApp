package com.epilepto.dhyanapp.presentation.screens.main_screen.home

import android.graphics.Picture
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.epilepto.dhyanapp.R
import com.epilepto.dhyanapp.model.user.User
import com.epilepto.dhyanapp.presentation.components.CircularScoreBoard
import com.epilepto.dhyanapp.presentation.components.LoadingScreen
import com.epilepto.dhyanapp.presentation.screens.main_screen.home.session_result.SessionCard
import com.epilepto.dhyanapp.theme.Roboto
import com.epilepto.dhyanapp.utils.capture
import com.epilepto.dhyanapp.utils.format
import com.epilepto.dhyanapp.utils.saveAndShareSession
import com.google.android.gms.wearable.Node


@Composable
fun HomeScreenContent(
    token: String?,
    getToken: () -> Unit,
    setToken: () -> Unit,
    sendNotification: (String, String) -> Unit,
) {

    val (title, setTitle) = rememberSaveable { mutableStateOf("") }
    val (body, setBody) = rememberSaveable { mutableStateOf("") }
    val (recipientToken, setRecipientToken) = rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(key1 = token) {
        if (token != null) {
            if (token.isEmpty()) {
                Toast.makeText(
                    context,
                    "No token is registered yet",
                    Toast.LENGTH_SHORT
                ).show()
            }
            setRecipientToken(token)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        TextField(
            value = title,
            onValueChange = setTitle,
            placeholder = {
                Text(text = "Title")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )

        TextField(
            value = body,
            onValueChange = setBody,
            placeholder = {
                Text(text = "Body")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )

        TextField(
            value = recipientToken,
            onValueChange = setRecipientToken,
            placeholder = {
                Text(text = "Recipient Token")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Button(onClick = getToken) {
                Text(text = "Get Token")
            }

            Button(onClick = setToken) {
                Text(text = "Set Token (for receiver)")
            }

        }

        Button(
            onClick = {
                if (title.isNotEmpty() && body.isNotEmpty() && recipientToken.isNotEmpty()) {
                    sendNotification(title, body)
                } else {
                    Toast.makeText(context, "Fields can't be empty", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = ShapeDefaults.Small
        ) {
            Text(text = "Send Message")
        }
    }
}


@Composable
fun HomeScreenContent(
    node: Node?,
    user: User?,
    sessionState: SessionScoreDataState
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = sessionState.error) {
        if (sessionState.error.isNotEmpty())
            Toast.makeText(context, sessionState.error, Toast.LENGTH_SHORT).show()
    }

    LoadingScreen(loadingState = sessionState.isLoading)

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val meditationScore =
            sessionState.meditationAnalysis.mapNotNull { it.score }

        val pranayamScore =
            sessionState.pranayamAnalysis.mapNotNull { it.score }

        val avgMedDuration =
            meditationScore.mapNotNull { it.duration }.filter { it > 0 }.let { it.sum() / it.size }
                .toDouble() / 1000


        val avgPranaDuration =
            pranayamScore.mapNotNull { it.duration }.filter { it > 0 }.let { it.sum() / it.size }
                .toDouble() / 1000

        val sortedScores = (meditationScore + pranayamScore).sortedByDescending { it.calender }

        val avgDhyanScore =
            sortedScores.mapNotNull { it.dhyan }.filter { !it.isNaN() && it > 0 }
                .map { it / 100 }
                .filter { it<=1 }
                .let { it.sum() / it.size }

        val avgAasanScore =
            sortedScores.mapNotNull { it.aasana }.filter { !it.isNaN() && it > 0 }
                .map { it / 100 }
                .filter { it<=1 }
                .let { it.sum() / it.size }

        val avgPranaScore =
            sortedScores.mapNotNull { it.prana }.filter { !it.isNaN() && it > 0 }
                .map { it / 100 }
                .filter { it<=1 }
                .let { it.sum() / it.size }

        Log.e(
            "Home Screen",
            "Dhyan :$avgDhyanScore\n" +
                    "Aasana :$avgAasanScore\n" +
                    "Prana :$avgPranaScore\n"
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                CircularScoreBoard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black)
                        .padding(12.dp),
                    dhyanScore = 0.9f,
                    aasanScore = 0.6f,
                    pranaScore = 0.3f,
                    ringSize = 200.dp
                )
            }

            item {
                if (user != null) {
                    UserProfileCard(
                        user = user,
                        avgMedDuration = avgMedDuration,
                        avgPranaDuration = avgPranaDuration
                    )
                }
            }
            item {
                WatchConnectionCard(node = node)
            }

            item {
                Text(
                    text = "Previous Sessions",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Medium
                )
            }


            itemsIndexed(sortedScores) { index, score ->
                val picture = remember { Picture() }

                SessionCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .capture(picture)
                        .padding(8.dp),
                    score = score,
                    onClick = {
                        saveAndShareSession(
                            context = context,
                            picture = picture,
                            index = index,
                            score = score
                        )
                    })
            }
        }
    }
}

@Composable
fun WatchConnectionCard(
    node: Node?
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        val screenHeight = LocalConfiguration.current.screenHeightDp
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = (0.15 * screenHeight).dp)
                .background(color = Color(0xff2f2f2f)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.smart_watch_2),
                contentDescription = "watch",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .scale(.8f)
                    .padding(6.dp)
            )
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {

                Text(
                    text = node?.displayName ?: "WearOS Not Connected",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(ShapeDefaults.Small.topStart))
                        .background(
                            color = Color.Gray
                        )
                ) {
                    Text(
                        text = if (node != null) "Connected"
                        else "Not Connected",
                        color = Color.White,
                        modifier = Modifier.padding(6.dp)
                    )
                }


            }
        }
    }
}


@Composable
fun UserProfileCard(
    user: User,
    avgMedDuration: Double,
    avgPranaDuration: Double
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        val screenHeight = LocalConfiguration.current.screenHeightDp
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = (0.3 * screenHeight).dp)
                .background(color = Color(0xff2f2f2f))
                .padding(8.dp)

        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Image(
                    painter = painterResource(
                        id = if (user.gender == "Male") {
                            R.drawable.male_avatar
                        } else R.drawable.female_avatar
                    ),
                    contentDescription = "watch",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(6.dp)

                )
                Spacer(modifier = Modifier.padding(12.dp))
                Column {

                    Text(
                        text = user.username,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "${user.age} Years",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(ShapeDefaults.Small.topStart))
                        .background(color = Color.Gray)
                        .padding(6.dp)
                ) {
                    Column {

                        Text(
                            text = "Meditation",
                            color = Color.White,
                            modifier = Modifier.padding(6.dp)
                        )

                        Text(
                            text = "${avgMedDuration.format(1)} sec",
                            color = Color.White,
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(ShapeDefaults.Small.topStart))
                        .background(color = Color.Gray)
                        .padding(6.dp)
                ) {
                    Column {
                        Text(
                            text = "Pranayam",
                            color = Color.White,
                            modifier = Modifier.padding(6.dp)
                        )

                        Text(
                            text = "${avgPranaDuration.format(1)} sec",
                            color = Color.White,
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                }
            }
        }
    }
}


