package com.cayot.flyingmore.data.airport

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cayot.flyingmore.data.DatabaseTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AirportDaoTest : DatabaseTest() {
    private lateinit var airportDao: AirportDao

    private val airportList = listOf(Airport.getCDG(), Airport.getHEL(), Airport.getJFK())

    @Before
    override fun createDatabase() {
        super.createDatabase()
        airportDao = flyingMoreDatabase.airportDao()
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