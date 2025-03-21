package com.cayot.flyingmore.workers

import androidx.work.Data
import com.cayot.flyingmore.data.model.statistics.enums.TimeFrame
import java.time.LocalDate
import java.time.Year

fun Data.getIntOrThrow(key: String, errorValue: Int = -1) : Int {
    val value = this.getInt(key, errorValue)
    if (value == errorValue)
        throw IllegalArgumentException("Key $key is mandatory")
    return (value)
}

fun getDateRange(inputData: Data, dataTimeFrame: TimeFrame) : Pair<LocalDate, LocalDate> {
    var minDepartureTime: LocalDate
    var maxDepartureTime: LocalDate

    //Extract year for all time frame
    val statisticYearInt = inputData.getIntOrThrow(YEAR_KEY)

    minDepartureTime = Year.of(statisticYearInt).atMonth(1).atDay(1)
    maxDepartureTime = Year.of(statisticYearInt).plusYears(1).atMonth(1).atDay(1)

    //Handle weekly time frame
    if (dataTimeFrame == TimeFrame.WEEK) {
        val statisticWeekInt = inputData.getIntOrThrow(WEEK_KEY)

        minDepartureTime = minDepartureTime.plusWeeks(statisticWeekInt.toLong())
        minDepartureTime = minDepartureTime.minusDays((minDepartureTime.dayOfWeek.value - 1).toLong())
        maxDepartureTime = minDepartureTime.plusWeeks(1L)
        return minDepartureTime to maxDepartureTime
    }

    //Handle more specific time frame
    if (dataTimeFrame <= TimeFrame.MONTH) {
        val statisticMonthInt = inputData.getIntOrThrow(MONTH_KEY)

        minDepartureTime = minDepartureTime.withMonth(statisticMonthInt)
        maxDepartureTime = minDepartureTime.plusMonths(1)
    }
    if (dataTimeFrame <= TimeFrame.DAY) {
        val statisticDayInt = inputData.getIntOrThrow(DAY_KEY)

        minDepartureTime = minDepartureTime.withDayOfMonth(statisticDayInt)
        maxDepartureTime = minDepartureTime.plusDays(1)
    }
    return minDepartureTime to maxDepartureTime
}