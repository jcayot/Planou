package com.cayot.flyingmore.data.repository

import com.cayot.flyingmore.data.model.statistics.YearTemporalStatistic
import kotlinx.coroutines.flow.Flow

interface FlyingStatisticsRepository {

    fun <T> getAllFlyingStatistics() : Flow<List<YearTemporalStatistic<T>>>

    fun <T> getFlyingStatistic(id: Int) : Flow<YearTemporalStatistic<T>>

    suspend fun <T> insertFlyingStatistic(yearTemporalStatistic: YearTemporalStatistic<T>)

    suspend fun <T> updateFlyingStatistic(yearTemporalStatistic: YearTemporalStatistic<T>)
}