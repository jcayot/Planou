package com.cayot.flyingmore.ui.flightList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cayot.flyingmore.data.FlightMapState
import com.cayot.flyingmore.data.flight.FlightBrief
import com.cayot.flyingmore.data.flight.FlightsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FlightListViewModel(
	private val flightsRepository: FlightsRepository
) : ViewModel() {

	private val _uiState = MutableStateFlow(FlightListUIState())
	val uiState: StateFlow<FlightListUIState> = _uiState

	init {
		viewModelScope.launch {
			flightsRepository.getAllFlightBriefsStream().collect { flights ->
				_uiState.update { it.copy(flightList = makeFlightItemsList(flights)) }
			}
		}
	}

	fun updateFlightMap(flight: FlightBrief) {
		if (!_uiState.value.flightMapStateMap.containsKey(flight.id)) {
			viewModelScope.launch {

				val mapState = FlightMapState.fromAirports(flight.originAirport, flight.destinationAirport)
				val updatedFlightMapStateMap = _uiState.value.flightMapStateMap.toMutableMap()
				updatedFlightMapStateMap[flight.id] = mapState

				_uiState.update { it.copy(flightMapStateMap = updatedFlightMapStateMap.toMap()) }
			}
		}
	}
}
