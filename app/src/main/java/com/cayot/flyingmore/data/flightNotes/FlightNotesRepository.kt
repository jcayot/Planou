package com.cayot.flyingmore.data.flightNotes

interface FlightNotesRepository {

    suspend fun getFromFlight(flightId: Int) : FlightNotes?

    suspend fun insertFlightNotes(flightNotes: FlightNotes)

    suspend fun updateFlightNotes(flightNotes: FlightNotes)

    suspend fun removeFlightNotesById(flightId: Int)
}