package com.cayot.flyingmore.data.local.dao

import com.cayot.flyingmore.data.local.model.Airport
import com.cayot.flyingmore.data.model.Flight
import com.cayot.flyingmore.data.model.toFlightEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class FlightDaoTest: DaoTestBase() {
    private lateinit var flightDao: FlightDao
    private lateinit var airportDao: AirportDao

    @Before
    fun createDao() {
        flightDao = database.flightDao()
        airportDao = database.airportDao()
    }

    val flight1 = Flight.getPlaceholder1()
    val flight2 = Flight.getPlaceholder2()

    private suspend fun fillDatabase() {
        airportDao.insertAll(
            listOf(Airport.getCDG(), Airport.getHEL(), Airport.getJFK())
        )
        flightDao.insert(flight1.toFlightEntity())
        flightDao.insert(flight2.toFlightEntity())
    }

    @Test
    fun insert_flight_insertsSuccessfully() = runBlocking {
        fillDatabase()
        val allFlights = flightDao.getAllFlightBriefs().first()
        assertEquals(2, allFlights.size)
    }
}