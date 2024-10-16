package com.cayot.planou.data.flightNotes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flight_notes")
data class FlightNotes (
    @PrimaryKey(autoGenerate = false)
    val flightId: Int,
    val text: String
)