package com.cayot.flyingmore.data.flight

import androidx.compose.ui.util.fastRoundToInt
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cayot.flyingmore.data.TravelClass
import com.cayot.flyingmore.ui.flightEdit.FlightForm
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
	override fun equals(other: Any?): Boolean {
		if (this === other)
			return true
		if (javaClass != other?.javaClass)
			return false

		other as Flight

		if (flightNumber != other.flightNumber)
			return false
		if (airline != other.airline)
			return false
		if (originAirportCode != other.originAirportCode)
			return false
		if (originAirportId != other.originAirportId)
			return false
		if (destinationAirportCode != other.destinationAirportCode)
			return false
		if (destinationAirportId != other.destinationAirportId)
			return false
		if (distance != other.distance)
			return false
		if (travelClass != other.travelClass)
			return false
		if (planeModel != other.planeModel)
			return false
		if (departureTime != other.departureTime)
			return false
		if (arrivalTime != other.arrivalTime)
			return false
		if (seatNumber != other.seatNumber)
			return false

		return true
	}

	override fun hashCode(): Int {
		var result = flightNumber.hashCode()
		result = 31 * result + airline.hashCode()
		result = 31 * result + originAirportCode.hashCode()
		result = 31 * result + originAirportId
		result = 31 * result + destinationAirportCode.hashCode()
		result = 31 * result + destinationAirportId
		result = 31 * result + distance.hashCode()
		result = 31 * result + travelClass.hashCode()
		result = 31 * result + planeModel.hashCode()
		result = 31 * result + departureTime.hashCode()
		result = 31 * result + (arrivalTime?.hashCode() ?: 0)
		result = 31 * result + (seatNumber?.hashCode() ?: 0)
		return result
	}

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
