package com.cayot.flyingmore.domain

import com.cayot.flyingmore.data.DayDifference
import java.util.Calendar
import java.util.TimeZone

class CalendarFromDayDifferenceHourMinuteUseCase {

    operator fun invoke(calendar: Calendar, dayDifference: DayDifference, hour: Int, minute: Int, timeZone: String): Calendar {
        val newCalendar = calendar.clone() as Calendar
        newCalendar.set(Calendar.HOUR_OF_DAY, hour)
        newCalendar.set(Calendar.MINUTE, minute)
        newCalendar.timeZone = TimeZone.getTimeZone(timeZone)
        newCalendar.add(Calendar.DAY_OF_MONTH, dayDifference.value)
        return (newCalendar)
    }
}