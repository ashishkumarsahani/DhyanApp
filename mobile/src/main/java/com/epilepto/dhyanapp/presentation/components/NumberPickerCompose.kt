package com.epilepto.dhyanapp.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationResult
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun NumberPicker(
    state: MutableState<Int>,
    modifier: Modifier = Modifier,
    range: List<Int> = emptyList(),
    textStyle: TextStyle = LocalTextStyle.current,
    onStateChanged: (Int) -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()
    val numbersColumnHeight = 100.dp
    val halvedNumbersColumnHeight = numbersColumnHeight / 2
    val halvedNumbersColumnHeightPx =
        with(LocalDensity.current) { halvedNumbersColumnHeight.toPx() }

    fun animatedStateValue(offset: Float): Int =
        state.value - (offset / halvedNumbersColumnHeightPx).toInt()

    val animatedOffset = remember { Animatable(0f) }.apply {
        if (range.isNotEmpty()) {
            val offsetRange = remember(state.value, range) {
                val value = state.value
                val first = -(range.last() - value) * halvedNumbersColumnHeightPx
                val last = -(range.first() - value) * halvedNumbersColumnHeightPx
                first..last
            }
            updateBounds(offsetRange.start, offsetRange.endInclusive)
        }
    }
    val coercedAnimatedOffset = animatedOffset.value % halvedNumbersColumnHeightPx
    val animatedStateValue = animatedStateValue(animatedOffset.value)

    var targetValue by remember { mutableFloatStateOf(1.5f) }
    val animatedScale = animateFloatAsState(targetValue = targetValue, label = "Label Scale")

    Column(
        modifier = modifier
            .wrapContentSize()
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { deltaY ->
                    coroutineScope.launch {
                        animatedOffset.snapTo(animatedOffset.value + deltaY)
                    }
                },
                onDragStopped = { velocity ->
                    coroutineScope.launch {
                        //targetValue = 1.25f
                        val endValue = animatedOffset.fling(
                            initialVelocity = velocity,
                            animationSpec = exponentialDecay(frictionMultiplier = 20f),
                            adjustTarget = { target ->
                                val coercedTarget = target % halvedNumbersColumnHeightPx
                                val coercedAnchors = listOf(
                                    -halvedNumbersColumnHeightPx,
                                    0f,
                                    halvedNumbersColumnHeightPx
                                )
                                val coercedPoint =
                                    coercedAnchors.minByOrNull { abs(it - coercedTarget) }!!
                                val base =
                                    halvedNumbersColumnHeightPx * (target / halvedNumbersColumnHeightPx).toInt()
                                coercedPoint + base
                            }
                        ).endState.value
                        targetValue = 1.5f
                        state.value = animatedStateValue(endValue)
                        onStateChanged(state.value)
                        animatedOffset.snapTo(0f)
                    }
                }
            )
    ) {

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .offset { IntOffset(x = 0, y = coercedAnimatedOffset.roundToInt()) }
                .fillMaxWidth()
        ) {
            val baseLabelModifier = Modifier
                .align(Alignment.Center)
                .padding(8.dp)
                //If the text is getting out of range than the range then make it invisible
//                Label(
//                    text = (animatedStateValue - 2).toString(),
//                    modifier = baseLabelModifier
//                        .offset(y = -halvedNumbersColumnHeight * 2)
//                        .alpha(
//                            if (animatedStateValue - 2 < range.first()) 0f
//                            else
//                                (coercedAnimatedOffset / halvedNumbersColumnHeightPx).scale()
//                        )
//                )
            ProvideTextStyle(value = textStyle) {

                Label(
                    text = (animatedStateValue - 1).toString(),
                    modifier = baseLabelModifier
                        .offset(y = -halvedNumbersColumnHeight)
                        .alpha(
                            if (animatedStateValue - 1 < range.first()) 0f
                            else
                                (coercedAnimatedOffset / halvedNumbersColumnHeightPx).scale()
                        )
                )
                Label(
                    text = animatedStateValue.toString(),
                    modifier = baseLabelModifier
                        .alpha((1 - abs(coercedAnimatedOffset) / halvedNumbersColumnHeightPx).scale())
                        .scale(animatedScale.value)
                )
                Label(
                    text = (animatedStateValue + 1).toString(),
                    modifier = baseLabelModifier
                        .offset(y = halvedNumbersColumnHeight)
                        .alpha(
                            if (animatedStateValue + 1 > range.last()) 0f
                            else
                                (-coercedAnimatedOffset / halvedNumbersColumnHeightPx).scale()
                        )
                )
            }
//                Label(
//                    text = (animatedStateValue + 2).toString(),
//                    modifier = baseLabelModifier
//                        .offset(y = halvedNumbersColumnHeight * 2)
//                        .alpha(
//                            if (animatedStateValue + 2 > range.last()) 0f
//                            else
//                                (-coercedAnimatedOffset / halvedNumbersColumnHeightPx).scale()
//                        )
//                )

        }
    }
}

@Composable
private fun Label(text: String, modifier: Modifier) {
    Text(
        text = text,
        modifier = modifier
            .padding(12.dp)
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    //  Empty to disable text selection
                })
            }
    )
}

private suspend fun Animatable<Float, AnimationVector1D>.fling(
    initialVelocity: Float,
    animationSpec: DecayAnimationSpec<Float>,
    adjustTarget: ((Float) -> Float)?,
    block: (Animatable<Float, AnimationVector1D>.() -> Unit)? = null,
): AnimationResult<Float, AnimationVector1D> {
    val targetValue = animationSpec.calculateTargetValue(value, initialVelocity)
    val adjustedTarget = adjustTarget?.invoke(targetValue)

    return if (adjustedTarget != null) {
        animateTo(
            targetValue = adjustedTarget,
            initialVelocity = initialVelocity,
            block = block
        )
    } else {
        animateDecay(
            initialVelocity = initialVelocity,
            animationSpec = animationSpec,
            block = block,
        )
    }
}

fun Float.scale() = this / 2f + 0.5f

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun PreviewNumberPicker() {
    Box(modifier = Modifier.fillMaxSize()) {
        NumberPicker(
            state = remember { mutableIntStateOf(9) },
            range = (0..20).toList(),
            modifier = Modifier.align(Alignment.Center),
            textStyle = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize
            ),
            onStateChanged = {
                
            }
        )
    }
}