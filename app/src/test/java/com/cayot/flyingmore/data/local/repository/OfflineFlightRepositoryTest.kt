package com.cayot.flyingmore.data.local.repository

import com.cayot.flyingmore.data.model.Flight
import com.cayot.flyingmore.data.model.TravelClass
import com.cayot.flyingmore.data.model.toFlightBrief
import com.cayot.flyingmore.data.model.toFlightEntity
import com.cayot.flyingmore.fake.data.local.dao.FakeFlightDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class OfflineFlightRepositoryTest {

    private lateinit var repository: OfflineFlightRepository
    private val flightDao = FakeFlightDao()

    private val flight1 = Flight.getPlaceholder1()
    private val flight2 = Flight.getPlaceholder2()

    @Before
    fun setUp() {
        repository = OfflineFlightRepository(flightDao)
    }

    @Test
    fun getAllFlightBriefsStream_returnsAllFlightBriefs() = runBlocking {
        flightDao.insert(flight1.toFlightEntity())
        flightDao.insert(flight2.toFlightEntity())

        val flightBriefs = repository.getAllFlightBriefsStream().first()

        assertEquals(2, flightBriefs.size)
    }

    @Test
    fun getAllFlightForDepartureTimeRange_returnsFlightsInRange() = runBlocking {
        flightDao.insert(flight1.toFlightEntity())

        val startTime = flight1.departureTime.timeInMillis - 1000
        val endTime = flight1.departureTime.timeInMillis + 1000

        flightDao.insert(flight2.toFlightEntity().copy(
            departureTime = startTime - 1000
        ))

        val flightsInRange = repository.getAllFlightForDepartureTimeRange(startTime, endTime)

        assertEquals(1, flightsInRange.size)
    }

    @Test
    fun getFlight_returnsCorrectFlight() = runBlocking {
        flightDao.insert(flight1.toFlightEntity())

        val retrievedFlight = repository.getFlight(1).first()

        assertEquals(flight1.flightNumber, retrievedFlight.flightNumber)
    }

    @Test
    fun insertFlight_insertsSuccessfully() = runBlocking {
        repository.insertFlight(flight1)

        val allFlights = flightDao.getAllFlightBriefs().first().map { it.toFlightBrief() }
        assertEquals(1, allFlights.size)
    }

    @Test
    fun deleteFlightById_deletesSuccessfully() = runBlocking {
        flightDao.insert(flight1.toFlightEntity())

        repository.deleteFlightById(1)

        val allFlights = flightDao.getAllFlightBriefs().first().map { it.toFlightBrief() }
        assertEquals(0, allFlights.size)
    }

    @Test
    fun updateFlightUpgradeSuccessfully() = runBlocking {
        flightDao.insert(flight1.toFlightEntity())
        val updatedFlight = flight1.copy(
            id = 1,
            seatNumber = "1A",
            travelClass = TravelClass.FIRST)

        repository.updateFlight(updatedFlight)

        val retrievedFlight = repository.getFlight(1).first()
        assertEquals(updatedFlight.seatNumber, retrievedFlight.seatNumber)
        assertEquals(updatedFlight.travelClass, retrievedFlight.travelClass)
    }
}
