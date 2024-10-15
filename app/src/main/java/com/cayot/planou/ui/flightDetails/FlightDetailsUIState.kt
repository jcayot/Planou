package com.cayot.planou.ui.flightDetails

import com.cayot.planou.data.airport.Airport
import com.cayot.planou.data.flight.Flight
import com.cayot.planou.data.FlightMapState

data class FlightDetailsUIState(
	val flight: Flight? = null,
	val retrievedOriginAirport: Airport? = null,
	val retrievedDestinationAirport: Airport? = null,
	val flightMapState: FlightMapState? = null,
	val isRetrievingFlight: Boolean = true,
	val notesVisible: Boolean = false
)
