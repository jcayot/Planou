package com.cayot.flyingmore.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.cayot.flyingmore.data.model.FlightBriefPOJO
import com.cayot.flyingmore.data.model.FlightDetailsPOJO
import com.cayot.flyingmore.data.local.model.FlightEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightDao {

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insert(flight: FlightEntity)

	@Update
	suspend fun update(flight: FlightEntity)

	@Query("DELETE from flights WHERE id = :id")
	suspend fun deleteById(id: Int)

	@Transaction
	@Query("SELECT * FROM flights WHERE id = :flightId")
	fun getFlightDetails(flightId: Int): Flow<FlightDetailsPOJO>

	@Transaction
	@Query("SELECT * FROM flights  ORDER BY departureTime DESC")
	fun getAllFlightBriefs(): Flow<List<FlightBriefPOJO>>
}