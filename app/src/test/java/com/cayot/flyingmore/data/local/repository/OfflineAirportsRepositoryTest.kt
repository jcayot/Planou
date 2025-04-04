package com.cayot.flyingmore.data.local.repository

import com.cayot.flyingmore.data.local.model.Airport
import com.cayot.flyingmore.fake.data.local.dao.FakeAirportDao
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class OfflineAirportsRepositoryTest {

    private lateinit var repository: OfflineAirportsRepository
    private val airportsDao = FakeAirportDao()

    private val airports = listOf(
        Airport.getCDG().copy(id = 1),
        Airport.getJFK().copy(id = 2),
        Airport.getHEL().copy(id = 3)
    )

    @Before
    fun setUp() {
        repository = OfflineAirportsRepository(airportsDao)
    }

    @Test
    fun insertAllAirports_insertsSuccessfully() = runBlocking {
        repository.insertAllAirports(airports)

        val insertedAirportCount = airportsDao.getSize()
        assertEquals(airports.size, insertedAirportCount)
    }

    @Test
    fun getAirport_returnsCorrectAirport() = runBlocking {
        repository.insertAllAirports(airports)

        val retrievedAirport = repository.getAirport(airports[0].id)

        assertEquals(airports[0], retrievedAirport)
    }

    @Test
    fun getAirport_returnsNullForNonExistentAirport() = runBlocking {
        val retrievedAirport = repository.getAirport(999)

        assertNull(retrievedAirport)
    }

    @Test
    fun getAirportByIataCode_returnsCorrectAirport() = runBlocking {
        repository.insertAllAirports(airports)

        val retrievedAirport = repository.getAirportByIataCode(airports[1].iataCode)

        assertEquals(airports[1], retrievedAirport)
    }

    @Test
    fun getAirportByIataCode_returnsNullForNonExistentIataCode() = runBlocking {
        val retrievedAirport = repository.getAirportByIataCode("XXX")

        assertNull(retrievedAirport)
    }

    @Test
    fun searchAirportsByFullName_returnsMatchingAirports() = runBlocking {
        repository.insertAllAirports(airports)

        val results = repository.searchAirportsByFullName("Charles de Gaulle", 10)

        assertEquals(1, results.size)
        assertEquals(airports[0].id, results[0].id)
    }

    @Test
    fun searchAirportsByIataCode_returnsMatchingAirports() = runBlocking {
        repository.insertAllAirports(airports)

        val results = repository.searchAirportsByIataCode("JFK", 10)

        assertEquals(1, results.size)
        assertEquals(airports[1].id, results[0].id)
    }
}