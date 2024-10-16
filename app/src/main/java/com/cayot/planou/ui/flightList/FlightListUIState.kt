package com.cayot.planou.ui.flightList

import com.cayot.planou.data.FlightMapState
import com.cayot.planou.data.flight.Flight
import java.util.Calendar
import java.util.TimeZone

data class FlightListUIState(
	val flightList: List<FlightItem> = emptyList(),
	val flightMapStateMap: Map<Int, FlightMapState?> = emptyMap()
)

data class FlightItem(
	val flight: Flight? = null,
	val year: String? = null
)

fun makeFlightItemsList(flightList: List<Flight>) : List<FlightItem> {
	val listFlightItem = mutableListOf<FlightItem>()

	var	previousFlightYear : Int? = null
	flightList.forEach { flight: Flight ->

		val flightYear = Calendar.getInstance(TimeZone.getDefault()).apply { timeInMillis = flight.departureTime }.get(Calendar.YEAR)

		if (previousFlightYear == null || previousFlightYear != flightYear) {
			listFlightItem.add(FlightItem(year = flightYear.toString()))
			previousFlightYear = flightYear
		}
		listFlightItem.add(FlightItem(flight = flight))
	}
	return (listFlightItem.toList())
}