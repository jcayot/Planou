package com.cayot.planou.data.flight

import androidx.compose.ui.util.fastRoundToInt
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cayot.planou.data.TravelClass
import com.cayot.planou.ui.flightEdit.FlightForm
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
	val travelClass: TravelClass,
	val planeModel: String,
	val departureTime: Long,
	val	arrivalTime: Long? = null,
	val seatNumber: String?
) {
	companion object {
		fun getPlaceholderFlight1() : Flight {
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
				planeModel = "Airbus A220",
				seatNumber = "1A"
			))
		}
		fun getPlaceholderFlight2() : Flight {
			return (Flight(
				flightNumber = "AF1176",
				airline = "Air France",
				originAirportCode = "CDG",
				originAirportId = 0,
				destinationAirportCode = "HEL",
				destinationAirportId = 0,
				distance = 1910000.0f,
				departureTime = Date.from(Instant.now()).time,
				travelClass = TravelClass.ECONOMY,
				planeModel = "Airbus A220",
				seatNumber = "11F"
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

fun Flight.getArrivalTimeString() : String? {
	if (arrivalTime == null)
		return (null)
	val dayDifference = ChronoUnit.DAYS.between(Instant.ofEpochMilli(departureTime), Instant.ofEpochMilli(arrivalTime))
	val dayDiffSuffix = if (dayDifference < 1)
		""
	else
		"+ $dayDifference"
	return (SimpleDateFormat("h:mm a", Locale.getDefault()).format(arrivalTime) + dayDiffSuffix)
}

fun Flight.toFlightForm() : FlightForm {
	return (FlightForm(
		id = id,
		flightNumber = flightNumber,
		airline = airline,
		originAirportString = originAirportCode,
		destinationAirportString = destinationAirportCode,
		departureTime = Instant.ofEpochMilli(departureTime),
		arrivalTime = arrivalTime?.let { Instant.ofEpochMilli(it) },
		travelClass = travelClass,
		planeModel = planeModel,
		seatNumber = seatNumber ?: ""
	))
}
