package com.cayot.flyingmore.domain

import com.cayot.flyingmore.data.model.statistics.DailyTemporalStatistic
import com.cayot.flyingmore.data.model.statistics.enums.FlyingStatistic
import com.cayot.flyingmore.data.repository.FlyingStatisticsRepository
import com.cayot.flyingmore.data.repository.GenerateFlyingStatisticRepository
import kotlinx.coroutines.flow.Flow

//TODO Temporary Use case. Flying statistics won't be generated that way in the future
class GenerateAndGetAllFlyingStatisticsUseCase(
    private val flyingStatisticsRepository: FlyingStatisticsRepository,
    private val generateFlyingStatisticRepository: GenerateFlyingStatisticRepository
) {
    operator fun invoke(year: Int): Flow<List<DailyTemporalStatistic<Any>>> {
        generateFlyingStatisticRepository.generateFlyingStatistics(
            statisticsToGenerate = FlyingStatistic.entries,
            year = year
        )
        return flyingStatisticsRepository.getAllFlyingStatistics()
    }
}