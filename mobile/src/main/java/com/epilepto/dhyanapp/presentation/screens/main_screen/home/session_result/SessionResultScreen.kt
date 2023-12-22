package com.epilepto.dhyanapp.presentation.screens.main_screen.home.session_result

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.epilepto.dhyanapp.model.session.SessionScoreData
import com.epilepto.dhyanapp.model.session.Score
import com.epilepto.dhyanapp.theme.Roboto
import com.epilepto.dhyanapp.utils.DateUtils
import com.epilepto.dhyanapp.utils.format
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SessionScoreCard(
    modifier: Modifier = Modifier,
    sessionNumber: Int,
    sessionScoreData: SessionScoreData,
    onLikeClicked: () -> Unit,
    onCommentClicked: () -> Unit,
    onShareClicked: () -> Unit,
    onClick: () -> Unit
) {

    ElevatedCard(
        modifier = modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(ShapeDefaults.Medium.topStart),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.Gray)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Session $sessionNumber",
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
                    text = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
                        .format(Date(sessionScoreData.sessionDate)),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            ScoreRow("Asana Score", sessionScoreData.asanaScore)
            ScoreRow("Dhyan Score", sessionScoreData.dhyanScore)
            ScoreRow("Prana Score", sessionScoreData.pranaScore)
            ScoreRow("Duration", sessionScoreData.duration, " mins")

            Divider(modifier = Modifier.fillMaxWidth())
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // Like Icon
                IconButton(onClick = onLikeClicked) {
                    Icon(imageVector = Icons.Filled.FavoriteBorder, contentDescription = "Like")
                }

                // Comment Icon
                IconButton(onClick = onCommentClicked) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Comment,
                        contentDescription = "Comment"
                    )
                }

                // Share Icon
                IconButton(onClick = onShareClicked) {
                    Icon(imageVector = Icons.Filled.Share, contentDescription = "Share")
                }
            }
        }
    }
}

@Composable
fun ScoreRow(label: String, score: Double, unit: String = "") {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.DarkGray)
        Text(text = "${score.format(2)}$unit", fontWeight = FontWeight.Bold)
    }
}


@Composable
fun SessionCard(
    modifier: Modifier = Modifier,
    backgroundColor:Color = Color(0xFFEEDEF6),
    score: Score,
    onClick: () -> Unit
) {
    val sessionDate = DateUtils.formatToSessionCardDate(score.calender ?: 0L)

    Card(
        modifier = modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(ShapeDefaults.Medium.topStart),
        colors = CardDefaults.cardColors(containerColor = backgroundColor )

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)){
                    Text(
                        text = "${score.type}",
                        style = MaterialTheme.typography.titleLarge,
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text = sessionDate,
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = Roboto,
                    )
                    Text(
                        text = "${score.duration?.div(1000)} Seconds",
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = Roboto
                    )
                }

                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.Black)
                )
            }
        }
    }
}