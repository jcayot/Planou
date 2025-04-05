package com.cayot.flyingmore.workers

import androidx.work.Data
import com.cayot.flyingmore.data.model.statistics.enums.FlyingStatistic
import com.cayot.flyingmore.data.model.statistics.enums.TimeFrame
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import java.time.LocalDate

class WorkersUtilsTest {

    private fun buildInputData(
        year: Int,
        month: Int? = null,
        day: Int? = null,
        week: Int? = null
    ): Data {
        val builder = Data.Builder().putInt(YEAR_KEY, year)
        month?.let { builder.putInt(MONTH_KEY, it) }
        day?.let { builder.putInt(DAY_KEY, it) }
        week?.let { builder.putInt(WEEK_KEY, it) }
        return builder.build()
    }

    @Test
    fun getDateRange_yearly() {
        val inputData = buildInputData(2023)
        val result = getDateRange(inputData, TimeFrame.YEAR)
        assertEquals(LocalDate.of(2023, 1, 1), result.first)
        assertEquals(LocalDate.of(2024, 1, 1), result.second)
    }

    @Test
    fun getDateRange_monthly() {
        val inputData = buildInputData(2023, month = 5)
        val result = getDateRange(inputData, TimeFrame.MONTH)
        assertEquals(LocalDate.of(2023, 5, 1), result.first)
        assertEquals(LocalDate.of(2023, 6, 1), result.second)
    }

    @Test
    fun getDateRange_weekly() {
        val inputData = buildInputData(2023, week = 10)
        val result = getDateRange(inputData, TimeFrame.WEEK)
        assertEquals(LocalDate.of(2023, 3, 6), result.first)
        assertEquals(LocalDate.of(2023, 3, 13), result.second)
    }

    @Test
    fun getDateRange_daily() {
        val inputData = buildInputData(2023, month = 7, day = 15)
        val result = getDateRange(inputData, TimeFrame.DAY)
        assertEquals(LocalDate.of(2023, 7, 15), result.first)
        assertEquals(LocalDate.of(2023, 7, 16), result.second)
    }

    @Test
    fun getDateRange_missingKey_throwsException() {
        val inputData = Data.Builder().build()
        assertThrows(IllegalArgumentException::class.java) {
            getDateRange(inputData, TimeFrame.YEAR)
        }
    }

    @Test
    fun getStatisticDateRangeForTime_yearly() {
        val departureTime = LocalDate.of(2023, 8, 20)
        val statistic = FlyingStatistic.NUMBER_OF_FLIGHT
        val result = getStatisticDateRangeForTime(departureTime, statistic)
        assertEquals(LocalDate.of(2023, 1, 1), result.first)
        assertEquals(LocalDate.of(2024, 1, 1), result.second)
    }
}