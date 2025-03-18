package com.cayot.flyingmore.fake.data.local.dao

import com.cayot.flyingmore.data.local.dao.FlyingStatisticsDao
import com.cayot.flyingmore.data.local.model.FlyingStatisticEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeFlyingStatisticsDao : FlyingStatisticsDao {
    private val _statistics = MutableStateFlow<List<FlyingStatisticEntity>>(emptyList())
    private val statistics: Flow<List<FlyingStatisticEntity>> = _statistics

    override suspend fun insert(flyingStatistic: FlyingStatisticEntity) {
        _statistics.update {
            it + flyingStatistic.copy(id = (it.maxOfOrNull { it.id } ?: 0) + 1)
        }
    }

    override suspend fun update(flyingStatistic: FlyingStatisticEntity) {
        _statistics.update { list ->
            list.map {
                if (it.id == flyingStatistic.id)
                    flyingStatistic
                else
                    it
            }
        }
    }

    override fun getAllFlyingStatistics(): Flow<List<FlyingStatisticEntity>> {
        return (statistics)
    }

    override fun getFlyingStatistic(id: Int): Flow<FlyingStatisticEntity> {
        return (statistics.map { list ->
            list.first { it.id == id }
        })
    }

    override fun getFlyingStatisticEntity(
        statisticTypeInt: Int,
        timeFrameStartLong: Long,
        timeFrameEndLong: Long
    ): FlyingStatisticEntity? {
        return _statistics.value.firstOrNull{
            it.statisticTypeInt == statisticTypeInt &&
                    it.timeFrameStartLong == timeFrameStartLong &&
                    it.timeFrameEndLong == timeFrameEndLong
        }
    }
}
