package com.cayot.planou.ui.flightDetails

import com.cayot.planou.data.airport.Airport
import com.cayot.planou.data.flight.Flight
import com.cayot.planou.data.FlightMapState
import com.cayot.planou.data.flightNotes.FlightNotes

data class FlightDetailsUIState(
	val flight: Flight? = null,
	val flightNotes: FlightNotes? = null,
	val retrievedOriginAirport: Airport? = null,
	val retrievedDestinationAirport: Airport? = null,
	val flightMapState: FlightMapState? = null,
	val notesVisible: Boolean = false,
	val notesEdition: Boolean = false
)
