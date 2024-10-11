package com.cayot.planou.ui.flightDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cayot.planou.data.FlightMapState
import com.cayot.planou.data.airport.Airport
import com.cayot.planou.data.airport.AirportsRepository
import com.cayot.planou.data.flight.Flight
import com.cayot.planou.data.flight.FlightsRepository
import com.cayot.planou.ui.navigation.PlanouScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FlightDetailsViewModel(
	savedStateHandle: SavedStateHandle,
	flightsRepository: FlightsRepository,
	airportsRepository: AirportsRepository) : ViewModel() {

	private val flightId: Int = checkNotNull(savedStateHandle[PlanouScreen.Details.argName])

	private val _uiState = MutableStateFlow(FlightDetailsUIState())
	val uiState :StateFlow<FlightDetailsUIState> = _uiState

	init {
		viewModelScope.launch {
			val flight : Flight? = flightsRepository.getFlightStream(flightId)

			if (flight != null) {
				val originAirport = airportsRepository.getAirportByIataCode(flight.originAirportCode)
				val destinationAirport = airportsRepository.getAirportByIataCode(flight.destinationAirportCode)

				onFlightRetrieved(flight = flight,
					originAirport = originAirport,
					destinationAirport = destinationAirport)
			} else {
				_uiState.update {
					it.copy(isRetrievingFlight = false)
				}
			}
		}
	}

	private	fun onFlightRetrieved(
		flight: Flight,
		originAirport: Airport?,
		destinationAirport: Airport?
	) {
		var flightMapState : FlightMapState? = null

		if (originAirport != null && destinationAirport != null)
			flightMapState = FlightMapState.fromAirports(originAirport, destinationAirport)

		_uiState.update {
			it.copy(flight = flight,
				retrievedOriginAirport = originAirport,
				retrievedDestinationAirport = destinationAirport,
				flightMapState = flightMapState,
				isRetrievingFlight = false)
		}
	}
}