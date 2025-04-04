package com.cayot.flyingmore.data.local.repository

import com.cayot.flyingmore.data.local.model.FlightNotes
import com.cayot.flyingmore.fake.data.local.dao.FakeFlightNotesDao
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class OfflineFlightNotesRepositoryTest {

    private lateinit var repository: OfflineFlightNotesRepository
    private val flightNotesDao = FakeFlightNotesDao()

    private val flightNotes1 = FlightNotes.getPlaceholderFlightNotes1()

    @Before
    fun setUp() {
        repository = OfflineFlightNotesRepository(flightNotesDao)
    }

    @Test
    fun getFromFlight_returnsCorrectFlightNotes() = runBlocking {
        flightNotesDao.insert(flightNotes1)

        val retrievedFlightNotes = repository.getFromFlight(1)

        assertEquals(flightNotes1, retrievedFlightNotes)
    }

    @Test
    fun getFromFlight_returnsNullForNonExistentFlight() = runBlocking {
        val retrievedFlightNotes = repository.getFromFlight(999)

        assertNull(retrievedFlightNotes)
    }

    @Test
    fun insertFlightNotes_insertsSuccessfully() = runBlocking {
        repository.insertFlightNotes(flightNotes1)

        val insertedFlightNotes = flightNotesDao.getFromFlight(flightNotes1.flightId)
        assertEquals(flightNotes1, insertedFlightNotes)
    }

    @Test
    fun updateFlightNotes_updatesSuccessfully() = runBlocking {
        flightNotesDao.insert(flightNotes1)
        val updatedFlightNotes = flightNotes1.copy(text = "Un bon vol !")

        repository.updateFlightNotes(updatedFlightNotes)

        val retrievedFlightNotes = repository.getFromFlight(flightNotes1.flightId)
        assertEquals(updatedFlightNotes, retrievedFlightNotes)
    }

    @Test
    fun removeFlightNotesById_deletesSuccessfully() = runBlocking {
        flightNotesDao.insert(flightNotes1)

        repository.removeFlightNotesById(flightNotes1.flightId)

        val retrievedFlightNotes = repository.getFromFlight(flightNotes1.flightId)
        assertNull(retrievedFlightNotes)
    }
}