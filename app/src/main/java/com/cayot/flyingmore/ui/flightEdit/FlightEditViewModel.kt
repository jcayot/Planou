package com.cayot.flyingmore.ui.flightEdit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cayot.flyingmore.data.airport.Airport
import com.cayot.flyingmore.data.airport.AirportsRepository
import com.cayot.flyingmore.data.flight.FlightsRepository
import com.cayot.flyingmore.data.flight.toFlightForm
import com.cayot.flyingmore.domain.CalendarFromDayDifferenceHourMinuteUseCase
import com.cayot.flyingmore.domain.ConvertLocalTimeToCalendarUseCase
import com.cayot.flyingmore.domain.DeleteFlightWithNoteUseCase
import com.cayot.flyingmore.ui.navigation.FlyingMoreScreen
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FlightEditViewModel(
	private val flightsRepository: FlightsRepository,
	private val airportsRepository: AirportsRepository,
	private val deleteFlightWithNoteUseCase: DeleteFlightWithNoteUseCase,
	private val convertLocalTimeToCalendarUseCase: ConvertLocalTimeToCalendarUseCase,
	private val calendarFromDifference: CalendarFromDayDifferenceHourMinuteUseCase,
	savedStateHandle: SavedStateHandle
) : ViewModel() {

	private val flightId: Int? = savedStateHandle[FlyingMoreScreen.Edit.argName]

	private val _uiState = MutableStateFlow(FlightEditUIState())
	val uiState: StateFlow<FlightEditUIState> = _uiState

	private val _navigateBack = MutableSharedFlow<Unit>()
	val navigateBack: SharedFlow<Unit> = _navigateBack

	private val _navigateHome = MutableSharedFlow<Unit>()
	val navigateHome: SharedFlow<Unit> = _navigateHome

	private var originAirport: Airport? = null
	private var destinationAirport: Airport? = null

	private var originSearchJob: Job? = null
	private var destinationSearchJob: Job? = null

	init {
	    if (flightId != null)
			getFlight(flightId)
		else
			_uiState.update { it.copy(formEnabled = true) }
	}

	fun updateFlightForm(flightForm: FlightForm) {
		_uiState.update {
			it.copy(
				flightForm = flightForm,
				isEntryValid = flightDetailsValid(flightForm)
			)
		}
	}

	fun searchDepartureAirport(airportString: String) {
		originAirport = null
		updateFlightForm(flightForm = _uiState.value.flightForm.copy(
			originAirportString = airportString,
			foundOriginAirportsList = emptyList()
		))
		originSearchJob?.cancel()
		originSearchJob = viewModelScope.launch {
			val airportList = searchAirportsDatabase(airportString)
			updateFlightForm(flightForm = _uiState.value.flightForm.copy(
				foundOriginAirportsList = airportList
			))
		}
	}

	fun departureAirportSelected(airport: Airport) {
		originSearchJob?.cancel()
		originAirport = airport
		updateFlightForm(flightForm = _uiState.value.flightForm.copy(
			originAirportString = airport.iataCode,
			foundOriginAirportsList = emptyList()
		))
	}

	fun departureAirportDismissed(arrivalInput: String, foundAirport: List<Airport>) {
		originSearchJob?.cancel()
		originAirport = onAirportDropdownCollapse(arrivalInput, foundAirport)
		_uiState.update { it.copy(isEntryValid = flightDetailsValid()) }
	}

	fun searchArrivalAirport(airportString: String) {
		destinationAirport = null
		updateFlightForm(flightForm = _uiState.value.flightForm.copy(destinationAirportString = airportString))
		destinationSearchJob?.cancel()
		destinationSearchJob = viewModelScope.launch {
			val airportList = searchAirportsDatabase(airportString)
			updateFlightForm(flightForm = _uiState.value.flightForm.copy(
				foundDestinationAirportList = airportList
			))
		}
	}

	fun arrivalAirportSelected(airport: Airport) {
		destinationSearchJob?.cancel()
		destinationAirport = airport
		updateFlightForm(flightForm = _uiState.value.flightForm.copy(
			destinationAirportString = airport.iataCode,
			foundDestinationAirportList = emptyList()
		))
	}

	fun arrivalAirportDismissed(arrivalInput: String, foundAirport: List<Airport>) {
		destinationSearchJob?.cancel()
		destinationAirport = onAirportDropdownCollapse(arrivalInput, foundAirport)
		_uiState.update { it.copy(isEntryValid = flightDetailsValid()) }
	}

	fun updateFormElementVisibility(formElementVisibility: FormElementVisibility) {
		_uiState.update {
			it.copy(formElementVisibility = formElementVisibility)
		}
	}

	fun saveFlight() {
		if (flightDetailsValid()) {
			viewModelScope.launch {
				_uiState.update { it.copy(formEnabled = false) }
				val flightDetails = _uiState.value.flightForm.toFlightDetails(
					originAirport = originAirport!!,
					destinationAirport = destinationAirport!!,
					convertLocalTimeToCalendarUseCase,
					calendarFromDifference)
				if (flightId == null)
					flightsRepository.insertFlight(flightDetails)
				else
					flightsRepository.updateFlight(flightDetails)
				_navigateBack.emit(Unit)
			}
		}
	}

	fun deleteFlight() {
		if (flightId != null) {
			_uiState.update { it.copy(formEnabled = false) }
			viewModelScope.launch {
				deleteFlightWithNoteUseCase(flightId)
				_navigateHome.emit(Unit)
			}
		}
	}

	private fun flightDetailsValid(flightForm: FlightForm = _uiState.value.flightForm) : Boolean {
		return (airportsValid(originAirport, destinationAirport) &&
				flightForm.isFlightNumberValid() &&
				flightForm.isAirlineValid() &&
				flightForm.isPlaneModelValid() &&
				flightForm.dateValid(
					convertLocalTimeToCalendarUseCase,
					calendarFromDifference,
					originAirport!!.timezone,
					destinationAirport!!.timezone))
	}

	private fun airportsValid(origin: Airport?, destination: Airport?) : Boolean{
		return (origin != null && destination != null && origin != destination)
	}

	private fun onAirportDropdownCollapse(airportInput: String, foundAirport: List<Airport>) : Airport? {
		foundAirport.forEach {
			if (it.iataCode == airportInput)
				return (it)
		}
		return (null)
	}

	private fun getFlight(flightId: Int)  = viewModelScope.launch {
		val flight = flightsRepository.getFlight(flightId).first()

		originAirport = flight.originAirport
		destinationAirport = flight.destinationAirport
		updateFlightForm(flight.toFlightForm())
		_uiState.update {
			it.copy(
				formEnabled = true,
				canDelete = true
			)
		}
	}

	private suspend fun searchAirportsDatabase(airportString: String) : List<Airport> {
		return (airportsRepository.searchAirportsByIataCode(airportString, 5))
	}
}