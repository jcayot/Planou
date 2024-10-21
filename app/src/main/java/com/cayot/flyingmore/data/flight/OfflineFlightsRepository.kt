package com.cayot.flyingmore.data.flight

import kotlinx.coroutines.flow.Flow

class OfflineFlightsRepository(private val flightDao: FlightDao) : FlightsRepository {
	override fun getAllFlightsStream(): Flow<List<Flight>> {
		return (flightDao.getAllFlights())
	}

	override fun getFlight(id: Int): Flow<Flight?> {
		return (flightDao.getFlight(id))
	}

	override suspend fun insertFlight(flight: Flight) {
		return (flightDao.insert(flight))
	}

	override suspend fun deleteFlightById(id: Int) {
		return (flightDao.deleteById(id))
	}

	override suspend fun updateFlight(flight: Flight) {
		return (flightDao.update(flight))
	}
}