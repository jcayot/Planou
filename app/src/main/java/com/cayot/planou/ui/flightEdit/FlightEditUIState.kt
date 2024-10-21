package com.cayot.planou.ui.flightEdit

import com.cayot.planou.data.TravelClass
import com.cayot.planou.data.airport.Airport
import com.cayot.planou.data.airport.distanceToAirport
import com.cayot.planou.data.flight.Flight
import java.time.Instant

data class FlightEditUIState(
	val flightForm: FlightForm = FlightForm(),
	val formEnabled: Boolean = false,
	val	isEntryValid: Boolean = false,
	val	canDelete: Boolean = false,
	val formElementVisibility: FormElementVisibility = FormElementVisibility()
	)

data class FlightForm (
	val id: Int = 0,
	val	originAirportString: String = "",
	val foundOriginAirportsList: List<Airport> = emptyList(),
	val destinationAirportString: String = "",
	val foundDestinationAirportList: List<Airport> = emptyList(),
	val airline: String = "",
	val	flightNumber: String = "",
	val planeModel: String = "",
	val travelClass: TravelClass = TravelClass.ECONOMY,
	val departureTime: Instant = Instant.now(),
	val arrivalTime: Instant? = null,
	val seatNumber: String = "",
	)

data class FormElementVisibility(
	val originAirportDropdownVisible: Boolean = false,
	val destinationAirportDropdownVisible: Boolean = false,
	val travelClassDropdownVisible: Boolean = false,
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
	return (departureTime.isBefore(Instant.now()) && arrivalTime?.isAfter(departureTime) ?: true)
}

fun FlightForm.seatNumberValid() : Boolean {
	return (seatNumber.isNotBlank() && seatNumber.length < 4)
}

fun FlightForm.toFlight(originAirport: Airport, destinationAirport: Airport) : Flight {
	return (Flight(
		id = id,
		flightNumber = flightNumber,
		airline = airline,
		departureTime = departureTime.toEpochMilli(),
		arrivalTime = arrivalTime?.toEpochMilli(),
		travelClass = travelClass,
		originAirportCode = originAirport.iataCode,
		originAirportId = originAirport.id,
		destinationAirportCode = destinationAirport.iataCode,
		destinationAirportId = destinationAirport.id,
		distance = originAirport.distanceToAirport(destinationAirport),
		planeModel = planeModel,
		seatNumber = if (seatNumberValid()) seatNumber else null
	))
}
