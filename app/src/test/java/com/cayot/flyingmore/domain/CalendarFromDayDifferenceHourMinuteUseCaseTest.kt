package com.cayot.flyingmore.domain

import com.cayot.flyingmore.data.model.DayDifference
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.Calendar
import java.util.TimeZone

class CalendarFromDayDifferenceHourMinuteUseCaseTest {

    private val useCase = CalendarFromDayDifferenceHourMinuteUseCase()

    @Test
    fun testCalendarFromDayDifferenceHourMinuteUseCase_addsDayDifferenceAndSetsTime() {
        val originalCalendar = Calendar.getInstance().apply {
            set(2023, Calendar.OCTOBER, 1, 0, 0, 0)
        }
        val dayDifference = DayDifference.TWO
        val hour = 14
        val minute = 30
        val timeZone = "GMT"

        val resultCalendar = useCase(originalCalendar, dayDifference, hour, minute, timeZone)

        assertEquals(2023, resultCalendar.get(Calendar.YEAR))
        assertEquals(Calendar.OCTOBER, resultCalendar.get(Calendar.MONTH))
        assertEquals(3, resultCalendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(14, resultCalendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(30, resultCalendar.get(Calendar.MINUTE))
        assertEquals(TimeZone.getTimeZone(timeZone), resultCalendar.timeZone)
    }

    @Test
    fun testCalendarFromDayDifferenceHourMinuteUseCase_addsDayDifferenceChangeMonth() {
        val originalCalendar = Calendar.getInstance().apply {
            set(2023, Calendar.OCTOBER, 30, 0, 0, 0)
        }
        val dayDifference = DayDifference.TWO
        val hour = 14
        val minute = 30
        val timeZone = "GMT"

        val resultCalendar = useCase(originalCalendar, dayDifference, hour, minute, timeZone)

        assertEquals(2023, resultCalendar.get(Calendar.YEAR))
        assertEquals(Calendar.NOVEMBER, resultCalendar.get(Calendar.MONTH))
        assertEquals(1, resultCalendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(14, resultCalendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(30, resultCalendar.get(Calendar.MINUTE))
        assertEquals(TimeZone.getTimeZone(timeZone), resultCalendar.timeZone)
    }

    @Test
    fun testCalendarFromDayDifferenceHourMinuteUseCase_handlesTimeZoneChange() {
        val originalCalendar = Calendar.getInstance().apply {
            set(2023, Calendar.OCTOBER, 1, 0, 0, 0)
        }
        val dayDifference = DayDifference.ONE
        val hour = 18
        val minute = 0
        val timeZone = "CET"

        val resultCalendar = useCase(originalCalendar, dayDifference, hour, minute, timeZone)

        assertEquals(2023, resultCalendar.get(Calendar.YEAR))
        assertEquals(Calendar.OCTOBER, resultCalendar.get(Calendar.MONTH))
        assertEquals(2, resultCalendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(18, resultCalendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(0, resultCalendar.get(Calendar.MINUTE))
        assertEquals(TimeZone.getTimeZone(timeZone), resultCalendar.timeZone)
    }
}
