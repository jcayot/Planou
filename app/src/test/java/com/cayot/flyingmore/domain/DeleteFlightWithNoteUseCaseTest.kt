package com.cayot.flyingmore.domain

import com.cayot.flyingmore.data.local.model.FlightNotes
import com.cayot.flyingmore.data.local.repository.OfflineFlightNotesRepository
import com.cayot.flyingmore.data.local.repository.OfflineFlightRepository
import com.cayot.flyingmore.data.model.Flight
import com.cayot.flyingmore.fake.data.local.dao.FakeFlightDao
import com.cayot.flyingmore.fake.data.local.dao.FakeFlightNotesDao
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DeleteFlightWithNoteUseCaseTest {

    private lateinit var useCase : DeleteFlightWithNoteUseCase
    private val flightRepository = OfflineFlightRepository(FakeFlightDao())
    private val flightNotesRepository = OfflineFlightNotesRepository(FakeFlightNotesDao())

    private val flight1 = Flight.getPlaceholder1()
    private val flightNotes1 = FlightNotes.getPlaceholderFlightNotes1()


    @Before
    fun setUp() {
        useCase = DeleteFlightWithNoteUseCase(
            flightRepository = flightRepository,
            flightNotesRepository = flightNotesRepository
        )
    }

    @Test
    fun testDeleteFlightWithNoteUseCase() = runBlocking {
        flightRepository.insertFlight(flight1)

        val savedFlight = flightRepository.getFlight(1).first()
        flightNotesRepository.insertFlightNotes(flightNotes1.copy(flightId = savedFlight.id))
        useCase.invoke(savedFlight.id)

        val allFlightStream = flightRepository.getAllFlightBriefsStream().first()
        val retrievedFlightNotes = flightNotesRepository.getFromFlight(1)

        assertEquals(0, allFlightStream.size)
        assertNull(retrievedFlightNotes)
    }
}