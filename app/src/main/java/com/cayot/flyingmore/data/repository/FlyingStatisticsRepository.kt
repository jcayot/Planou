package com.cayot.flyingmore.data.repository

import com.cayot.flyingmore.data.model.statistics.TemporalStatistic
import kotlinx.coroutines.flow.Flow

interface FlyingStatisticsRepository {

    fun getAllFlyingStatistics() : Flow<List<TemporalStatistic>>

    fun getFlyingStatistic(name: String) : Flow<TemporalStatistic>

    suspend fun insertFlyingStatistic(temporalStatistic: TemporalStatistic)

    suspend fun updateFlyingStatistic(temporalStatistic: TemporalStatistic)
}