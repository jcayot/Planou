package com.cayot.flyingmore.data.local.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cayot.flyingmore.data.local.model.Airport
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AirportDaoTest : DaoTestBase() {
    private lateinit var airportDao: AirportDao

    private val airportList = listOf(Airport.getCDG(), Airport.getHEL(), Airport.getJFK())

    @Before
    fun createDao() {
        airportDao = database.airportDao()
    }

    private suspend fun fillDatabase() {
        airportDao.insertAll(airportList)
    }

    @Test
    fun getAirportByIataCodeTest() = runBlocking {
        fillDatabase()
        val cdg = airportDao.getAirportByIataCode(Airport.getCDG().iataCode)
        assertEquals(cdg!!, Airport.getCDG().copy(id = cdg.id))
    }

    @Test
    fun searchAirportByFullNameTest() = runBlocking {
        fillDatabase()
        val foundAirports = airportDao.searchAirportsByFullName("C", 5)
        assertEquals(foundAirports.size, 1)
        assertEquals(foundAirports.first(), Airport.getCDG().copy(id = foundAirports.first().id))
    }

    @Test
    fun searchAirportByIataCodeTest() = runBlocking {
        fillDatabase()
        val foundAirports = airportDao.searchAirportsByIataCode("JF", 5)
        assertEquals(foundAirports.size, 1)
        assertEquals(foundAirports.first(), Airport.getJFK().copy(id = foundAirports.first().id))
    }

    @Test
    fun getAirportByIdTest() = runBlocking {
        fillDatabase()
        val cdgByIata = airportDao.getAirportByIataCode(Airport.getCDG().iataCode)
        val cdgById = airportDao.getAirport(cdgByIata!!.id)
        assertEquals(cdgById!!, Airport.getCDG().copy(id = cdgById.id))
    }
}