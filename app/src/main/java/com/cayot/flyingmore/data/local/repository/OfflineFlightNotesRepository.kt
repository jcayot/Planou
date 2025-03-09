package com.cayot.flyingmore.data.local.repository

import com.cayot.flyingmore.data.local.dao.FlightNotesDao
import com.cayot.flyingmore.data.local.model.FlightNotes
import com.cayot.flyingmore.data.repository.FlightNotesRepository

class OfflineFlightNotesRepository(private val flightNotesDao: FlightNotesDao) :
    FlightNotesRepository {
    override suspend fun getFromFlight(flightId: Int): FlightNotes? {
        return (flightNotesDao.getFromFlight(flightId))
    }

    override suspend fun insertFlightNotes(flightNotes: FlightNotes) {
        return (flightNotesDao.insert(flightNotes))
    }

    override suspend fun updateFlightNotes(flightNotes: FlightNotes) {
        return (flightNotesDao.update(flightNotes))
    }

    override suspend fun removeFlightNotesById(flightId: Int) {
        return (flightNotesDao.deleteById(flightId))
    }
}