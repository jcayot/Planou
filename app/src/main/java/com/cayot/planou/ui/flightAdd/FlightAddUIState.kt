package com.cayot.planou.ui.flightAdd

import com.cayot.planou.data.TravelClass
import com.cayot.planou.data.airport.Airport
import com.cayot.planou.data.airport.distanceToAirport
import com.cayot.planou.data.flight.Flight
import java.time.Instant

data class FlightAddUIState(
	val flightForm: FlightForm = FlightForm(),
	val formEnabled: Boolean = true,
	val	isEntryValid: Boolean = false,
	val formElementVisibility: FormElementVisibility = FormElementVisibility()
	)

data class FlightForm (
	val	originAirportString: String = "",
	val destinationAirportString: String = "",
	val airline: String = "",
	val	flightNumber: String = "",
	val planeModel: String = "",
	val travelClass: TravelClass = TravelClass.ECONOMY,
	val departureTime: Instant = Instant.now(),
	val arrivalTime: Instant = Instant.now()
	)

data class FormElementVisibility(
	val travelClassDropdownExpanded: Boolean = false,
	val departureDayModalVisible: Boolean = false,
	val departureTimeModalVisible: Boolean = false,
	val arrivalDayModalVisible: Boolean = false,
	val arrivalTimeModalVisible: Boolean = false
)

fun FlightForm.isFlightNumberValid() : Boolean {
	if (flightNumber.length < 3 || flightNumber.length > 6)
		return (false)
	repeat(2) { i ->
		if (!flightNumber[i].isLetter())
			return (false)
	}
	for (i in 2 until flightNumber.length) {
		if (!flightNumber[i].isDigit())
			return (false)
	}
	return (true)
}

fun FlightForm.isAirlineValid() : Boolean {
	return (airline.isNotBlank())
}

fun FlightForm.isPlaneModelValid() : Boolean {
	return (planeModel.isNotBlank())
}

fun FlightForm.areDateValid() : Boolean {
	return (departureTime.isBefore(arrivalTime) && departureTime.isBefore(Instant.now()))
}

fun FlightForm.toFlight(originAirport: Airport, destinationAirport: Airport) : Flight {
	return (Flight(
		flightNumber = flightNumber,
		airline = airline,
		departureTime = departureTime.toEpochMilli(),
		travelClass = travelClass,
		originAirportCode = originAirport.iataCode,
		originAirportId = originAirport.id,
		destinationAirportCode = destinationAirport.iataCode,
		destinationAirportId = destinationAirport.id,
		distance = originAirport.distanceToAirport(destinationAirport),
		planeModel = planeModel
	))
}
