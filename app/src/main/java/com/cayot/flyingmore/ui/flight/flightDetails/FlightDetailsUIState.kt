package com.cayot.flyingmore.ui.flight.flightDetails

import com.cayot.flyingmore.data.FlightMapState
import com.cayot.flyingmore.data.flight.FlightDetails

data class FlightDetailsUIState(
    val flight: FlightDetails? = null,
    val flightMapState: FlightMapState? = null,
    val notesVisible: Boolean = false,
    val notesEdition: Boolean = false
)
