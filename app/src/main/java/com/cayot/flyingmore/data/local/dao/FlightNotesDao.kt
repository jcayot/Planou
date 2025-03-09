package com.cayot.flyingmore.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cayot.flyingmore.data.local.model.FlightNotes

@Dao
interface FlightNotesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(flightNotes: FlightNotes)

    @Update
    suspend fun update(flightNotes: FlightNotes)

    @Query("DELETE from flight_notes WHERE flightId = :flightId")
    suspend fun deleteById(flightId : Int)

    @Query("SELECT * FROM flight_notes WHERE flightId =:flightId")
    suspend fun getFromFlight(flightId : Int) : FlightNotes?
}