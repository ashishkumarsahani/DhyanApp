package com.epilepto.dhyanapp.presentation.screens.additional_details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.epilepto.dhyanapp.presentation.components.LoadingScreen
import com.epilepto.dhyanapp.utils.Constants
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.PagerState
import com.stevdzasan.messagebar.MessageBarState
import kotlinx.coroutines.launch


@OptIn(ExperimentalPagerApi::class)
@Composable
fun AdditionalDetailsScreenContent(
    pagerState: PagerState,
    messageBarState: MessageBarState,
    onFinish: (String, Int, Goal) -> Unit,
    loadingState: Boolean
) {
    val coroutineScope = rememberCoroutineScope()

    var selectedGender by rememberSaveable { mutableStateOf("") }
    var selectedAge by rememberSaveable { mutableStateOf("") }
    var selectedGoal by remember { mutableStateOf<Goal>(Goal.Beginner) }

    LoadingScreen(loadingState = loadingState, modifier = Modifier.fillMaxSize())

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .padding(12.dp),
            indicatorWidth = 70.dp,
            indicatorShape = Shapes().extraSmall,
            indicatorHeight = 6.dp,
            spacing = 3.dp
        )

        HorizontalPager(
            count = Constants.detailScreens.size,
            state = pagerState,
            userScrollEnabled = false
        ) { index ->
            when (Constants.detailScreens[index]) {
                AdditionalDetailsScreenList.Gender -> {
                    GenderInputScreen(
                        age = selectedAge,
                        onAgeChange = {
                            selectedAge = it
                        },
                        gender = selectedGender,
                        onGenderSelect = { gender ->
                            selectedGender = gender
                        },
                        onNext = {
                            if (selectedGender.isEmpty()) {
                                messageBarState.addError(Exception("Please Select A Gender!"))
                            } else {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index + 1)
                                }
                            }
                        })
                }

                AdditionalDetailsScreenList.Goal -> {
                    GoalsSelectionScreen(
                        selectedGoal = selectedGoal,
                        onGoalChange = { goal ->
                            selectedGoal = goal
                        },
                        onPrevious = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index - 1)
                            }
                        },
                        onFinish = {
                            if (selectedGoal == Goal.None) {
                                messageBarState.addError(Exception("Please Select A Goal!"))
                            } else if (selectedAge.isBlank()) {
                                messageBarState.addError(Exception("Please fill out your age."))
                            } else {
                                onFinish(selectedGender, selectedAge.toInt(), selectedGoal)
                            }
                        }
                    )
                }

//                AdditionalDetailsScreenList.Age -> {
//                    AgeDetailsScreen(
//                        ageRange = Constants.ageRange,
//                        selectedAge = selectedAge,
//                        onPrevious = {
//                            coroutineScope.launch {
//                                pagerState.animateScrollToPage(index - 1)
//                            }
//                        },
//                        onNext = {
//                            coroutineScope.launch {
//                                pagerState.animateScrollToPage(index + 1)
//                            }
//                        }
//                    )
//                }

                else -> Unit
            }
        }
    }
}




