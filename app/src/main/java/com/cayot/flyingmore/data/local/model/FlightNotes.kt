package com.cayot.flyingmore.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flight_notes")
data class FlightNotes (
    @PrimaryKey(autoGenerate = false)
    val flightId: Int,
    val text: String
) {
    companion object {
        fun getPlaceholderFlightNotes1() = FlightNotes(flightId = 1, text = "Note for flight 1")
        fun getPlaceholderFlightNotes2() = FlightNotes(flightId = 2, text = "Note for flight 2")
    }
}