package com.cayot.planou.ui.flightDetails

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cayot.planou.data.FlightMapState
import com.cayot.planou.data.ImageRepository
import com.cayot.planou.data.airport.Airport
import com.cayot.planou.data.airport.AirportsRepository
import com.cayot.planou.data.flight.Flight
import com.cayot.planou.data.flight.FlightsRepository
import com.cayot.planou.data.flightNotes.FlightNotes
import com.cayot.planou.data.flightNotes.FlightNotesRepository
import com.cayot.planou.ui.navigation.PlanouScreen
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FlightDetailsViewModel(
	private val flightsRepository: FlightsRepository,
	private val airportsRepository: AirportsRepository,
	private val flightNotesRepository: FlightNotesRepository,
	private val imageRepository: ImageRepository,
	savedStateHandle: SavedStateHandle
) : ViewModel() {

	private val flightId: Int = checkNotNull(savedStateHandle[PlanouScreen.Details.argName])

	private val _uiState = MutableStateFlow(FlightDetailsUIState())
	val uiState :StateFlow<FlightDetailsUIState> = _uiState

	private val _shareCard = MutableSharedFlow<Uri>()
	val shareCard: SharedFlow<Uri> = _shareCard

	private lateinit var flightNotesAccessJob: Job

	init {
		viewModelScope.launch {
			val flight : Flight? = flightsRepository.getFlightStream(flightId)

			if (flight != null) {
				flightNotesAccessJob = getNotes(flightId)
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

	fun updateTopBarDropdownVisibility() {
		_uiState.update {
			it.copy(topBarDropdownExpanded = !_uiState.value.topBarDropdownExpanded)
		}
	}

	fun updateNotesVisibility() {
		_uiState.update {
			it.copy(notesVisible = !_uiState.value.notesVisible)
		}
	}

	fun onFlightNotesChange(flightNotes: FlightNotes) {
		_uiState.update {
			it.copy(flightNotes = flightNotes)
		}
	}

	fun editNotes() {
		if (!flightNotesAccessJob.isActive) {
			if (_uiState.value.flightNotes == null)
				flightNotesAccessJob = createFlightNotes()
			else
				_uiState.update { it.copy(notesEdition = true) }
		}
	}

	fun discardFlightNotesChanges() {
		if (!flightNotesAccessJob.isActive) {
			flightNotesAccessJob = getNotes(flightId)
			_uiState.update {
				it.copy(notesEdition = false)
			}
		}
	}

	fun saveFlightNotes() {
		if (!flightNotesAccessJob.isActive) {
			flightNotesAccessJob = updateFlightNotes(_uiState.value.flightNotes!!)
			_uiState.update {
				it.copy(notesEdition = false)
			}
		}
	}

	fun deleteFlightNotes() {
		if (!flightNotesAccessJob.isActive) {
			flightNotesAccessJob = deleteFlightNotes(_uiState.value.flightNotes!!)
			_uiState.update {
				it.copy(flightNotes = null)
			}
		}
	}

	fun shareFlightCard(flightCard: Bitmap) {
		viewModelScope.launch {
			val cardImageUri = imageRepository.saveBitmapToCache(flightCard)
			if (cardImageUri != null)
				_shareCard.emit(cardImageUri)
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

	private fun createFlightNotes()  = viewModelScope.launch {
		val newFlightNotes = FlightNotes(flightId, "")
		flightNotesRepository.insertFlightNotes(newFlightNotes)
		_uiState.update {
			it.copy(flightNotes = newFlightNotes,
				notesEdition = true)
		}
	}

	private fun updateFlightNotes(flightNotes: FlightNotes) = viewModelScope.launch {
		flightNotesRepository.updateFlightNotes(flightNotes)
	}

	private fun deleteFlightNotes(flightNotes: FlightNotes) = viewModelScope.launch {
		flightNotesRepository.removeFlightNotes(flightNotes)
	}

	private fun getNotes(flightId: Int) = viewModelScope.launch {
		val flightNotes = flightNotesRepository.getFromFlight(flightId)
		_uiState.update {
			it.copy(flightNotes = flightNotes)
		}
	}
}