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
        name = "Statistic 1",
        year = 2022,
        dataResolutionInt = 1080,
        defaultDisplayResolutionInt = 720,
        allowedDisplayResolutionsJson = """["1080", "720", "480"]""",
        dataJson = """{"data": [1, 2, 3]}""",
        dataTypeInt = 1,
        chartTypeInt = 2,
        unit = "meters"
    )

    val flyingStatistic2 = FlyingStatisticEntity(
        name = "Statistic 2",
        year = 2023,
        dataResolutionInt = 1440,
        defaultDisplayResolutionInt = 1080,
        allowedDisplayResolutionsJson = """["1440", "1080", "720"]""",
        dataJson = """{"data": [4, 5, 6, 7]}""",
        dataTypeInt = 2,
        chartTypeInt = 1,
        unit = "kilometers"
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
        val updatedFlyingStatistic = flyingStatisticsDao.getFlyingStatistic(1).first().copy(year = 2024)

        flyingStatisticsDao.update(updatedFlyingStatistic)

        val retrievedFlyingStatistic = flyingStatisticsDao.getFlyingStatistic(updatedFlyingStatistic.id).first()
        assertEquals(updatedFlyingStatistic.year, retrievedFlyingStatistic.year)
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