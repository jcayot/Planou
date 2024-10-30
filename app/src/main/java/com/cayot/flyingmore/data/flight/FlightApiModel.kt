package com.cayot.flyingmore.data.flight

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cayot.flyingmore.data.TravelClass

@Entity(tableName = "flights")
data class FlightApiModel(
	@PrimaryKey(autoGenerate = true)
	val id: Int = 0,
	val	flightNumber: String,
	val	airline: String,
	val originAirportId: Int,
	val	destinationAirportId: Int,
	val distance: Float,
	val travelClass: TravelClass,
	val planeModel: String,
	val departureTime: Long,
	val	arrivalTime: Long? = null,
	val seatNumber: String?
)
