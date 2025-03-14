package com.cayot.flyingmore.data.model.statistics

import com.cayot.flyingmore.data.model.statistics.enums.ChartType
import com.cayot.flyingmore.data.model.statistics.enums.Resolution
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.Year

class MapStringNumberTemporalStatisticTest {

    val testStatistic: MapStringNumberTemporalStatistic = MapStringNumberTemporalStatistic(
        name = "Test Statistic 1",
        year = Year.of(2023),
        dataResolution = Resolution.DAILY,
        defaultDisplayResolution = Resolution.MONTHLY,
        allowedDisplayResolutions = listOf(Resolution.DAILY, Resolution.WEEKLY, Resolution.MONTHLY),
        data = List<Map<String, Int>>(
            365,
            init = { mapOf("CDG" to 3, "AMS" to 2) }
        ),
        chartType = ChartType.BAR_GRAPH,
        unit = "meters"
    )

    @Test
    fun sumData_test() {
        val summedData = testStatistic.sumData(testStatistic.data)
        assertEquals(summedData.size, 2)
        assertEquals(summedData["CDG"], (if (testStatistic.year.isLeap) 366 else 365) * 3)
        assertEquals(summedData["AMS"], (if (testStatistic.year.isLeap) 366 else 365) * 2)
    }
}