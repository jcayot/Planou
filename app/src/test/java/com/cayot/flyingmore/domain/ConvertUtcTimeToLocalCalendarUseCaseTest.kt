package com.cayot.flyingmore.domain

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.Calendar
import java.util.TimeZone

class ConvertUtcTimeToLocalCalendarUseCaseTest {

    private val useCase = ConvertUtcTimeToLocalCalendarUseCase()


    @Test
    fun testConvertUtcTimeToLocalCalendarUseCase_simpleCase() {
        val utcTime = 1696118400000L
        val timeZone = "Europe/Paris"

        val resultCalendar = useCase.invoke(utcTime, timeZone)

        val expectedCalendar = Calendar.getInstance(TimeZone.getTimeZone(timeZone)).apply {
            timeInMillis = utcTime
        }

        assertEquals(expectedCalendar.get(Calendar.YEAR), resultCalendar.get(Calendar.YEAR))
        assertEquals(expectedCalendar.get(Calendar.MONTH), resultCalendar.get(Calendar.MONTH))
        assertEquals(expectedCalendar.get(Calendar.DAY_OF_MONTH), resultCalendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(expectedCalendar.get(Calendar.HOUR_OF_DAY), resultCalendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(expectedCalendar.get(Calendar.MINUTE), resultCalendar.get(Calendar.MINUTE))
        assertEquals(expectedCalendar.timeZone, resultCalendar.timeZone)
    }

    @Test
    fun testConvertUtcTimeToLocalCalendarUseCase_differentTimeZone() {
        val utcTime = 1696118400000L
        val timeZone = "America/New_York"

        val resultCalendar = useCase.invoke(utcTime, timeZone)

        val expectedCalendar = Calendar.getInstance(TimeZone.getTimeZone(timeZone)).apply {
            timeInMillis = utcTime
        }

        assertEquals(expectedCalendar.get(Calendar.YEAR), resultCalendar.get(Calendar.YEAR))
        assertEquals(expectedCalendar.get(Calendar.MONTH), resultCalendar.get(Calendar.MONTH))
        assertEquals(expectedCalendar.get(Calendar.DAY_OF_MONTH), resultCalendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(expectedCalendar.get(Calendar.HOUR_OF_DAY), resultCalendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(expectedCalendar.get(Calendar.MINUTE), resultCalendar.get(Calendar.MINUTE))
        assertEquals(expectedCalendar.timeZone, resultCalendar.timeZone)
    }
}
