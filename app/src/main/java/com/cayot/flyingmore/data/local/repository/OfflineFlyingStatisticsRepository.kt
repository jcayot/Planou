package com.cayot.flyingmore.data.local.repository

import com.cayot.flyingmore.data.local.dao.FlyingStatisticsDao
import com.cayot.flyingmore.data.model.statistics.DailyTemporalStatistic
import com.cayot.flyingmore.data.model.statistics.toTemporalStatistic
import com.cayot.flyingmore.data.repository.FlyingStatisticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OfflineFlyingStatisticsRepository(private val flyingStatisticsDao: FlyingStatisticsDao) : FlyingStatisticsRepository {
    override fun <T> getAllFlyingStatistics(): Flow<List<DailyTemporalStatistic<T>>> {
        return (flyingStatisticsDao.getAllFlyingStatistics().map { flyingStatisticsList ->
            flyingStatisticsList.map { it.toTemporalStatistic() }
        })
    }

    override fun <T> getFlyingStatistic(id: Int): Flow<DailyTemporalStatistic<T>> {
        return (flyingStatisticsDao.getFlyingStatistic(id).map { it.toTemporalStatistic() })
    }

    override suspend fun <T> insertFlyingStatistic(dailyTemporalStatistic: DailyTemporalStatistic<T>) {
        flyingStatisticsDao.insert(dailyTemporalStatistic.toFlyingStatisticEntity())
    }

    override suspend fun <T> updateFlyingStatistic(dailyTemporalStatistic: DailyTemporalStatistic<T>) {
        flyingStatisticsDao.update(dailyTemporalStatistic.toFlyingStatisticEntity())
    }
}