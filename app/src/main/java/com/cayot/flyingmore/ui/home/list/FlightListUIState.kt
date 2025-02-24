package com.cayot.flyingmore.ui.home.list

import com.cayot.flyingmore.data.FlightMapState
import com.cayot.flyingmore.data.flight.FlightBrief
import java.util.Calendar

data class FlightListUIState(
	val flightList: List<FlightItem>? = null,
	val flightMapStateMap: Map<Int, FlightMapState?> = emptyMap()
)

data class FlightItem(
	val flight: FlightBrief? = null,
	val year: String? = null
)

fun makeFlightItemsList(flightList: List<FlightBrief>) : List<FlightItem> {
	val listFlightItem = mutableListOf<FlightItem>()

	var	previousFlightYear : Int? = null
	flightList.forEach { flight: FlightBrief ->

		val flightYear = flight.departureTime.get(Calendar.YEAR)

		if (previousFlightYear == null || previousFlightYear != flightYear) {
			listFlightItem.add(FlightItem(year = flightYear.toString()))
			previousFlightYear = flightYear
		}
		listFlightItem.add(FlightItem(flight = flight))
	}
	return (listFlightItem.toList())
}