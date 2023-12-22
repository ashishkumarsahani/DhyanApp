
import android.content.Context
import android.media.MediaPlayer
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.CurvedLayout
import androidx.wear.compose.foundation.CurvedModifier
import androidx.wear.compose.foundation.CurvedTextStyle
import androidx.wear.compose.foundation.basicCurvedText
import androidx.wear.compose.foundation.padding
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.epilepto.dhyanapp.R
import com.epilepto.dhyanapp.presentation.presentation.utils.ProgressIndicatorSegment
import com.epilepto.dhyanapp.presentation.presentation.utils.SegmentedProgressIndicator
import kotlin.math.pow

@Composable
fun Light_SW_UI(IEratio: String, totalTime: Int, pranayamaSession:Int, timeRemainingInSeconds:Int) :String {
    val (x, y) = IEratio.split(":").map { it.toFloat() }
    val totalAnimationTime = x + y

    val innerProgress = remember { Animatable(0f) }
    val outerProgressSpec: AnimationSpec<Float> = TweenSpec(durationMillis = totalTime * 1000)
    val outerProgress by animateFloatAsState(targetValue = 1f, animationSpec = outerProgressSpec)
    var cycleCount by remember { mutableStateOf(0) }
        var sessionsLeft by remember { mutableStateOf(1f) }  //number of session remaining scaled between (0 to 1)
    if (((timeRemainingInSeconds) % totalAnimationTime).toInt() == 0) {
        sessionsLeft = ((timeRemainingInSeconds / totalAnimationTime)/pranayamaSession).toFloat()
    }
    val meditation by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.meditation))

    var segment = List(pranayamaSession) { ProgressIndicatorSegment(1f, Color.Green) }

    var breathState by remember { mutableStateOf("Inhale") }
    var currentTime by remember { mutableStateOf(0f) }

    val context = LocalContext.current

    var playTrackOne by remember { mutableStateOf(true) } // This boolean determines which track to play
    var mediaPlayer: MediaPlayer? = null


    val vibrator = LocalContext.current.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    val timing = longArrayOf(50,150,50,150,50,150,50,150,50,150,50,150,50,150,50,150,50,150,50,150,50,150,50,150,50,150,50,600,50,800,50,1000)
    val amplitude = intArrayOf(100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0)
    vibrator.vibrate(VibrationEffect.createWaveform(timing, amplitude, -1))

    if(IEratio == "3:6"){
        val timing = longArrayOf(50,150,50,150,50,150,50,150,50,150,50,150,50,150,50,150,50,150,50,150,50,150,50,150,50,150,50,600,50,800,50)
        val amplitude = intArrayOf(100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0)
    }
    else if(IEratio == "4:8"){
        val timing = longArrayOf(50,150,50,150,50,150,50,150,50,150,50,150,50,150,50,150,50,150,50,150,50,150,50,150,50,150,50,600,50,800,50,1000)
        val amplitude = intArrayOf(100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0)
    }

    else{
        val timing = longArrayOf(50,150,50,150,50,150,50,150,50,150,50,150,50,150,50,150,50,150,50,150,50,150,50,150,50,150,50,600,50,800,50,1000)
        val amplitude = intArrayOf(100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0,100,0)
    }

    LaunchedEffect(playTrackOne) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(
            context,
            if (playTrackOne) R.raw.inhale else R.raw.exhale // Select track based on boolean value
        )
        mediaPlayer?.start()
    }

    LaunchedEffect(key1 = IEratio) {
        while (currentTime < totalTime) {
            playTrackOne = true
            breathState = "Inhale"
            innerProgress.animateTo(
                targetValue = 1f,
                animationSpec = TweenSpec(durationMillis = (x * 1000).toInt())
            )
            currentTime += x
            if (currentTime < totalTime) {
                playTrackOne = false
                breathState = "Exhale"
                innerProgress.animateTo(
                    targetValue = 0f,
                    animationSpec = TweenSpec(durationMillis = (y * 1000).toInt())
                )
                currentTime += y
            }
        }
    }
        SegmentedProgressIndicator(
            trackSegments = segment,
            progress = sessionsLeft,
            modifier = Modifier.fillMaxSize(),
            strokeWidth = 10.dp,
            paddingAngle = 2f,
            trackColor = Color.DarkGray
        )

        androidx.wear.compose.material.CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 13.dp),
            startAngle = 295.5f,
            endAngle = 245.5f,
            progress = innerProgress.value, // Ensure progress is within [0, 1]
            strokeWidth = 10.dp,
            indicatorColor = Color(0xFFFFBD48),
            trackColor = Color.DarkGray
        )
    Box(modifier = Modifier.fillMaxSize()){
        LottieAnimation(
            composition = meditation,
            progress = (innerProgress.value)*0.5f,
        )

        // Align the Column to the bottom of the Box
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter) // Align the column to the bottom center
                .fillMaxWidth() // Fill the maximum width of the Box
                .padding(25.dp), // Add some padding at the bottom
            horizontalAlignment = Alignment.CenterHorizontally // Center the children horizontally
        ) {
            CurvedLayout(modifier = Modifier.fillMaxSize()) {
                basicCurvedText(
                    breathState,
                    CurvedModifier.padding(10.dp),
                    style = {
                        CurvedTextStyle(
                            fontSize = 12.sp,
                            color = Color(0xFFFFBD48),
                            background = Color.Black
                        )
                    }
                )
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        }
    }
    return breathState
}


fun generateVibrationPattern(inhaleSeconds: Int, exhaleSeconds: Int): Pair<LongArray, IntArray> {
    val inhaleDuration = inhaleSeconds * 1000L // Convert to milliseconds
    val exhaleDuration = exhaleSeconds * 1000L

    // Determine the number of segments based on the minimum segment length
    val minSegmentLength = 10L
    val maxSegmentLength = 25L
    val totalSegments = ((inhaleDuration + exhaleDuration) / ((minSegmentLength + maxSegmentLength) / 2)).toInt()

    val timings = LongArray(totalSegments)
    val amplitudes = IntArray(totalSegments)

    // Calculate segment durations
    val segmentDuration = (inhaleDuration + exhaleDuration) / totalSegments

    for (i in 0 until totalSegments) {
        timings[i] = segmentDuration

        val progress = if (i < totalSegments / 2) {
            // Inhale phase
            i.toDouble() / (totalSegments / 2)
        } else {
            // Exhale phase
            1.0 - (i - totalSegments / 2).toDouble() / (totalSegments / 2)
        }

        // Non-linear amplitude increase (quadratic)
        amplitudes[i] = (150 * progress.pow(2)).toInt()
    }

    return Pair(timings, amplitudes)
}









