package com.cayot.flyingmore.data.local.repository

import com.cayot.flyingmore.data.local.dao.FlyingStatisticsDao
import com.cayot.flyingmore.data.model.statistics.TemporalStatistic
import com.cayot.flyingmore.data.model.statistics.toFlyingStatisticEntity
import com.cayot.flyingmore.data.model.statistics.toTemporalStatistic
import com.cayot.flyingmore.data.repository.FlyingStatisticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OfflineFlyingStatisticsRepository(private val flyingStatisticsDao: FlyingStatisticsDao) : FlyingStatisticsRepository {
    override fun getAllFlyingStatistics(): Flow<List<TemporalStatistic>> {
        return (flyingStatisticsDao.getAllFlyingStatistics().map { flyingStatisticsList ->
            flyingStatisticsList.map { it.toTemporalStatistic() }
        })
    }

    override fun getFlyingStatistic(name: String): Flow<TemporalStatistic> {
        return (flyingStatisticsDao.getFlyingStatistic(name).map { it.toTemporalStatistic() })
    }

    override suspend fun insertFlyingStatistic(temporalStatistic: TemporalStatistic) {
        flyingStatisticsDao.insert(temporalStatistic.toFlyingStatisticEntity())
    }

    override suspend fun updateFlyingStatistic(temporalStatistic: TemporalStatistic) {
        flyingStatisticsDao.update(temporalStatistic.toFlyingStatisticEntity())
    }
}