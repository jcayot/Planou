package com.cayot.flyingmore.domain

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.Calendar
import java.util.TimeZone

class ConvertLocalTimeToUtcCalendarUseCase {

    operator fun invoke(date: Long, hour: Int, minute: Int, timeZone: String): Calendar {
        val dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneOffset.UTC)
            .withHour(hour)
            .withMinute(minute)

        val zonedDateTime = ZonedDateTime.of(dateTime.year, dateTime.monthValue, dateTime.dayOfMonth,
            dateTime.hour, dateTime.minute, 0, 0, ZoneId.of(timeZone))

        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = zonedDateTime.toInstant().toEpochMilli()

        return (calendar)
    }
}