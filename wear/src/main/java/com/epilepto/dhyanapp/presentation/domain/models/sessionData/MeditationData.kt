package com.epilepto.dhyanapp.presentation.domain.models.sessionData

data class MeditationData(
    val calender: Long? = 0,
    var bpm: List<Float>?,
    var ms_bpm: List<Int>,
    var spo2: List<Float>?,
    var ms_spo2: List<Int>?,
    var ax: List<Float>?,
    var ay: List<Float>?,
    var az: List<Float>?,
    var gx: List<Float>?,
    var gy: List<Float>?,
    var gz: List<Float>?,
)
