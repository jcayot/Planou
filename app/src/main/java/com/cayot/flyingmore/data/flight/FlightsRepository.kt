package com.cayot.flyingmore.data.flight

import kotlinx.coroutines.flow.Flow

interface FlightsRepository {

	fun getAllFlightBriefsStream() : Flow<List<FlightBrief>>

	fun getFlight(id: Int): Flow<FlightDetails>

	suspend fun insertFlight(flight: FlightDetails)

	suspend fun deleteFlightById(id: Int)

	suspend fun updateFlight(flight: FlightDetails)
}