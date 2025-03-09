package com.cayot.flyingmore.ui.flight.details

import com.cayot.flyingmore.data.model.FlightMapState
import com.cayot.flyingmore.data.model.FlightDetails

data class FlightDetailsUIState(
    val flight: FlightDetails? = null,
    val flightMapState: FlightMapState? = null,
    val notesVisible: Boolean = false,
    val notesEdition: Boolean = false
)
