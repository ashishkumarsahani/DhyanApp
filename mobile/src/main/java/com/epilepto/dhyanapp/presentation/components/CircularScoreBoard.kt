package com.epilepto.dhyanapp.presentation.components

import androidx.annotation.FloatRange
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.min

@Composable
fun CircularScoreBoard(
    modifier: Modifier = Modifier,
    ringSize: Dp = 100.dp,
    dhyanScore: Float,
    aasanScore: Float,
    pranaScore: Float
) {

    var startAnimation by remember { mutableStateOf(false) }

    val flag1 = animateFloatAsState(
        targetValue = if (startAnimation) dhyanScore else 0f,
        animationSpec = tween(1000, easing = LinearEasing),
        label = "Dhyan"
    )
    val flag2 = animateFloatAsState(
        targetValue = if (startAnimation) aasanScore else 0f,
        animationSpec = tween(1000, easing = LinearEasing),
        label = "Aasan"
    )

    val flag3 = animateFloatAsState(
        targetValue = if (startAnimation) pranaScore else 0f,
        animationSpec = tween(1000, easing = LinearEasing),
        label = "Prana"
    )

    /*LaunchedEffect(Unit) {
        flag_1.animateTo(
            targetValue = dhyanScore,
            animationSpec = tween(1000, easing = LinearEasing)
        )

        flag_2.animateTo(
            targetValue = aasanScore,
            animationSpec = tween(1000, easing = LinearEasing)
        )

        flag_3.animateTo(
            targetValue = pranaScore,
            animationSpec = tween(1000, easing = LinearEasing)
        )
    }*/

    LaunchedEffect(key1 = Unit){
        startAnimation = true
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
//        CurvedLayout(
//            modifier = Modifier.size(200.dp),
//            anchor = 270f,
//            anchorType = AnchorType.Center,
//            radialAlignment = CurvedAlignment.Radial.Center,
//        ) {
//            curvedColumn(angularAlignment = CurvedAlignment.Angular.Center) {
//                curvedText(text = "Dhyaan", color = Color(0xFFF38686))
//                curvedText(text = "Asana", color = Color.Yellow)
//                curvedText(text = "Prana", color = Color.Green)
//            }
//        }
        CircularProgressIndicator(
            modifier = Modifier
                .size(ringSize)
                .padding(1.dp),
            startAngle = 295.5f,
            endAngle = 245.5f,
            progress = flag1.value, // Ensure progress is within [0, 1]
            strokeWidth = 15.dp,
            indicatorColor = Color(0xFFF38686),
//                    trackColor = Color.DarkGray
//                    trackColor = Color(0x30FFD9E7)
            trackColor = Color(0x30FFBDBB)
        )
        CircularProgressIndicator(
            modifier = Modifier
                .size(ringSize)
                .padding(18.dp),
            startAngle = 300.5f,
            endAngle = 240.5f,
            progress = flag2.value, // Ensure progress is within [0, 1]
            strokeWidth = 15.dp,
            indicatorColor = Color.Yellow,
//                    trackColor = Color.DarkGray
            trackColor = Color(0x40FFED73)
        )
        Text(text = "Prana", modifier = Modifier)

        CircularProgressIndicator(
            modifier = Modifier
                .size(ringSize)
                .padding(35.dp),
            startAngle = 305.5f,
            endAngle = 235.5f,
            progress = flag3.value, // Ensure progress is within [0, 1]
            strokeWidth = 18.dp,
            indicatorColor = Color.Green,
//                    trackColor = Color.DarkGray
            trackColor = Color(0x4066BB6A)
        )
    }
}


    @Composable
    public fun CircularProgressIndicator(
        @FloatRange(from = 0.0, to = 1.0)
        progress: Float,
        modifier: Modifier = Modifier,
        startAngle: Float = 270f,
        endAngle: Float = startAngle,
        indicatorColor: Color = MaterialTheme.colorScheme.background,
        trackColor: Color = MaterialTheme.colorScheme.background,
        strokeWidth: Dp = 10.dp,
    ) {
        // Canvas internally uses Spacer.drawBehind.
        // Using Spacer.drawWithCache to optimize the stroke allocations.
        Spacer(
            modifier
                .progressSemantics(progress)
                .size(40.dp)
                .focusable()
                .drawWithCache {
                    val backgroundSweep = 360f - ((startAngle - endAngle) % 360 + 360) % 360
                    val progressSweep = backgroundSweep * progress.coerceIn(0f..1f)
                    val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)

                    onDrawWithContent {
                        // Draw a background
                        drawCircularIndicator(
                            startAngle,
                            backgroundSweep,
                            trackColor,
                            stroke
                        )

                        // Draw a progress
                        drawCircularIndicator(
                            startAngle,
                            progressSweep,
                            indicatorColor,
                            stroke
                        )
                    }
                }
        )
    }
private fun DrawScope.drawCircularIndicator(
    startAngle: Float,
    sweep: Float,
    color: Color,
    stroke: Stroke
) {
    // To draw this circle we need a rect with edges that line up with the midpoint of the stroke.
    // To do this we need to remove half the stroke width from the total diameter for both sides.
    val diameter = min(size.width, size.height)
    val diameterOffset = stroke.width / 2
    val arcDimen = diameter - 2 * diameterOffset
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweep,
        useCenter = false,
        topLeft = Offset(
            diameterOffset + (size.width - diameter) / 2,
            diameterOffset + (size.height - diameter) / 2
        ),
        size = Size(arcDimen, arcDimen),
        style = stroke
    )
}

//@Composable
//public fun CurvedLayout(
//    modifier: Modifier = Modifier,
//    anchor: Float = 270f,
//    anchorType: AnchorType = AnchorType.Center,
//    // TODO: reimplement as modifiers
//    radialAlignment: CurvedAlignment.Radial? = null,
//    angularDirection: CurvedDirection.Angular = CurvedDirection.Angular.Normal,
//    contentBuilder: CurvedScope.() -> Unit
//) {
//    // Note that all angles in the function are in radians, and the anchor parameter is in degrees
//
//    val curvedLayoutDirection = initialCurvedLayoutDirection(angularDirection)
//
//    // Create the curved tree and subscribe to be recomposed when any part changes, this may not be
//    // optimal but since we have only one measure block (the one here) for all the curved layout,
//    // we still need to do most of the work when content changes.
//    val curvedRowChild = CurvedRowChild(curvedLayoutDirection, radialAlignment, contentBuilder)
//
//    Layout(
//        modifier = modifier.drawWithContent {
//            with(curvedRowChild) { draw() }
//            drawContent()
//        },
//
//        content = {
//            curvedRowChild.SubComposition()
//        }
//    ) { measurables, constraints ->
//        require(constraints.hasBoundedHeight || constraints.hasBoundedWidth) {
//            "either height or width should be bounded"
//        }
//        // We take as much room as possible, the same in both dimensions, within the constraints
//        val diameter = min(
//            if (constraints.hasBoundedWidth) constraints.maxWidth else Int.MAX_VALUE,
//            if (constraints.hasBoundedHeight) constraints.maxHeight else Int.MAX_VALUE,
//        )
//        val radius = diameter / 2f
//
//        // Give the curved row scope the information needed to measure and map measurables
//        // to children.
//        with(CurvedMeasureScope(subDensity = this, curvedLayoutDirection, radius)) {
//            with(curvedRowChild) {
//                val iterator = measurables.iterator()
//                initializeMeasure(iterator)
//                require(!iterator.hasNext()) { "unused measurable" }
//            }
//        }
//
//        curvedRowChild.estimateThickness(radius)
//
//        curvedRowChild.radialPosition(
//            parentOuterRadius = radius,
//            parentThickness = curvedRowChild.estimatedThickness,
//        )
//
//        val totalSweep = curvedRowChild.sweepRadians
//
//        // Apply anchor & anchorType
//        var layoutAngleStart = anchor.toRadians() -
//                (if (curvedLayoutDirection.clockwise()) anchorType.ratio else
//                    1f - anchorType.ratio) * totalSweep
//
//        curvedRowChild.angularPosition(layoutAngleStart, totalSweep, Offset(radius, radius))
//
//        // Place the composable children
//        layout(diameter, diameter) {
//            with(curvedRowChild) { placeIfNeeded() }
//        }
//    }
//}
//
//@JvmInline
//public value class AnchorType internal constructor(internal val ratio: Float) {
//    companion object {
//        /**
//         * Start the content of the [CurvedLayout] on the anchor
//         */
//        val Start = AnchorType(0f)
//
//        /**
//         * Center the content of the [CurvedLayout] around the anchor
//         */
//        val Center = AnchorType(0.5f)
//
//        /**
//         * End the content of the [CurvedLayout] on the anchor
//         */
//        val End = AnchorType(1f)
//    }
//
//    override fun toString(): String {
//        return when (this) {
//            Center -> "AnchorType.Center"
//            Start -> "AnchorType.Start"
//            End -> "AnchorType.End"
//            else -> "unknown"
//        }
//    }
//}
//
//public interface CurvedAlignment {
//    /**
//     * How to lay down components when they are thinner than the container. This is analogue of
//     * [Alignment.Vertical] in a [Row].
//     */
//    @kotlin.jvm.JvmInline
//    public value class Radial internal constructor(internal val ratio: Float) {
//        companion object {
//            /**
//             * Put the child closest to the center of the container, within the available space
//             */
//            val Inner = Radial(1f)
//
//            /**
//             * Put the child in the middle point of the available space.
//             */
//            val Center = Radial(0.5f)
//
//            /**
//             * Put the child farthest from the center of the container, within the available space
//             */
//            val Outer = Radial(0f)
//
//            /**
//             * Align the child in a custom position, 0 means Outer, 1 means Inner
//             */
//            fun Custom(ratio: Float): Radial {
//                return Radial(ratio)
//            }
//        }
//    }
//
//    /**
//     * How to lay down components when they have a smaller sweep than their container. This is
//     * analogue of [Alignment.Horizontal] in a [Column].
//     */
//    @kotlin.jvm.JvmInline
//    public value class Angular internal constructor(internal val ratio: Float) {
//        companion object {
//            /**
//             * Put the child at the angular start of the layout of the container, within the
//             * available space
//             */
//            val Start = Angular(0f)
//
//            /**
//             * Put the child in the middle point of the available space.
//             */
//            val Center = Angular(0.5f)
//
//            /**
//             * Put the child at the angular end of the layout of the container, within the available
//             * space
//             */
//            val End = Angular(1f)
//
//            /**
//             * Align the child in a custom position, 0 means Start, 1 means End
//             */
//            fun Custom(ratio: Float): Angular {
//                return Angular(ratio)
//            }
//        }
//    }
//}
