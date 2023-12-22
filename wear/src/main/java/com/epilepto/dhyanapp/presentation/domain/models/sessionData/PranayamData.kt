package com.epilepto.dhyanapp.presentation.domain.models.sessionData

data class PranayamData(
    var bpm: List<Float>?,
    var ms_bpm: List<Int>,
    var spo2: List<Float>?,
    var ms_spo2: List<Int>,
    var ax: List<Float>?,
    var ay: List<Float>?,
    var az: List<Float>?,
    var gx: List<Float>?,
    var gy: List<Float>?,
    var gz: List<Float>?,
    var arrayBreathWithBPM: List<String>?,
    var sessions: Int?,
    var IEratio: String?,
)