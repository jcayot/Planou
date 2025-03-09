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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(flyingStatistic: FlyingStatisticEntity)

    @Update
    suspend fun update(flyingStatistic: FlyingStatisticEntity)

    @Query("SELECT * FROM `flying-statistics`")
    fun getAllFlyingStatistics() : Flow<List<FlyingStatisticEntity>>

    @Query("SELECT * FROM `flying-statistics` WHERE name = :name")
    fun getFlyingStatistic(name: String) : Flow<FlyingStatisticEntity>
}