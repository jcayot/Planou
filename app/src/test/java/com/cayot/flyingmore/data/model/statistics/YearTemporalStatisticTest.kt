package com.cayot.flyingmore.data.model.statistics

import com.cayot.flyingmore.data.model.statistics.enums.ChartType
import com.cayot.flyingmore.data.model.statistics.enums.Resolution
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Year

class YearTemporalStatisticTest {

    private val testStatistic: YearTemporalStatistic<Int> = NumberYearTemporalStatistic(
        name = "Test Statistic 1",
        year = Year.of(2023),
        dataResolution = Resolution.DAILY,
        defaultDisplayResolution = Resolution.MONTHLY,
        allowedDisplayResolutions = listOf(Resolution.DAILY, Resolution.WEEKLY, Resolution.MONTHLY),
        data = List(
            365,
            init = {1}
        ),
        chartType = ChartType.BAR_GRAPH,
        unit = "meters"
    )

    @Test
    fun getResolution_dailyToWeekly() {
        val weekly = testStatistic.getResolution(resolution = Resolution.WEEKLY)
        assertEquals(53, weekly.size)
        assertEquals(1, weekly[0])
        for (i in 1..51)
            assertEquals(7, weekly[i])
        assertEquals(7, weekly[52])
    }

    @Test
    fun getResolution_dailyToMonthly() {
        val monthly = testStatistic.getResolution(resolution = Resolution.MONTHLY)
        assertEquals(12, monthly.size)
        for (currentMonth in 1..12)
            assertEquals(testStatistic.year.atMonth(currentMonth).lengthOfMonth(), monthly[currentMonth - 1])
    }

    @Test
    fun getResolution_dailyToYearly() {
        val yearly = testStatistic.getResolution(resolution = Resolution.YEARLY)
        assertEquals(1, yearly.size)
        assertEquals(yearly[0], if (testStatistic.year.isLeap) 366 else 365 )
    }
}