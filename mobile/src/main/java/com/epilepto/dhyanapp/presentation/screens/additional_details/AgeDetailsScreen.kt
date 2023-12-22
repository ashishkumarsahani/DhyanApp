package com.epilepto.dhyanapp.presentation.screens.additional_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.epilepto.dhyanapp.presentation.components.NumberPicker


@Composable
fun AgeDetailsScreen(
    selectedAge:MutableState<Int>,
    ageRange:List<Int>,
    onNext: () -> Unit,
    onPrevious: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .weight(.8f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "How old are you?",
                style = TextStyle(
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontWeight = FontWeight.Bold,
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(100.dp))

            NumberPicker(
                state = selectedAge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                onStateChanged = {
                    selectedAge.value = it
                },
                textStyle = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize
                ),
                range = ageRange
            )
        }

            Column(
                modifier = Modifier.weight(.2f)
            ){
                OutlinedButton(
                    onClick = onNext,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    shape = Shapes().small,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                ) {
                    Text(text = "Next")
                }

                TextButton(
                    onClick = onPrevious,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    shape = Shapes().small,
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)
                ) {
                    Text(text = "Previous")
                }

            }
        }
    }

