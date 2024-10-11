package com.cayot.planou.ui.flightAdd

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cayot.planou.data.airport.Airport
import com.cayot.planou.data.airport.AirportsRepository
import com.cayot.planou.data.flight.FlightsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class FlightAddViewModel(
	private val flightsRepository: FlightsRepository,
	private val airportsRepository: AirportsRepository
) : ViewModel() {

	private val _uiState = MutableStateFlow(FlightAddUIState())
	val uiState: StateFlow<FlightAddUIState> = _uiState

	private val _navigateBack = MutableSharedFlow<Unit>()
	val navigateBack: SharedFlow<Unit> = _navigateBack

	private var originAirport: Airport? = null
	private var destinationAirport: Airport? = null

	private var originSearchJob: Job? = null
	private var destinationSearchJob: Job? = null

	fun updateFlightDetails(flightForm: FlightForm) {
		val	originAirportChanged: Boolean = flightForm.originAirportString != _uiState.value.flightForm.originAirportString
		val destinationAirportChanged: Boolean = flightForm.destinationAirportString != _uiState.value.flightForm.destinationAirportString

		if (!originAirportChanged && !destinationAirportChanged) {
			updateUIState(
				flightForm = flightForm,
				isEntryValid = flightDetailsValid(flightForm)
			)
		} else {
			updateUIState(
				flightForm = flightForm,
				isEntryValid = false
			)
			if (originAirportChanged) {
				originAirport = null
				originSearchJob?.cancel()
				originSearchJob = viewModelScope.launch {
					originAirport = airportFromIataCode(flightForm.originAirportString)
					updateUIState(isEntryValid = flightDetailsValid())
				}
			}
			if (destinationAirportChanged) {
				destinationAirport = null
				destinationSearchJob?.cancel()
				destinationSearchJob = viewModelScope.launch {
					destinationAirport = airportFromIataCode(flightForm.destinationAirportString)
					updateUIState(isEntryValid = flightDetailsValid())
				}
			}
		}
	}

	fun	updateTravelClassDropdownVisibility(visible: Boolean) {
		updateUIState(travelClassDropdownExpanded = visible)
	}

	fun updateDepartureDayModalVisibility(visible: Boolean) {
		updateUIState(departureDayModalVisible = visible)
	}

	fun saveFlight() {
		updateUIState(formEnabled = false)
		viewModelScope.launch {
			if (flightDetailsValid()) {
				flightsRepository.insertFlight(_uiState.value.flightForm.toFlight(
					originAirport = originAirport!!,
					destinationAirport = destinationAirport!!)
				)
				_navigateBack.emit(Unit)
			} else
				updateUIState(formEnabled = true)
		}
	}

	private fun flightDetailsValid(flightForm: FlightForm = _uiState.value.flightForm) : Boolean {
		return (airportsValid(originAirport, destinationAirport) &&
				flightForm.isFlightNumberValid() &&
				flightForm.isAirlineValid() &&
				flightForm.isDepartureDayValid())
	}

	private fun updateUIState(
		flightForm: FlightForm = _uiState.value.flightForm,
		isEntryValid: Boolean = _uiState.value.isEntryValid,
		formEnabled: Boolean = _uiState.value.formEnabled,
		travelClassDropdownExpanded: Boolean = _uiState.value.travelClassDropdownExpanded,
		departureDayModalVisible: Boolean = _uiState.value.departureDayModalVisible,
	) {
		_uiState.update { currentState ->
			currentState.copy(
				flightForm = flightForm,
				isEntryValid = isEntryValid,
				formEnabled = formEnabled,
				travelClassDropdownExpanded = travelClassDropdownExpanded,
				departureDayModalVisible = departureDayModalVisible,
			)
		}
	}

	private fun airportsValid(origin: Airport?, destination: Airport?) : Boolean{
		return (origin != null && destination != null && origin != destination)
	}

	private suspend fun airportFromIataCode(code: String) : Airport? {
		return (airportsRepository.getAirportByIataCode(code))
	}
}