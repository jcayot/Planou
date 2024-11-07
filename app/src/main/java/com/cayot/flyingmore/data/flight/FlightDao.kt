package com.cayot.flyingmore.data.flight

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightDao {

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insert(flight: FlightApiModel)

	@Update
	suspend fun update(flight: FlightApiModel)

	@Query("DELETE from flights WHERE id = :id")
	suspend fun deleteById(id: Int)

	@Transaction
	@Query("SELECT * FROM flights WHERE id = :flightId")
	fun getFlightDetails(flightId: Int): Flow<FlightDetailsPOJO>

	@Transaction
	@Query("SELECT * FROM flights  ORDER BY departureTime DESC")
	fun getAllFlightBriefs(): Flow<List<FlightBriefPOJO>>
}