package com.cayot.planou.data.airport

interface AirportsRepository {

	suspend fun insertAllAirports(airports: List<Airport>)

	suspend fun getAirport(id : Int): Airport?

	suspend fun getAirportByIataCode(iataCode: String): Airport?

	suspend fun searchAirportsByFullName(fullName: String, limit: Int) : List<Airport>

	suspend fun searchAirportsByIataCode(iataCode: String, limit: Int) : List<Airport>
}