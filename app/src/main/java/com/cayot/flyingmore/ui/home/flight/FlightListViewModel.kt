package com.cayot.flyingmore.ui.home.flight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cayot.flyingmore.data.repository.FlightRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FlightListViewModel(
	private val flightRepository: FlightRepository
) : ViewModel() {

	private val _uiState = MutableStateFlow(FlightListUIState())
	val uiState: StateFlow<FlightListUIState> = _uiState

	init {
		viewModelScope.launch {
			flightRepository.getAllFlightBriefsStream().collect { flights ->
				_uiState.update { it.copy(flightList = makeFlightItemsList(flights)) }
			}
		}
	}
}
