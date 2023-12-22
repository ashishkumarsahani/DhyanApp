package com.epilepto.dhyanapp.presentation.screens.additional_details

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.epilepto.dhyanapp.theme.primaryColor

sealed class Goal(
    val title: String,
    val description: String
    ) {
        data object Beginner : Goal(
        "Beginner Yoga",
        "Start your yoga journey with foundational poses and gentle stretches designed for beginners"
    )

    data object Intermediate : Goal(
        "Intermediate Yoga",
        "Progress to more challenging postures and deepen your practice with intermediate yoga"
    )

    data object Advanced : Goal(
        "Advanced Yoga",
        "Explore advanced poses, sequences, and meditation techniques for experienced yogis seeking mastery and spiritual growth"
    )

    data object None : Goal("", "")

    fun getGoalFromTitle(title:String): Goal {
        return when(title){
            "Beginner Yoga" -> Beginner

            "Intermediate Yoga" -> Intermediate

            "Advanced Yoga" -> Advanced

            else -> None
        }
    }
}


@Composable
fun GoalsSelectionScreen(
    selectedGoal: Goal,
    onGoalChange:(Goal)->Unit,
    onFinish:()->Unit,
    onPrevious:()->Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .weight(.8f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "What's your goal?",
                style = TextStyle(
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontWeight = FontWeight.Bold,
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            Column {
                GoalCard(
                    goal = Goal.Beginner,
                    onSelect = onGoalChange,
                    isSelected = selectedGoal == Goal.Beginner
                )
      //          Spacer(modifier = Modifier.padding(12.dp))
                GoalCard(
                    goal = Goal.Intermediate,
                    onSelect = onGoalChange,
                    isSelected = selectedGoal == Goal.Intermediate
                )
   //             Spacer(modifier = Modifier.padding(12.dp))
                GoalCard(
                    goal = Goal.Advanced,
                    onSelect = onGoalChange,
                    isSelected = selectedGoal == Goal.Advanced
                )
                Spacer(modifier = Modifier.padding(12.dp))
            }

        }

        Column(
            modifier = Modifier.weight(.2f)
        ) {
            OutlinedButton(
                onClick = onFinish,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                shape = Shapes().small,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
            ) {
                Text(text = "Finish")
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalCard(
    goal: Goal,
    onSelect: (Goal) -> Unit,
    isSelected: Boolean
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .border(1.dp,Color.LightGray,Shapes().small)
        ,
        onClick = {
            onSelect(goal)
        },
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (isSelected) primaryColor else Color.White
        ),
        shape = Shapes().small,
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            Column(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text(
                    text = goal.title,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(text = goal.description)
            }
        }

    }
}