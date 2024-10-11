package com.cayot.planou.data.airport

class OfflineAirportsRepository(private val airportDao: AirportDao) : AirportsRepository {

	override suspend fun insertAllAirports(airports: List<Airport>) {
		return (airportDao.insertAll(airports))
	}

	override suspend fun getAirportByIataCode(iataCode: String): Airport? {
		return (airportDao.getAirportByIataCode(iataCode))
	}

	override suspend fun searchAirportsByFullNameStream(fullName: String): List<Airport> {
		return (airportDao.searchAirportsByFullName(fullName))
	}

	override suspend fun searchAirportsByIataCodeStream(iataCode: String): List<Airport> {
		return (airportDao.searchAirportsByIataCode(iataCode))
	}
}