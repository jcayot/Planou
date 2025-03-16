package com.cayot.flyingmore.data.local.dao

import com.cayot.flyingmore.data.local.model.FlyingStatisticEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class FlyingStatisticsDaoTest : DaoTestBase() {
    private lateinit var flyingStatisticsDao: FlyingStatisticsDao

    @Before
    fun createDao() {
        flyingStatisticsDao = database.flyingStatisticsDao()
    }

    val flyingStatistic1 = FlyingStatisticEntity(
        timeFrameStartLong = System.nanoTime(),
        timeFrameEndLong = System.nanoTime(),
        dataJson = """{"data": [1, 2, 3]}""",
        statisticTypeInt = 1
    )

    val flyingStatistic2 = FlyingStatisticEntity(
        timeFrameStartLong = System.nanoTime(),
        timeFrameEndLong = System.nanoTime(),
        dataJson = """{"data": [1, 2, 3]}""",
        statisticTypeInt = 2
    )

    private fun addTwoEntity() {
        runBlocking {
            flyingStatisticsDao.insert(flyingStatistic1)
            flyingStatisticsDao.insert(flyingStatistic2)
        }
    }

    @Test
    fun insert_flyingStatistic_insertsSuccessfully() = runBlocking {
        addTwoEntity()
        val allFlyingStatistics = flyingStatisticsDao.getAllFlyingStatistics().first()
        assertEquals(2, allFlyingStatistics.size)
    }

    @Test
    fun update_flyingStatistic_updatesSuccessfully() = runBlocking {
        addTwoEntity()
        val updatedFlyingStatistic = flyingStatisticsDao.getFlyingStatistic(1).first().copy(dataJson = flyingStatistic2.dataJson)

        flyingStatisticsDao.update(updatedFlyingStatistic)

        val retrievedFlyingStatistic = flyingStatisticsDao.getFlyingStatistic(updatedFlyingStatistic.id).first()
        assertEquals(updatedFlyingStatistic, retrievedFlyingStatistic)
    }

    @Test
    fun getFlyingStatistic_withExistingId_returnsStatistic() = runBlocking {
        addTwoEntity()

        val retrievedFlyingStatistic = flyingStatisticsDao.getFlyingStatistic(1).first()

        assertEquals(flyingStatistic1.copy(id = retrievedFlyingStatistic.id), retrievedFlyingStatistic)
    }

    @Test
    fun getFlyingStatistic_withNonExistingId_returnsNull() = runBlocking {
        val retrievedFlyingStatistic = flyingStatisticsDao.getFlyingStatistic(100).first()

        assertNull(retrievedFlyingStatistic)
    }
}