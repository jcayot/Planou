package com.cayot.flyingmore.data.model.statistics

import com.cayot.flyingmore.data.model.statistics.enums.FlyingStatistic
import com.cayot.flyingmore.data.model.statistics.enums.TimeFrame
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.Year

class YearTemporalStatisticTest {

    private val testStatistic: DailyTemporalStatistic<Int> = NumberDailyTemporalStatistic(
        timeFrameStart = LocalDate.of(2023, 1, 1),
        timeFrameEnd = LocalDate.of(2024, 1, 1),
        data = List(
            365,
            init = {1}
        ),
        statisticType = FlyingStatistic.NUMBER_OF_FLIGHT
    )

    @Test
    fun getResolution_dailyToWeekly() {
        val weekly = testStatistic.getResolution(resolution = TimeFrame.WEEK)
        assertEquals(53, weekly.size)
        assertEquals(1, weekly[0])
        for (i in 1..51)
            assertEquals(7, weekly[i])
        assertEquals(7, weekly[52])
    }

    @Test
    fun getResolution_dailyToMonthly() {
        val year = Year.from(testStatistic.timeFrameStart)
        val monthly = testStatistic.getResolution(resolution = TimeFrame.MONTH)
        assertEquals(12, monthly.size)
        for (currentMonth in 1..12)
            assertEquals(year.atMonth(currentMonth).lengthOfMonth(), monthly[currentMonth - 1])
    }

    @Test
    fun getResolution_dailyToYearly() {
        val year = Year.from(testStatistic.timeFrameStart)
        val yearly = testStatistic.getResolution(resolution = TimeFrame.YEAR)
        assertEquals(1, yearly.size)
        assertEquals(yearly[0], if (year.isLeap) 366 else 365 )
    }
}