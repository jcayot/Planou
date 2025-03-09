package com.cayot.flyingmore.ui.flight.details

import com.cayot.flyingmore.data.model.FlightMapState
import com.cayot.flyingmore.data.model.Flight

data class FlightDetailsUIState(
    val flight: Flight? = null,
    val flightMapState: FlightMapState? = null,
    val notesVisible: Boolean = false,
    val notesEdition: Boolean = false
)
