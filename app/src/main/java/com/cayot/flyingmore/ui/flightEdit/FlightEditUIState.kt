package com.cayot.flyingmore.ui.flightEdit

import com.cayot.flyingmore.data.TravelClass
import com.cayot.flyingmore.data.airport.Airport
import com.cayot.flyingmore.data.airport.distanceToAirport
import com.cayot.flyingmore.data.flight.FlightDetails
import com.cayot.flyingmore.domain.ConvertLocalTimeToCalendarUseCase
import java.time.LocalDate
import java.time.ZoneId

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
	val departureDate: Long = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
	val departureHour: Int = 0,
	val departureMinute: Int = 0,
	val arrivalDate: Long? = null,
	val arrivalHour: Int? = null,
	val arrivalMinute: Int? = null,
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

fun FlightForm.areDateValid(
	timeToUtcUseCase: ConvertLocalTimeToCalendarUseCase,
	originTimeZone: String,
	destinationTimeZone: String
) : Boolean {
	val departureUtcCalendar = timeToUtcUseCase(departureDate, departureHour, departureMinute, originTimeZone)

	if (arrivalDate == null && arrivalHour == null && arrivalMinute == null) {
		return (true)
	}

	if (arrivalDate == null || arrivalHour == null || arrivalMinute == null) {
		return (false)
	}

	val arrivalUtcCalendar = timeToUtcUseCase(arrivalDate, arrivalHour, arrivalMinute, destinationTimeZone)

	return (arrivalUtcCalendar.timeInMillis > departureUtcCalendar.timeInMillis)
}

fun FlightForm.seatNumberValid() : Boolean {
	return (seatNumber.isNotBlank() && seatNumber.length < 4)
}

fun FlightForm.toFlightDetails(
	originAirport: Airport,
	destinationAirport: Airport,
	timeToUtcUseCase: ConvertLocalTimeToCalendarUseCase
): FlightDetails {
	return (FlightDetails(
		id = id,
		flightNumber = flightNumber,
		airline = airline,
		departureTime = timeToUtcUseCase(departureDate, departureHour, departureMinute, originAirport.timezone),
		arrivalTime = arrivalDate?.let { timeToUtcUseCase(arrivalDate, arrivalHour!!, arrivalMinute!!, destinationAirport.timezone) },
		travelClass = travelClass,
		originAirport = originAirport,
		destinationAirport = destinationAirport,
		distance = originAirport.distanceToAirport(destinationAirport),
		planeModel = planeModel,
		seatNumber = if (seatNumberValid()) seatNumber else null
	))
}
