package com.cayot.flyingmore.data.repository

import com.cayot.flyingmore.data.model.FlightBrief
import com.cayot.flyingmore.data.model.Flight
import kotlinx.coroutines.flow.Flow

interface FlightsRepository {

	fun getAllFlightBriefsStream() : Flow<List<FlightBrief>>

	fun getAllFlightForDepartureTimeRange(startTime: Long, endTime: Long) : List<Flight>

	fun getFlight(id: Int): Flow<Flight>

	suspend fun insertFlight(flight: Flight)

	suspend fun deleteFlightById(id: Int)

	suspend fun updateFlight(flight: Flight)
}