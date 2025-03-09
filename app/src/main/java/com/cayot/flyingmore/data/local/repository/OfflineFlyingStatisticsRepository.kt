package com.cayot.flyingmore.data.local.repository

import com.cayot.flyingmore.data.local.dao.FlyingStatisticsDao
import com.cayot.flyingmore.data.model.statistics.YearTemporalStatistic
import com.cayot.flyingmore.data.model.statistics.toFlyingStatisticEntity
import com.cayot.flyingmore.data.model.statistics.toTemporalStatistic
import com.cayot.flyingmore.data.repository.FlyingStatisticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OfflineFlyingStatisticsRepository(private val flyingStatisticsDao: FlyingStatisticsDao) : FlyingStatisticsRepository {
    override fun <T> getAllFlyingStatistics(): Flow<List<YearTemporalStatistic<T>>> {
        return (flyingStatisticsDao.getAllFlyingStatistics().map { flyingStatisticsList ->
            flyingStatisticsList.map { it.toTemporalStatistic() }
        })
    }

    override fun <T> getFlyingStatistic(name: String): Flow<YearTemporalStatistic<T>> {
        return (flyingStatisticsDao.getFlyingStatistic(name).map { it.toTemporalStatistic() })
    }

    override suspend fun <T> insertFlyingStatistic(yearTemporalStatistic: YearTemporalStatistic<T>) {
        flyingStatisticsDao.insert(yearTemporalStatistic.toFlyingStatisticEntity())
    }

    override suspend fun <T> updateFlyingStatistic(yearTemporalStatistic: YearTemporalStatistic<T>) {
        flyingStatisticsDao.update(yearTemporalStatistic.toFlyingStatisticEntity())
    }
}