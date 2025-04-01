package com.cayot.flyingmore.ui.home.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cayot.flyingmore.data.repository.FlightsRepository
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
}
