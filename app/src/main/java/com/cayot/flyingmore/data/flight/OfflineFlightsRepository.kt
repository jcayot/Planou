package com.cayot.flyingmore.data.flight

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OfflineFlightsRepository(private val flightDao: FlightDao) : FlightsRepository {
	override fun getAllFlightBriefsStream(): Flow<List<FlightBrief>> {
		return (flightDao.getAllFlightBriefs().map { pojoList ->
			pojoList.map { item -> item.toFlightBrief() }
		})
	}

	override fun getFlight(id: Int): Flow<FlightDetails?> {
		return (flightDao.getFlightDetails(id).map { it.toFlightDetails() })
	}

	override suspend fun insertFlight(flight: FlightDetails) {
		return (flightDao.insert(flight.toFlightApiModel()))
	}

	override suspend fun deleteFlightById(id: Int) {
		return (flightDao.deleteById(id))
	}

	override suspend fun updateFlight(flight: FlightDetails) {
		return (flightDao.update(flight.toFlightApiModel()))
	}
}