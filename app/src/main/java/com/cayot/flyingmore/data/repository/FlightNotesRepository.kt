package com.cayot.flyingmore.data.repository

import com.cayot.flyingmore.data.local.model.FlightNotes

interface FlightNotesRepository {

    suspend fun getFromFlight(flightId: Int) : FlightNotes?

    suspend fun insertFlightNotes(flightNotes: FlightNotes)

    suspend fun updateFlightNotes(flightNotes: FlightNotes)

    suspend fun removeFlightNotesById(flightId: Int)
}