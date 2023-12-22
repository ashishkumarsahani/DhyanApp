package com.epilepto.dhyanapp.model.session

data class Score(
    val calender: Long? = 0,
    var dhyan: Float? = 0f,
    var aasana: Float? = 0f,
    var prana: Float? = 0f,
    var duration: Float? = 0f,
    var type: String? = ""
)
