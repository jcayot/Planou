package com.cayot.planou.data.airport

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cayot.planou.data.PlanouDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class AirportDaoTest {
    private lateinit var airportDao: AirportDao
    private lateinit var planouDatabase: PlanouDatabase

    private val airportList = listOf(Airport.getCDG(), Airport.getHEL(), Airport.getJFK())

    @Before
    fun createDatabase() {
        val context: Context = ApplicationProvider.getApplicationContext()

        planouDatabase = Room.inMemoryDatabaseBuilder(context, PlanouDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        airportDao = planouDatabase.airportDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        planouDatabase.close()
    }

    private suspend fun fillDatabase() {
        airportDao.insertAll(airportList)
    }

    @Test
    fun getAirportByIataCodeTest() = runBlocking {
        fillDatabase()
        val cdg = airportDao.getAirportByIataCode(Airport.getCDG().iataCode)
        assertEquals(cdg!!.name, Airport.getCDG().name)
    }

    @Test
    fun searchAirportByFullNameTest() = runBlocking {
        fillDatabase()
        val foundAirports = airportDao.searchAirportsByFullName("C", 5)
        assertEquals(foundAirports.size, 1)
        assertEquals(foundAirports.first().iataCode, Airport.getCDG().iataCode)
    }

    @Test
    fun searchAirportByIataCodeTest() = runBlocking {
        fillDatabase()
        val foundAirports = airportDao.searchAirportsByIataCode("JF", 5)
        assertEquals(foundAirports.size, 1)
        assertEquals(foundAirports.first().name, Airport.getJFK().name)
    }
}