package com.epilepto.dhyanapp.presentation.screens.main_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.epilepto.dhyanapp.R
import com.epilepto.dhyanapp.theme.NunitoSans

@Composable
fun TimerCard(
    title: String,
    imagePainter: Painter,
    time: Int,
    shape: CornerBasedShape = ShapeDefaults.Medium,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .fillMaxHeight(.30f)
            .clickable { onClick() },
        shape = shape,
        colors = CardDefaults.elevatedCardColors(
            containerColor = backgroundColor
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight.Bold,
                    )
                )

                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .background(color = Color.White, shape = RoundedCornerShape(50)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$time min",
                        color = Color.Black,
                        modifier = Modifier.padding(5.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        fontFamily = NunitoSans
                    )
                }
            }

            Image(
                painter = imagePainter,
                contentDescription = "image",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(12.dp)
                    .scale(1.25f)
            )
            Spacer(modifier = Modifier.padding(10.dp))
        }

    }
}


@Composable
fun HelperCard(
    title: String,
    imagePainter: Painter,
    time: Int,
    shape: CornerBasedShape = ShapeDefaults.Medium,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(.4f)
            .padding(12.dp)
            .clickable { onClick() },
        shape = shape,
        colors = CardDefaults.elevatedCardColors(
            containerColor = backgroundColor
        )
    ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = title,
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            fontWeight = FontWeight.Bold,
                        )
                    )

                    Spacer(modifier = Modifier.padding(2.dp))

                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .background(color = Color.White,
                                shape = RoundedCornerShape(50))
                            ,
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$time min",
                            color = Color.Black,
                            modifier = Modifier.padding(5.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            fontFamily = NunitoSans
                        )
                    }
                }

                Image(
                    painter = imagePainter,
                    contentDescription = "image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(1.25f)
                        .padding(12.dp)
                )
                Spacer(modifier = Modifier.padding(10.dp))
            }
        }
    }

object CardHelperUtils{
    data class CardData(
        val title:String,
        val icon:Int
    )

    val meditationSounds = listOf(
        CardData(
            title = "Om Sound",
            icon = R.drawable.om
        ),
        CardData(
            title = "Dance",
            icon = R.drawable.dance
        ),
        CardData(
            title = "Gayatri Mantra",
            icon = R.drawable.om
        ),
        CardData(
            title = "Guided Session",
            icon = R.drawable.guided_session
        )
    )
    val pranayamaCardTitles = listOf(
        CardData(
            title = "Bhastrika",
            icon = R.drawable.om
        ),
        CardData(
            title = "KapalBhati",
            icon = R.drawable.dance
        ),
        CardData(
            title = "Anulom Vilom",
            icon = R.drawable.anulom_vilom
        ),
        CardData(
            title = "Bhramari",
            icon = R.drawable.bhramari
        )
    )
}

