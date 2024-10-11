@file:OptIn(ExperimentalMaterial3Api::class)

package com.cayot.planou.ui.flightAdd

import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import com.cayot.planou.data.flight.Flight
import com.cayot.planou.data.TravelClass
import com.cayot.planou.data.airport.Airport
import com.cayot.planou.data.airport.distanceToAirport
import java.time.LocalDate
import java.util.Date
import java.util.Locale

data class FlightAddUIState(
	val flightForm: FlightForm = FlightForm(),
	val formEnabled: Boolean = true,
	val	isEntryValid: Boolean = false,
	val travelClassDropdownExpanded: Boolean = false,
	val departureDayModalVisible: Boolean = false,
	val	departureDayPickerState: DatePickerState =
		DatePickerState(locale = Locale.getDefault(),
			selectableDates = SelectableDatesRange,
			initialSelectedDateMillis = Date().time)
	)

data class FlightForm (
	val	originAirportString: String = "",
	val destinationAirportString: String = "",
	val airline: String = "",
	val	flightNumber: String = "",
	val travelClass: TravelClass = TravelClass.ECONOMY,
	val departureDay: Date = Date(),
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

fun FlightForm.isDepartureDayValid() : Boolean {
	if (departureDay.after(Date()))
		return (false)
	return (true)
}

fun FlightForm.toFlight(originAirport: Airport, destinationAirport: Airport) : Flight {
	return (Flight(
		flightNumber = flightNumber,
		airline = airline,
		departureTime = departureDay.time,
		travelClass = travelClass,
		originAirportCode = originAirport.iataCode,
		originAirportId = originAirport.id,
		destinationAirportCode = destinationAirport.iataCode,
		destinationAirportId = destinationAirport.id,
		distance = originAirport.distanceToAirport(destinationAirport),
		planeModel = "Airbus A220"
	))
}

object SelectableDatesRange: SelectableDates {
	override fun isSelectableDate(utcTimeMillis: Long): Boolean {
		return (utcTimeMillis <= System.currentTimeMillis())
	}

	override fun isSelectableYear(year: Int): Boolean {
		return (year <= LocalDate.now().year)
	}
}