package com.cayot.planou.data.airport

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AirportDao {

	@Insert
	suspend fun insertAll(airports: List<Airport>)

	@Query("SELECT * from airports WHERE iataCode = :iataCode")
	suspend fun getAirportByIataCode(iataCode: String) : Airport?

	@Query("SELECT * FROM airports WHERE name LIKE '%' || :name || '%'")
	suspend fun searchAirportsByFullName(name: String): List<Airport>

	@Query("SELECT * FROM airports WHERE iataCode LIKE '%' || :iataCode || '%'")
	suspend fun	searchAirportsByIataCode(iataCode: String) : List<Airport>
}