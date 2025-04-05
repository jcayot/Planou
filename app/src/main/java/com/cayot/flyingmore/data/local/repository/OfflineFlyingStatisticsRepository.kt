package com.cayot.flyingmore.data.local.repository

import com.cayot.flyingmore.data.local.dao.FlyingStatisticsDao
import com.cayot.flyingmore.data.model.statistics.DailyTemporalStatistic
import com.cayot.flyingmore.data.model.statistics.toTemporalStatistic
import com.cayot.flyingmore.data.repository.FlyingStatisticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneOffset

class OfflineFlyingStatisticsRepository(private val flyingStatisticsDao: FlyingStatisticsDao) : FlyingStatisticsRepository {
    override fun getAllFlyingStatistics(): Flow<List<DailyTemporalStatistic<Any>>> {
        return (flyingStatisticsDao.getAllFlyingStatistics().map { flyingStatisticsList ->
            flyingStatisticsList.map { it.toTemporalStatistic() }
        })
    }

    override fun getFlyingStatistic(id: Int): Flow<DailyTemporalStatistic<Any>> {
        return (flyingStatisticsDao.getFlyingStatistic(id).map { it.toTemporalStatistic() })
    }

    //TODO Tests
    override suspend fun getFlyingStatisticContainingTime(time: Long): List<DailyTemporalStatistic<Any>> {
        return (flyingStatisticsDao.getFlyingStatisticContainingTime(time).map { it.toTemporalStatistic() })
    }

    override suspend fun getFlyingStatistic(
        statisticTypeInt: Int,
        timeFrameStart: LocalDate,
        timeFrameEnd: LocalDate
    ): DailyTemporalStatistic<Any>? {
        return (flyingStatisticsDao.getFlyingStatisticEntity(statisticTypeInt,
            timeFrameStart.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(),
            timeFrameEnd.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
        )?.toTemporalStatistic())
    }

    //TODO Potential cast problems ???
    override suspend fun <T> insertFlyingStatistic(dailyTemporalStatistic: DailyTemporalStatistic<T>) {
        val alreadyExisting = getFlyingStatistic(
            statisticTypeInt = dailyTemporalStatistic.statisticType.ordinal,
            timeFrameStart = dailyTemporalStatistic.timeFrameStart,
            timeFrameEnd = dailyTemporalStatistic.timeFrameEnd
        ) as DailyTemporalStatistic<T>?
        if (alreadyExisting == null)
            flyingStatisticsDao.insert(dailyTemporalStatistic.toFlyingStatisticEntity())
        else
            updateFlyingStatistic(alreadyExisting.copy(data = dailyTemporalStatistic.data))
    }

    override suspend fun <T> updateFlyingStatistic(dailyTemporalStatistic: DailyTemporalStatistic<T>) {
        flyingStatisticsDao.update(dailyTemporalStatistic.toFlyingStatisticEntity())
    }

    override suspend fun <T> deleteFlyingStatistic(dailyTemporalStatistic: DailyTemporalStatistic<T>) {
        flyingStatisticsDao.delete(dailyTemporalStatistic.toFlyingStatisticEntity())
    }
}