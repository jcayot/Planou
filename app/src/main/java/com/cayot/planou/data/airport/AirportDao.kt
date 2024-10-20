package com.cayot.planou.data.airport

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AirportDao {

	@Insert
	suspend fun insertAll(airports: List<Airport>)

	@Query("SELECT * from airports WHERE id = :id")
	suspend fun getAirport(id: Int) : Airport?

	@Query("SELECT * from airports WHERE iataCode = :iataCode")
	suspend fun getAirportByIataCode(iataCode: String) : Airport?

	@Query("SELECT * FROM airports WHERE name LIKE '%' || :name || '%'  LIMIT :limit")
	suspend fun searchAirportsByFullName(name: String, limit: Int): List<Airport>

	@Query("SELECT * FROM airports WHERE iataCode LIKE '%' || :iataCode || '%' LIMIT :limit")
	suspend fun	searchAirportsByIataCode(iataCode: String, limit: Int) : List<Airport>
}