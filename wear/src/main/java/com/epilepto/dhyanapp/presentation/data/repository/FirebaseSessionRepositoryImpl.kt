package com.epilepto.dhyanapp.presentation.data.repository

import android.util.Log
import com.epilepto.dhyanapp.presentation.data.utils.FirebaseConstants
import com.epilepto.dhyanapp.presentation.data.utils.MeditationAnalysis
import com.epilepto.dhyanapp.presentation.data.utils.PranayamAnalysis
import com.epilepto.dhyanapp.presentation.data.utils.UserSessionData
import com.epilepto.dhyanapp.presentation.domain.models.sessionData.MeditationData
import com.epilepto.dhyanapp.presentation.domain.models.sessionData.PranayamData
import com.epilepto.dhyanapp.presentation.domain.repository.FirebaseSessionRepository
import com.epilepto.dhyanapp.presentation.domain.models.sessionData.Score
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.time.Instant

class FirebaseSessionRepositoryImpl(): FirebaseSessionRepository {

    private val userRef = Firebase.firestore.collection("users")

    override suspend fun addMeditationAnalysisToFirebase(
        dhyan: Float,
        aasana: Float,
        prana: Float,
        duration: Float,
        bpm: List<Float>?,
        ms_bpm: List<Int>,
        spo2: List<Float>?,
        ms_spo2: List<Int>?,
        ax: List<Float>?,
        ay: List<Float>?,
        az: List<Float>?,
        gx: List<Float>?,
        gy: List<Float>?,
        gz: List<Float>?,
        userId:String?
    ):Result<String> {
        val currentTimeMillis = Instant.now().toEpochMilli()
        // creating a collection reference for our Firebase Firestore database.
        val type = "Meditation"  //Manually adding rn
        // adding our data to our scores object class.
        val score = Score(type ,currentTimeMillis, dhyan, aasana, prana, duration)
        val meditationData = MeditationData(
            calender = currentTimeMillis,
            bpm = bpm,
            ms_bpm = ms_bpm,
            spo2 = spo2,
            ms_spo2 = ms_spo2,
            ax = ax,
            ay = ay,
            az = az,
            gx = gx,
            gy = gy,
            gz = gz
        )

        val userSessionData = UserSessionData(
            meditationAnalysis = MeditationAnalysis(meditationData, score),
            pranayamAnalysis = null
        )

         if (userId != null) {
             return try {
                 userRef
                     .document(userId)
                     .collection(FirebaseConstants.SESSION_REF)
                     .add(userSessionData)
                     .await()
                 Result.success("Data added successfully")
             } catch (e: Exception) {
                 Log.e(this.javaClass.name, e.message, e)
                 Result.failure(e)
             }
        } else {
            Log.e(this.javaClass.name, "No User ID Found")
             return Result.failure(Exception("No User ID Found"))
        }
    }


    override suspend fun addPranayamAnalysisToFirebase(
        dhyan: Float,
        aasana: Float,
        prana: Float,
        duration: Float,
        bpm: List<Float>?,
        ms_bpm: List<Int>,
        spo2: List<Float>?,
        ms_spo2: List<Int>,
        ax: List<Float>?,
        ay: List<Float>?,
        az: List<Float>?,
        array_breathwithBPM: List<String>?,
        gx: List<Float>?,
        gy: List<Float>?,
        gz: List<Float>?,
        session: Int?,
        IEratio: String?,
        userId: String?
    ): Result<String> {
        Log.d(session.toString(), "Session")
        val currentTimeMillis = Instant.now().toEpochMilli()
        val type = "Pranayama"  //Manually adding rn

        val score = Score(type ,currentTimeMillis, dhyan, aasana, prana, duration)
        val pranayamData = PranayamData(
            bpm = bpm,
            ms_bpm = ms_bpm,
            spo2 = spo2,
            ms_spo2 = ms_spo2,
            ax = ax,
            ay = ay,
            az = az,
            gx = gx,
            gy = gy,
            gz = gz,
            arrayBreathWithBPM = array_breathwithBPM,
            sessions = session,
            IEratio = IEratio,
        )

        val userSessionData = UserSessionData(
            meditationAnalysis = null,
            pranayamAnalysis = PranayamAnalysis(pranayamData, score)
        )

        if (userId != null) {
           return try {
                userRef
                    .document(userId)
                    .collection(FirebaseConstants.SESSION_REF)
                    .add(userSessionData)
                    .await()
               Result.success("Data added successfully")
            } catch (e: Exception) {
                Log.e(this.javaClass.name, e.message, e)
               Result.failure(e)
            }
        } else {
            Log.e(this.javaClass.name, "No User ID Found")
            return Result.failure(Exception("No User Id Found"))
        }
    }
}



