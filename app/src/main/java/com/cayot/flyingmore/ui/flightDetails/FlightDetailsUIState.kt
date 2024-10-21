package com.cayot.flyingmore.ui.flightDetails

import com.cayot.flyingmore.data.airport.Airport
import com.cayot.flyingmore.data.flight.Flight
import com.cayot.flyingmore.data.FlightMapState
import com.cayot.flyingmore.data.flightNotes.FlightNotes

data class FlightDetailsUIState(
	val flight: Flight? = null,
	val flightNotes: FlightNotes? = null,
	val retrievedOriginAirport: Airport? = null,
	val retrievedDestinationAirport: Airport? = null,
	val flightMapState: FlightMapState? = null,
	val notesVisible: Boolean = false,
	val notesEdition: Boolean = false
)
