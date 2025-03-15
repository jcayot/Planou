package com.cayot.flyingmore.data.repository

import com.cayot.flyingmore.data.model.statistics.DailyTemporalStatistic
import kotlinx.coroutines.flow.Flow

interface FlyingStatisticsRepository {

    fun <T> getAllFlyingStatistics() : Flow<List<DailyTemporalStatistic<T>>>

    fun <T> getFlyingStatistic(id: Int) : Flow<DailyTemporalStatistic<T>>

    suspend fun <T> insertFlyingStatistic(dailyTemporalStatistic: DailyTemporalStatistic<T>)

    suspend fun <T> updateFlyingStatistic(dailyTemporalStatistic: DailyTemporalStatistic<T>)
}