package com.cayot.flyingmore.data.repository

import com.cayot.flyingmore.data.model.statistics.DailyTemporalStatistic
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface FlyingStatisticsRepository {

    fun getAllFlyingStatistics() : Flow<List<DailyTemporalStatistic<Any>>>

    fun getFlyingStatistic(id: Int) : Flow<DailyTemporalStatistic<Any>>

    suspend fun getFlyingStatistic(
        statisticTypeInt: Int,
        timeFrameStart: LocalDate,
        timeFrameEnd: LocalDate
    ) : DailyTemporalStatistic<Any>?

    suspend fun <T> insertFlyingStatistic(dailyTemporalStatistic: DailyTemporalStatistic<T>)

    suspend fun <T> updateFlyingStatistic(dailyTemporalStatistic: DailyTemporalStatistic<T>)
}