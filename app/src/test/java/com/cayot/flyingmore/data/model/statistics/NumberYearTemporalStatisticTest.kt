package com.cayot.flyingmore.data.model.statistics

import com.cayot.flyingmore.data.model.statistics.enums.ChartType
import com.cayot.flyingmore.data.model.statistics.enums.Resolution
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.Year

class NumberYearTemporalStatisticTest {

    private val testStatistic: NumberYearTemporalStatistic = NumberYearTemporalStatistic(
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

    //Trivial here but useful for other statistics class
    @Test
    fun sumDate_test() {
        val summedData = testStatistic.sumData(testStatistic.data)
        assertEquals(summedData, if (testStatistic.year.isLeap) 366 else 365)
    }
}