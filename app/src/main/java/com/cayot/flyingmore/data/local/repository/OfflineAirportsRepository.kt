package com.cayot.flyingmore.data.local.repository

import com.cayot.flyingmore.data.local.dao.AirportDao
import com.cayot.flyingmore.data.local.model.Airport
import com.cayot.flyingmore.data.repository.AirportsRepository

class OfflineAirportsRepository(private val airportDao: AirportDao) : AirportsRepository {

	override suspend fun insertAllAirports(airports: List<Airport>) {
		return (airportDao.insertAll(airports))
	}

	override suspend fun getAirport(id: Int): Airport? {
		return (airportDao.getAirport(id))
	}

	override suspend fun getAirportByIataCode(iataCode: String): Airport? {
		return (airportDao.getAirportByIataCode(iataCode))
	}

	override suspend fun searchAirportsByFullName(fullName: String, limit: Int): List<Airport> {
		return (airportDao.searchAirportsByFullName(fullName, limit))
	}

	override suspend fun searchAirportsByIataCode(iataCode: String, limit: Int): List<Airport> {
		return (airportDao.searchAirportsByIataCode(iataCode, limit))
	}
}
