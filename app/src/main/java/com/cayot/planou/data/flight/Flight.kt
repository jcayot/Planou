package com.cayot.planou.data.flight

import androidx.compose.ui.util.fastRoundToInt
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cayot.planou.data.TravelClass
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

@Entity(tableName = "flights")
data class Flight(
	@PrimaryKey(autoGenerate = true)
	val id: Int = 0,
	val	flightNumber: String,
	val	airline: String,
	val originAirportCode: String,
	val originAirportId: Int,
	val destinationAirportCode: String,
	val	destinationAirportId: Int,
	val distance: Float,
	val departureTime: Long,
	val	arrivalTime: Long? = null,
	val travelClass: TravelClass,
	val planeModel: String
) {
	companion object {
		fun getPlaceholderFlight() : Flight {
			return (Flight(
				flightNumber = "AF1176",
				airline = "Air France",
				originAirportCode = "CDG",
				originAirportId = 0,
				destinationAirportCode = "HEL",
				destinationAirportId = 0,
				distance = 1910000.0f,
				departureTime = Date.from(Instant.now()).time,
				arrivalTime = Date.from(Instant.now()).time,
				travelClass = TravelClass.BUSINESS,
				planeModel = "Airbus A220"
			))
		}
	}
}

fun Flight.getDistanceString() : String {
	return ((distance / 1000.0f).fastRoundToInt().toString() + " km")
}

fun Flight.getDepartureDateString() : String {
	return (SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(departureTime))
}

fun Flight.getDepartureTimeString() : String {
	return (SimpleDateFormat("h:mm a", Locale.getDefault()).format(departureTime))
}

fun Flight.getArrivalTimeString() : String {
	if (arrivalTime == null)
		return ("-")
	val dayDifference = ChronoUnit.DAYS.between(Instant.ofEpochMilli(departureTime), Instant.ofEpochMilli(arrivalTime))
	val dayDiffSuffix = if (dayDifference < 1)
		""
	else
		"+ $dayDifference"
	return (SimpleDateFormat("h:mm a", Locale.getDefault()).format(arrivalTime) + dayDiffSuffix)
}