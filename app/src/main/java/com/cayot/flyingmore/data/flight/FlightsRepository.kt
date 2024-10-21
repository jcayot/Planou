package com.cayot.flyingmore.data.flight

import kotlinx.coroutines.flow.Flow

interface FlightsRepository {

	fun getAllFlightsStream() : Flow<List<Flight>>

	fun getFlight(id: Int): Flow<Flight?>

	suspend fun insertFlight(flight: Flight)

	suspend fun deleteFlightById(id: Int)

	suspend fun updateFlight(flight: Flight)
}