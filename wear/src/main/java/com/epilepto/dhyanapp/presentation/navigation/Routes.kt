package com.epilepto.dhyanapp.presentation.navigation

import android.util.Log
import com.epilepto.dhyanapp.presentation.domain.models.sensor.SensorData

sealed class Routes(val route: String) {
    data object Main : Routes("main_screen")
    data object Permission : Routes("permission_screen")
    data object MeditationSession : Routes("meditation_session")
    data object MeditationTimer : Routes("meditation_timer/{session}"){
        fun passArguments(session: String) = "meditation_timer/{$session}"
    }
    data object PranayamSession : Routes("pranayam_session")
    data object TimerScreen : Routes("timer_screen")
    data object SessionRecords : Routes("records_screen")
    data object SplashScreen : Routes("splash_screen")
    data object UserInfo : Routes("user_info")

    data object MeditationTimerAnimation: Routes(
        route = "timer_animation/{time}/{session}"
    ){
        fun passArguments(time:Int,session:String ) = "timer_animation/${time}/${session}"
    }

    data object PranayamTimerAnimation: Routes(
        route = "pranayam_timer_animation/{pranayamaMode}/{pranayamaSession}/{IEratio}"
    ){
        fun passArguments(pranayamaMode:String,pranayamaSession:Int, IEratio:String ) = "pranayam_timer_animation/${pranayamaMode}/${pranayamaSession}/${IEratio}"
    }

    data object MeditationStopwatch : Routes(
        route = "stopwatch/{time}/{session}"
    ) {
        fun passArguments(time: Int, session: String) = "stopwatch/$time/$session"
    }


    data object MeditationTest : Routes(
        route = "Test/{time}/{session}"
    ) {
        fun passArguments(time: Int, session: String) = "Test/$time/$session"
    }





    data object PranayamTimer: Routes(
        route = "pranayam_timer/{pranayamaMode}/{pranayamaSession}"
    ){
        fun passArguments(pranayamaMode: String,pranayamaSession: Int) = "pranayam_timer/$pranayamaMode/$pranayamaSession"
    }
    data object PranayamLightModeIESelection :
        Routes("pranayam_light_mode_IE_selection_screen/{pranayamaMode}/{pranayamaSession}") {
        fun passArguments(pranayamaMode: String, pranayamaSession: Int): String {
            return "pranayam_light_mode_IE_selection_screen/$pranayamaMode/$pranayamaSession"
        }
    }


    data object PranayamLightModeStopwatch: Routes(
        route = "pranayam_stopwatch_light_mode/{pranayamaMode}/{pranayamaSession}/{IEratio}"
    ){
        fun passArguments(pranayamaMode:String,pranayamaSession: Int,IEratio:String) = "pranayam_stopwatch_light_mode/${pranayamaMode}/${pranayamaSession}/${IEratio}"
    }
    data object PranayamBalancedModeStopwatch: Routes(
        route = "pranayam_stopwatch_balanced_mode/{pranayamaMode}/{pranayamaSession}/{IEratio}"
    ){
        fun passArguments(pranayamaMode:String,pranayamaSession: Int,IEratio:String) = "pranayam_stopwatch_balanced_mode/${pranayamaMode}/${pranayamaSession}/${IEratio}"
    }


    data object PranayamBalancedModeIESelection :
        Routes("pranayam_balanced_mode_IE_selection_screen/{pranayamaMode}/{pranayamaSession}") {
        fun passArguments(pranayamaMode: String, pranayamaSession: Int): String {
            return "pranayam_balanced_mode_IE_selection_screen/$pranayamaMode/$pranayamaSession"
        }
    }

    data object MeditationSessionScore : Routes(
        route = "meditation_session_score"
    ) {
        var duration = 0f
        var dhyanScore = 0f
        var asanaScore = 0f
        var pranaScore = 0f
        var arrayBpm = ArrayList<Float>()
        var arrayMsBpm =  ArrayList<Int>()
        var arraySPO2 =  ArrayList<Float>()
        var arrayMsSPO2 =  ArrayList<Int>()
        var arrayAx =  ArrayList<Float>()
        var arrayAy =  ArrayList<Float>()
        var arrayAz =  ArrayList<Float>()
        var arrayGx =  ArrayList<Float>()
        var arrayGy =  ArrayList<Float>()
        var arrayGz =  ArrayList<Float>()
        var arrayInhaleExhale =  ArrayList<String>()
        var breaths = 0
        var IEratio = ""
        var pranayamSession = 0
        fun passSensorData(sensorData: SensorData): String {
            duration = sensorData.duration
            dhyanScore = sensorData.dhyanScore
            asanaScore = sensorData.asanaScore
            pranaScore = sensorData.pranaScore
            arrayBpm = sensorData.arrayBpm
            arrayMsBpm = sensorData.arrayMsBpm
            arraySPO2 = sensorData.arraySPO2
            arrayMsSPO2 = sensorData.arrayMsSPO2
            arrayAx = sensorData.arrayAx
            arrayAy = sensorData.arrayAy
            arrayAz = sensorData.arrayAz
            arrayGx = sensorData.arrayGx
            arrayGy = sensorData.arrayGy
            arrayGz = sensorData.arrayGz
            arrayInhaleExhale = sensorData.arrayInhaleExhale
            breaths = sensorData.breaths
            IEratio = sensorData.IEratio
            pranayamSession = sensorData.pranayamSession
//            Log.d("", "passSensorData: ${sensorData.arrayBpm[6]}")
            Log.d("$pranayamSession", "Meditation Sensor Data is Updated")
//            return "meditation_session_score/${sensorData.duration}/${sensorData.dhyanScore}/${sensorData.asanaScore}/${sensorData.pranaScore}/${sensorData.arrayBpm}/${sensorData.arrayMsBpm}/${sensorData.arrayAx}/${sensorData.arrayAy}/${sensorData.arrayAz}/${sensorData.arrayGx}/${sensorData.arrayGy}/${sensorData.arrayGz}"
            return "meditation_session_score"
        }
    }

    data object PranayamSessionScore : Routes(
        route = "pranayam_session_score",
    ) {
        var duration = 0f
        var dhyanScore = 0f
        var asanaScore = 0f
        var pranaScore = 0f
        var arrayBpm = ArrayList<Float>()
        var arrayMsBpm = ArrayList<Int>()
        var arraySPO2 = ArrayList<Float>()
        var arrayMsSPO2 = ArrayList<Int>()
        var arrayAx = ArrayList<Float>()
        var arrayAy = ArrayList<Float>()
        var arrayAz = ArrayList<Float>()
        var arrayGx = ArrayList<Float>()
        var arrayGy = ArrayList<Float>()
        var arrayGz = ArrayList<Float>()
        var arrayInhaleExhale = ArrayList<String>()
        var breaths = 0
        var IEratio = ""
        var pranayamSession = 0
        fun passSensorData(sensorData: SensorData): String {
            duration = sensorData.duration
            dhyanScore = sensorData.dhyanScore
            asanaScore = sensorData.asanaScore
            pranaScore = sensorData.pranaScore
            arrayBpm = sensorData.arrayBpm
            arrayMsBpm = sensorData.arrayMsBpm
            arraySPO2 = sensorData.arraySPO2
            arrayMsSPO2 = sensorData.arrayMsSPO2
            arrayAx = sensorData.arrayAx
            arrayAy = sensorData.arrayAy
            arrayAz = sensorData.arrayAz
            arrayGx = sensorData.arrayGx
            arrayGy = sensorData.arrayGy
            arrayGz = sensorData.arrayGz
            arrayInhaleExhale = sensorData.arrayInhaleExhale
            breaths = sensorData.breaths
            IEratio = sensorData.IEratio
            pranayamSession = sensorData.pranayamSession
            Log.d("$pranayamSession", "Pranayam Sensor Data is Updated")
            return "pranayam_session_score"
        }
    }

    data object SessionRecordsDetails: Routes(
        "session_details",
    )
}
