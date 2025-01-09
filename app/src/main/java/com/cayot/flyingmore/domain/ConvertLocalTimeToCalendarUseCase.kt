package com.cayot.flyingmore.domain

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Calendar
import java.util.TimeZone

class ConvertLocalTimeToCalendarUseCase {

    operator fun invoke(date: Long, hour: Int, minute: Int, timeZone: String): Calendar {
        val instant = Instant.ofEpochMilli(date)
        val zoneId = ZoneId.of(timeZone)
        val zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId)
            .withHour(hour)
            .withMinute(minute)
            .withSecond(0)
            .withNano(0)

        val utcZonedDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"))
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = utcZonedDateTime.toInstant().toEpochMilli()

        return (calendar)
    }
}