package com.epilepto.dhyanapp.presentation.domain.repository

import android.content.Context

interface FirebaseSessionRepository {

    suspend fun addMeditationAnalysisToFirebase(
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
    ):Result<String>

    suspend fun addPranayamAnalysisToFirebase(
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
    ):Result<String>
}