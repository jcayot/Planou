package com.cayot.flyingmore.data.flightNotes

class OfflineFlightNotesRepository(private val flightNotesDao: FlightNotesDao) : FlightNotesRepository {
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