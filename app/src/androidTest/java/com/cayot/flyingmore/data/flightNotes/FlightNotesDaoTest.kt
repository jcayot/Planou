package com.cayot.flyingmore.data.flightNotes

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cayot.flyingmore.data.DatabaseTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FlightNotesDaoTest : DatabaseTest() {
    private lateinit var flightNotesDao: FlightNotesDao

    private val flightNotesList = listOf(
        FlightNotes.getPlaceholderFlightNotes1(),
        FlightNotes.getPlaceholderFlightNotes2()
    )

    override fun createDatabase() {
        super.createDatabase()
        flightNotesDao = flyingMoreDatabase.flightNotesDao()
    }

    private suspend fun fillDatabase() {
        flightNotesList.forEach {
            flightNotesDao.insert(it)
        }
    }

    @Test
    fun insertFlightNotesTest() = runBlocking {
        val flightNotes = FlightNotes.getPlaceholderFlightNotes1()
        flightNotesDao.insert(flightNotes)
        val dbFlightNotes = flightNotesDao.getFromFlight(flightNotes.flightId)
        assertEquals(dbFlightNotes, flightNotes)
    }

    @Test
    fun updateFlightNotesTest() = runBlocking {
        val flightNotes = FlightNotes.getPlaceholderFlightNotes1()
        flightNotesDao.insert(flightNotes)
        val updatedFlightNotes = flightNotes.copy(text = "Updated Note for flight 1")
        flightNotesDao.update(updatedFlightNotes)
        val dbFlightNotes = flightNotesDao.getFromFlight(flightNotes.flightId)
        assertEquals(dbFlightNotes, updatedFlightNotes)
    }

    @Test
    fun deleteFlightNotesByIdTest() = runBlocking {
        val flightNotes = FlightNotes.getPlaceholderFlightNotes1()
        flightNotesDao.insert(flightNotes)
        flightNotesDao.deleteById(flightNotes.flightId)
        val dbFlightNotes = flightNotesDao.getFromFlight(flightNotes.flightId)
        assertNull(dbFlightNotes)
    }

    @Test
    fun getFromFlightTest() = runBlocking {
        fillDatabase()
        val dbFlightNotes1 = flightNotesDao.getFromFlight(flightNotesList[0].flightId)
        val dbFlightNotes2 = flightNotesDao.getFromFlight(flightNotesList[1].flightId)
        assertEquals(dbFlightNotes1, flightNotesList[0])
        assertEquals(dbFlightNotes2, flightNotesList[1])
    }
}