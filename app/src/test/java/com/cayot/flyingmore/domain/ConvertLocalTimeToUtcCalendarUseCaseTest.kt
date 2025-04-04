package com.cayot.flyingmore.domain

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Calendar
import java.util.TimeZone

class ConvertLocalTimeToUtcCalendarUseCaseTest {

    private val useCase = ConvertLocalTimeToUtcCalendarUseCase()
    @Test
    fun testConvertLocalTimeToCalendarUseCase_simpleCase() {
        val date = 1696118400000L
        val hour = 14
        val minute = 30
        val timeZone = "Europe/Paris"

        val resultCalendar = useCase.invoke(date, hour, minute, timeZone)

        val expectedZonedDateTime = ZonedDateTime.of(2023, 10, 1, hour, minute, 0, 0, ZoneId.of(timeZone))
        val expectedCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            timeInMillis = expectedZonedDateTime.toInstant().toEpochMilli()
        }

        assertEquals(expectedCalendar.get(Calendar.YEAR), resultCalendar.get(Calendar.YEAR))
        assertEquals(expectedCalendar.get(Calendar.MONTH), resultCalendar.get(Calendar.MONTH))
        assertEquals(expectedCalendar.get(Calendar.DAY_OF_MONTH), resultCalendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(expectedCalendar.get(Calendar.HOUR_OF_DAY), resultCalendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(expectedCalendar.get(Calendar.MINUTE), resultCalendar.get(Calendar.MINUTE))
        assertEquals(expectedCalendar.timeZone, resultCalendar.timeZone)
    }

    @Test
    fun testConvertLocalTimeToCalendarUseCase_handlesDifferentTimeZone() {
        val date = 1696118400000L
        val hour = 8
        val minute = 45
        val timeZone = "America/New_York"

        val resultCalendar = useCase.invoke(date, hour, minute, timeZone)

        val expectedZonedDateTime = ZonedDateTime.of(2023, 10, 1, hour, minute, 0, 0, ZoneId.of(timeZone))
        val expectedCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            timeInMillis = expectedZonedDateTime.toInstant().toEpochMilli()
        }

        assertEquals(expectedCalendar.get(Calendar.YEAR), resultCalendar.get(Calendar.YEAR))
        assertEquals(expectedCalendar.get(Calendar.MONTH), resultCalendar.get(Calendar.MONTH))
        assertEquals(expectedCalendar.get(Calendar.DAY_OF_MONTH), resultCalendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(expectedCalendar.get(Calendar.HOUR_OF_DAY), resultCalendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(expectedCalendar.get(Calendar.MINUTE), resultCalendar.get(Calendar.MINUTE))
        assertEquals(expectedCalendar.timeZone, resultCalendar.timeZone)
    }
}
