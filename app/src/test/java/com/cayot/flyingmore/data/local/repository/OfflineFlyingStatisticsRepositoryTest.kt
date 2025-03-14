package com.cayot.flyingmore.data.local.repository

import com.cayot.flyingmore.data.model.statistics.NumberYearTemporalStatistic
import com.cayot.flyingmore.data.model.statistics.enums.ChartType
import com.cayot.flyingmore.data.model.statistics.enums.Resolution
import com.cayot.flyingmore.fake.data.local.dao.FakeFlyingStatisticsDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Year

class OfflineFlyingStatisticsRepositoryTest {

    private val repository = OfflineFlyingStatisticsRepository(
        flyingStatisticsDao = FakeFlyingStatisticsDao()
    )

    private val temporalStatistic1 = NumberYearTemporalStatistic(
        name = "Test Statistic 1",
        year = Year.of(2023),
        dataResolution = Resolution.DAILY,
        defaultDisplayResolution = Resolution.MONTHLY,
        allowedDisplayResolutions = listOf(Resolution.DAILY, Resolution.WEEKLY, Resolution.MONTHLY),
        data = List(
            365,
            init = {0}
        ),
        chartType = ChartType.BAR_GRAPH,
        unit = "meters"
    )

    private val temporalStatistic2 = NumberYearTemporalStatistic(
        name = "Test Statistic 2",
        year = Year.of(2024),
        dataResolution = Resolution.DAILY,
        defaultDisplayResolution = Resolution.MONTHLY,
        allowedDisplayResolutions = listOf(Resolution.DAILY, Resolution.WEEKLY, Resolution.MONTHLY),
        data = List(
            366,
            init = {0}
        ),
        chartType = ChartType.PIE_CHART,
        unit = "kilometers"
    )

    @Test
    fun insertFlyingStatistic_insertsSuccessfully() = runBlocking {
        repository.insertFlyingStatistic(temporalStatistic1)

        val allStatistics = repository.getAllFlyingStatistics<Int>().first()
        assertEquals(1, allStatistics.size)
    }

    @Test
    fun updateFlyingStatistic_updatesSuccessfully() = runBlocking {
        repository.insertFlyingStatistic(temporalStatistic1)
        val updatedTemporalStatistic: NumberYearTemporalStatistic =
            (repository.getFlyingStatistic<Int>(1).first() as NumberYearTemporalStatistic)
                .copy(data = List(
                    365, {5}
                ))

        repository.updateFlyingStatistic(updatedTemporalStatistic)

        val retrievedStatistic = repository.getFlyingStatistic<Int>(1).first() as NumberYearTemporalStatistic
        assertEquals(updatedTemporalStatistic.data, retrievedStatistic.data)
    }

    @Test
    fun getAllFlyingStatistics_returnsAllStatistics() = runBlocking {
        repository.insertFlyingStatistic(temporalStatistic1)
        repository.insertFlyingStatistic(temporalStatistic2)

        val allStatistics = repository.getAllFlyingStatistics<Int>().first()

        assertEquals(2, allStatistics.size)
        assertEquals(temporalStatistic1.name, allStatistics[0].name)
        assertEquals(temporalStatistic2.name, allStatistics[1].name)
    }

    @Test
    fun getFlyingStatistic_withExistingId_returnsStatistic() = runBlocking {
        repository.insertFlyingStatistic(temporalStatistic1)

        val retrievedStatistic = repository.getFlyingStatistic<Int>(1).first()

        assertEquals(temporalStatistic1.name, retrievedStatistic.name)
    }
}