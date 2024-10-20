package com.cayot.planou.data.flightNotes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FlightNotesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(flightNotes: FlightNotes)

    @Update
    suspend fun update(flightNotes: FlightNotes)

    @Delete
    suspend fun delete(flightNotes: FlightNotes)

    @Query("SELECT * FROM flight_notes WHERE flightId =:flightId")
    suspend fun getFromFlight(flightId : Int) : FlightNotes?
}