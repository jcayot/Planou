package com.cayot.planou.data.flight

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightDao {

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insert(flight: Flight)

	@Update
	suspend fun update(flight: Flight)

	@Delete
	suspend fun delete(flight: Flight)

	@Query("SELECT * from flights WHERE id = :id")
	fun getFlight(id: Int) : Flow<Flight>

	@Query("SELECT * from flights ORDER BY departureTime DESC")
	fun getAllFlights() : Flow<List<Flight>>
}