package com.cayot.flyingmore.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cayot.flyingmore.data.model.TravelClass

@Entity(tableName = "flights")
data class FlightEntity(
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
