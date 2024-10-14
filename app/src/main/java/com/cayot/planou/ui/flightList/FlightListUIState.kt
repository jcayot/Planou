package com.cayot.planou.ui.flightList

import com.cayot.planou.data.FlightMapState
import com.cayot.planou.data.flight.Flight

data class FlightListUIState(
	val flightList: List<Flight> = emptyList(),
	val flightMapStateMap: Map<Int, FlightMapState?> = emptyMap()
)
