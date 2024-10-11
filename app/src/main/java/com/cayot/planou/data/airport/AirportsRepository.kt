package com.cayot.planou.data.airport

interface AirportsRepository {

	suspend fun insertAllAirports(airports: List<Airport>)

	suspend fun getAirportByIataCode(iataCode: String): Airport?

	suspend fun searchAirportsByFullNameStream(fullName: String) : List<Airport>

	suspend fun searchAirportsByIataCodeStream(iataCode: String) : List<Airport>
}