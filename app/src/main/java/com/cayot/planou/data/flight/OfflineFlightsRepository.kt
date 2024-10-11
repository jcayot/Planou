package com.cayot.planou.data.flight

import kotlinx.coroutines.flow.Flow

class OfflineFlightsRepository(private val flightDao: FlightDao) : FlightsRepository {
	override fun getAllFlightsStream(): Flow<List<Flight>> {
		return (flightDao.getAllFlights())
	}

	override suspend fun getFlightStream(id: Int): Flight? {
		return (flightDao.getFlight(id))
	}

	override suspend fun insertFlight(flight: Flight) {
		return (flightDao.insert(flight))
	}

	override suspend fun deleteFlight(flight: Flight) {
		return (flightDao.delete(flight))
	}

	override suspend fun updateFlight(flight: Flight) {
		return (flightDao.update(flight))
	}
}