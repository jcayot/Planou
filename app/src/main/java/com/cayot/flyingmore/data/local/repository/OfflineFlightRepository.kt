package com.cayot.flyingmore.data.local.repository

import com.cayot.flyingmore.data.local.dao.FlightDao
import com.cayot.flyingmore.data.model.FlightBrief
import com.cayot.flyingmore.data.model.Flight
import com.cayot.flyingmore.data.model.toFlightEntity
import com.cayot.flyingmore.data.model.toFlightBrief
import com.cayot.flyingmore.data.model.toFlightDetails
import com.cayot.flyingmore.data.repository.FlightRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class OfflineFlightRepository(private val flightDao: FlightDao) : FlightRepository {
	override fun getAllFlightBriefsStream(): Flow<List<FlightBrief>> {
		return (flightDao.getAllFlightBriefs().map { pojoList ->
			pojoList.map { item -> item.toFlightBrief() }
		})
	}

	override fun getAllFlightForDepartureTimeRange(
		startTime: Long,
		endTime: Long
	): List<Flight> {
		return (flightDao.getAllFlightForDepartureTimeRange(startTime, endTime).map {
			it.toFlightDetails()
		})
	}

	override fun getFlight(id: Int): Flow<Flight> {
		return (flightDao.getFlightDetails(id).filterNotNull().map { it.toFlightDetails() })
	}

	override suspend fun insertFlight(flight: Flight) {
		return (flightDao.insert(flight.toFlightEntity()))
	}

	override suspend fun deleteFlightById(id: Int) {
		return (flightDao.deleteById(id))
	}

	override suspend fun updateFlight(flight: Flight) {
		return (flightDao.update(flight.toFlightEntity()))
	}
}