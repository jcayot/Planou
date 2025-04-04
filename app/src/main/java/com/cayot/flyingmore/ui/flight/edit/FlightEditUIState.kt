package com.cayot.flyingmore.ui.flight.edit

import com.cayot.flyingmore.data.model.DayDifference
import com.cayot.flyingmore.data.model.TravelClass
import com.cayot.flyingmore.data.local.model.Airport
import com.cayot.flyingmore.data.local.model.distanceToAirport
import com.cayot.flyingmore.data.model.Flight
import com.cayot.flyingmore.domain.CalendarFromDayDifferenceHourMinuteUseCase
import com.cayot.flyingmore.domain.ConvertLocalTimeToUtcCalendarUseCase
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar

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
	val dayDifference: DayDifference = DayDifference.ZERO,
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

fun FlightForm.dateValid(
    timeToUtcUseCase: ConvertLocalTimeToUtcCalendarUseCase,
    calendarFromDifference: CalendarFromDayDifferenceHourMinuteUseCase,
    originTimeZone: String,
    destinationTimeZone: String
) : Boolean {
	val departureUtcCalendar = timeToUtcUseCase(departureDate, departureHour, departureMinute, originTimeZone)

	if (arrivalHour == null && arrivalMinute == null) {
		return (true)
	}

	val arrivalUtcCalendar = calendarFromDifference(departureUtcCalendar, dayDifference, arrivalHour!!, arrivalMinute!!, destinationTimeZone)

	return (arrivalUtcCalendar.timeInMillis > departureUtcCalendar.timeInMillis)
}

fun FlightForm.seatNumberValid() : Boolean {
	return (seatNumber.isNotBlank() && seatNumber.length < 4)
}

fun FlightForm.toFlightDetails(
    originAirport: Airport,
    destinationAirport: Airport,
    timeToUtcUseCase: ConvertLocalTimeToUtcCalendarUseCase,
    calendarFromDifference: CalendarFromDayDifferenceHourMinuteUseCase
): Flight {
	val departureTime = timeToUtcUseCase(departureDate, departureHour, departureMinute, originAirport.timezone)
	val arrivalTime: Calendar? = if (arrivalHour != null && arrivalMinute != null)
		calendarFromDifference(departureTime, dayDifference, arrivalHour, arrivalMinute, destinationAirport.timezone)
	else
		null

	return (Flight(
		id = id,
		flightNumber = flightNumber,
		airline = airline,
		departureTime = departureTime,
		arrivalTime = arrivalTime,
		travelClass = travelClass,
		originAirport = originAirport,
		destinationAirport = destinationAirport,
		distance = originAirport.distanceToAirport(destinationAirport),
		planeModel = planeModel,
		seatNumber = if (seatNumberValid()) seatNumber else null
	))
}
