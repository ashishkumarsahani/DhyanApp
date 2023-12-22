package com.epilepto.dhyanapp.data.repository

import com.epilepto.dhyanapp.data.utils.Response
import com.epilepto.dhyanapp.model.session.BPMPoints
import com.epilepto.dhyanapp.model.session.SessionScoreData
import com.epilepto.dhyanapp.model.session.UserSessionData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseSessionRepository {

    private val firestore = Firebase.firestore
    private val sessionRef = firestore
        .collection("users")
        .document("SqHoD4BHVKVgrT1bnOnRfeawVDk2")
        .collection("Sessions")


   /* fun getSessionData(): Flow<Response<List<SessionScoreData>>> = flow {
        emit(Response.Loading())
        try {
            val sessionData = sessionRef.get().await().documents.mapNotNull { doc ->
                doc.toSessionData()
            }
            emit(Response.Success(sessionData))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: "Oops! An unknown error occurred"))
        }
    }

    */

    fun getSessionData():Flow<Response<List<UserSessionData>>> = flow {
        emit(Response.Loading())
        try {
            val sessionData = sessionRef.get().await().toObjects(UserSessionData::class.java)
            val meditationData = sessionData.mapNotNull { it.meditationAnalysis }
            val pranayamData = sessionData.mapNotNull { it.pranayamAnalysis }


            emit(Response.Success(sessionData))
        } catch (e:Exception) {
            emit(Response.Error(e.message ?: "Oops! An unknown error occurred"))
        }
    }

    fun getBPM(): Flow<Response<BPMPoints>> = flow {
        emit(Response.Loading())
        try {
            val sessionData = sessionRef.get().await().toObjects(UserSessionData::class.java)
            val meditationData = sessionData.mapNotNull { it.meditationAnalysis }

/*            val points = meditationData.mapNotNull { it.meditationData }
            val bpm = points.map { it.bpm }
            val time = it["ms_bpm"] as ArrayList<Double>

            BPMPoints(bpm.map { list -> list.toFloat() }, time.map { list -> list.toFloat() })

            */

        emit(Response.Success(BPMPoints()))
    } catch (e:Exception)
    {
        emit(Response.Error(e.message ?: "Oops! An unknown error occurred"))
    }

}

private fun DocumentSnapshot.toSessionData(): SessionScoreData? {
    return this.data?.let { data ->
        SessionScoreData(
            asanaScore = data["aasana"] as Double? ?: 0.0,
            dhyanScore = data["dhyan"] as Double? ?: 0.0,
            sessionDate = data["calender"] as Long? ?: 0L,
            duration = data["duration"] as Double? ?: 0.0
        )
    }

}
}