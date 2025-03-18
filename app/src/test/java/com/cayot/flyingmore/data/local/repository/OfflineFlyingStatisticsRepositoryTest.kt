package com.cayot.flyingmore.data.local.repository

import com.cayot.flyingmore.data.model.statistics.NumberDailyTemporalStatistic
import com.cayot.flyingmore.data.model.statistics.enums.FlyingStatistic
import com.cayot.flyingmore.fake.data.local.dao.FakeFlyingStatisticsDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class OfflineFlyingStatisticsRepositoryTest {

    private val repository = OfflineFlyingStatisticsRepository(
        flyingStatisticsDao = FakeFlyingStatisticsDao()
    )

    private val temporalStatistic1 = NumberDailyTemporalStatistic(
        timeFrameStart = LocalDate.of(2023, 1, 1),
        timeFrameEnd = LocalDate.of(2024, 1, 1),
        data = List(
            365,
            init = {0}
        ),
        statisticType = FlyingStatistic.NUMBER_OF_FLIGHT
    )

    private val temporalStatistic2 = NumberDailyTemporalStatistic(
        timeFrameStart = LocalDate.of(2024, 1, 1),
        timeFrameEnd = LocalDate.of(2025, 1, 1),
        data = List(
            366,
            init = {0}
        ),
        statisticType = FlyingStatistic.NUMBER_OF_FLIGHT
    )

    @Test
    fun insertFlyingStatistic_insertsSuccessfully() = runBlocking {
        repository.insertFlyingStatistic(temporalStatistic1)

        val allStatistics = repository.getAllFlyingStatistics().first()
        assertEquals(1, allStatistics.size)
    }

    @Test
    fun updateFlyingStatistic_updatesSuccessfully() = runBlocking {
        repository.insertFlyingStatistic(temporalStatistic1)
        val updatedTemporalStatistic: NumberDailyTemporalStatistic =
            (repository.getFlyingStatistic(1).first() as NumberDailyTemporalStatistic)
                .copy(data = List(
                    365, {5}
                ))

        repository.updateFlyingStatistic(updatedTemporalStatistic)

        val retrievedStatistic = repository.getFlyingStatistic(1).first() as NumberDailyTemporalStatistic
        assertEquals(updatedTemporalStatistic.data, retrievedStatistic.data)
    }

    @Test
    fun getAllFlyingStatistics_returnsAllStatistics() = runBlocking {
        repository.insertFlyingStatistic(temporalStatistic1)
        repository.insertFlyingStatistic(temporalStatistic2)

        val allStatistics = repository.getAllFlyingStatistics().first()

        assertEquals(2, allStatistics.size)
        assertEquals(temporalStatistic1.timeFrameStart, allStatistics[0].timeFrameStart)
        assertEquals(temporalStatistic2.timeFrameStart, allStatistics[1].timeFrameStart)
    }

    @Test
    fun getFlyingStatistic_withExistingId_returnsStatistic() = runBlocking {
        repository.insertFlyingStatistic(temporalStatistic1)

        val retrievedStatistic = repository.getFlyingStatistic(1).first()

        assertEquals(temporalStatistic1.timeFrameStart, retrievedStatistic.timeFrameStart)
    }
}