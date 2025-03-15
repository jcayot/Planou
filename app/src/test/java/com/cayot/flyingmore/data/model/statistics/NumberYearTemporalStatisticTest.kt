package com.cayot.flyingmore.data.model.statistics

import com.cayot.flyingmore.data.model.statistics.enums.FlyingStatistic
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class NumberYearTemporalStatisticTest {

    private val testStatistic: NumberDailyTemporalStatistic = NumberDailyTemporalStatistic(
        timeFrameStart = LocalDate.of(2023, 1, 1),
        timeFrameEnd = LocalDate.of(2024, 1, 1),
        data = List(
            365,
            init = {1}
        ),
        statisticType = FlyingStatistic.NUMBER_OF_FLIGHT
    )

    //Trivial here but useful for other statistics class
    @Test
    fun sumDate_test() {
        val summedData = testStatistic.sumData(testStatistic.data)
        assertEquals(summedData, ChronoUnit.DAYS.between(testStatistic.timeFrameStart, testStatistic.timeFrameEnd).toInt())
    }
}