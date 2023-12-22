package com.epilepto.dhyanapp.model.session

data class MeditationData(
    val calender: Long? = 0,
    var duration: Float? = 0f,
    var dhyan: Float? = 0f,
    var aasana: Float? = 0f,
    var prana: Float? = 0f,
    var bpm: List<Float>? = null,
    var ms_bpm: List<Float>? = null,
    var spo2: List<Float>? = null,
    var ax: List<Float>? = null,
    var ay: List<Float>? = null,
    var az: List<Float>? = null,
    var gx: List<Float>? = null,
    var gy: List<Float>? = null,
    var gz: List<Float>? = null
)
