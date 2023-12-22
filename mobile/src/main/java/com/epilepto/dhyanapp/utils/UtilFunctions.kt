package com.epilepto.dhyanapp.utils

import co.yml.charts.common.model.Point
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object DateUtils {
    fun localDateToLong(localDate: LocalDate): Long {
        val referenceDate = LocalDate.of(1970, 1, 1) // Unix epoch start date
        val seconds = localDate.atStartOfDay()
            .toEpochSecond(ZoneOffset.UTC) - referenceDate.atStartOfDay()
            .toEpochSecond(ZoneOffset.UTC)
        return seconds * 1000L // Convert seconds to milliseconds
    }

    fun localDateToString(localDate: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return localDate.format(formatter)
    }

    fun formatToSessionCardDate(time:Long): String {
        val date = Date(time)
        val formatter = SimpleDateFormat("dd MMM yyyy, h:mm a", Locale.ENGLISH)
        return formatter.format(date)
    }

    fun longToLocalDate(time: Long): LocalDate {
        val instant = Instant.ofEpochMilli(time)
        return instant.atZone(ZoneOffset.UTC).toLocalDate()
    }

    fun epochToFormattedDate(time: Long): String {
        val format = DateTimeFormatter.ofPattern("dd MMM yyyy")
        return longToLocalDate(time).format(format)
    }
}

fun List<Point>.normalizeTime(): List<Point> {
    if(this.isEmpty()) return this

    val maxVal = this.maxByOrNull { it.x }?.x
    val minVal = this.minByOrNull { it.x }?.x

    return if (minVal != null && maxVal != null) {
        this.map { point ->
            val newX = (point.x - minVal) / (maxVal - minVal)
            Point(newX*10, point.y)
        }
    } else this
}

data class NotificationData(
    val data: Notification,
    val token: String
)

data class Notification(
    val title: String,
    val body: String
)

fun Double.format(digits: Int) = "%.${digits}f".format(this)
