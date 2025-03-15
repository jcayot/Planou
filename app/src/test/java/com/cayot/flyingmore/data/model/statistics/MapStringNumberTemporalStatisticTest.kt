package com.cayot.flyingmore.data.model.statistics

import com.cayot.flyingmore.data.model.statistics.enums.FlyingStatistic
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class MapStringNumberTemporalStatisticTest {

    val testStatistic: MapStringNumberDailyTemporalStatistic = MapStringNumberDailyTemporalStatistic(
        timeFrameStart = LocalDate.of(2023, 1, 1),
        timeFrameEnd = LocalDate.of(2024, 1, 1),
        data = List<Map<String, Int>>(
            365,
            init = { mapOf("CDG" to 3, "AMS" to 2) }
        ),
        statisticType = FlyingStatistic.NUMBER_OF_FLIGHT
    )

    @Test
    fun sumData_test() {
        val summedData = testStatistic.sumData(testStatistic.data)
        assertEquals(summedData.size, 2)
        assertEquals(summedData["CDG"],
            ((ChronoUnit.DAYS.between(testStatistic.timeFrameStart, testStatistic.timeFrameEnd)) * 3).toInt())
        assertEquals(summedData["AMS"],
            ((ChronoUnit.DAYS.between(testStatistic.timeFrameStart, testStatistic.timeFrameEnd)) * 2).toInt())
    }
}