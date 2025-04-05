package com.cayot.flyingmore.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cayot.flyingmore.data.local.model.FlyingStatisticEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FlyingStatisticsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(flyingStatistic: FlyingStatisticEntity)

    @Update
    suspend fun update(flyingStatistic: FlyingStatisticEntity)

    @Query("SELECT * FROM `flying-statistics`")
    fun getAllFlyingStatistics() : Flow<List<FlyingStatisticEntity>>

    @Query("SELECT * FROM `flying-statistics` WHERE id = :id")
    fun getFlyingStatistic(id: Int) : Flow<FlyingStatisticEntity>

    @Query("SELECT * FROM 'flying-statistics' WHERE statisticTypeInt = :statisticTypeInt AND" +
            " timeFrameStartLong = :timeFrameStartLong AND" +
            " timeFrameEndLong = :timeFrameEndLong")
    suspend fun getFlyingStatisticEntity(statisticTypeInt: Int,
                                 timeFrameStartLong: Long,
                                 timeFrameEndLong: Long) : FlyingStatisticEntity?

    //TODO TEST
    @Query("SELECT * FROM `flying-statistics` WHERE :time BETWEEN timeFrameStartLong AND timeFrameEndLong")
    suspend fun getFlyingStatisticContainingTime(time: Long) : List<FlyingStatisticEntity>
}
