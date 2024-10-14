package com.cayot.planou.ui.flightAdd

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
			_uiState.update {
				it.copy(
					flightForm = flightForm,
					isEntryValid = flightDetailsValid(flightForm)
				)
			}
		} else {
			_uiState.update {
				it.copy(
					flightForm = flightForm,
					isEntryValid = false
				)
			}
			if (originAirportChanged) {
				originAirport = null
				originSearchJob?.cancel()
				originSearchJob = viewModelScope.launch {
					originAirport = airportFromIataCode(flightForm.originAirportString)
					_uiState.update {
						it.copy(
							isEntryValid = flightDetailsValid()
						)
					}
				}
			}
			if (destinationAirportChanged) {
				destinationAirport = null
				destinationSearchJob?.cancel()
				destinationSearchJob = viewModelScope.launch {
					destinationAirport = airportFromIataCode(flightForm.destinationAirportString)
					_uiState.update {
						it.copy(
							isEntryValid = flightDetailsValid()
						)
					}
				}
			}
		}
	}

	fun updateFormElementVisibility(formElementVisibility: FormElementVisibility) {
		_uiState.update {
			it.copy(formElementVisibility = formElementVisibility)
		}
	}

	fun saveFlight() {
		_uiState.update {
			it.copy(formEnabled = false)
		}
		viewModelScope.launch {
			if (flightDetailsValid()) {
				flightsRepository.insertFlight(_uiState.value.flightForm.toFlight(
					originAirport = originAirport!!,
					destinationAirport = destinationAirport!!)
				)
				_navigateBack.emit(Unit)
			} else
				_uiState.update {
					it.copy(formEnabled = true)
				}
		}
	}

	private fun flightDetailsValid(flightForm: FlightForm = _uiState.value.flightForm) : Boolean {
		return (airportsValid(originAirport, destinationAirport) &&
				flightForm.isFlightNumberValid() &&
				flightForm.isAirlineValid() &&
				flightForm.isPlaneModelValid() &&
				flightForm.areDateValid())
	}

	private fun airportsValid(origin: Airport?, destination: Airport?) : Boolean{
		return (origin != null && destination != null && origin != destination)
	}

	private suspend fun airportFromIataCode(code: String) : Airport? {
		return (airportsRepository.getAirportByIataCode(code))
	}
}