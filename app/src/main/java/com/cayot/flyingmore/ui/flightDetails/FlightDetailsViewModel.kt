package com.cayot.flyingmore.ui.flightDetails

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cayot.flyingmore.data.FlightMapState
import com.cayot.flyingmore.data.ImageRepository
import com.cayot.flyingmore.data.flight.FlightDetails
import com.cayot.flyingmore.data.flight.FlightsRepository
import com.cayot.flyingmore.data.flightNotes.FlightNotes
import com.cayot.flyingmore.data.flightNotes.FlightNotesRepository
import com.cayot.flyingmore.ui.navigation.FlyingMoreScreen
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FlightDetailsViewModel(
	private val flightsRepository: FlightsRepository,
	private val flightNotesRepository: FlightNotesRepository,
	private val imageRepository: ImageRepository,
	savedStateHandle: SavedStateHandle
) : ViewModel() {

	private val flightId: Int = checkNotNull(savedStateHandle[FlyingMoreScreen.Details.argName])

	private val _uiState = MutableStateFlow(FlightDetailsUIState())
	val uiState :StateFlow<FlightDetailsUIState> = _uiState

	private val _shareCard = MutableSharedFlow<Uri>()
	val shareCard: SharedFlow<Uri> = _shareCard

	private val _navigateBack = MutableSharedFlow<Unit>()
	val navigateBack: SharedFlow<Unit> = _navigateBack

	private var flightNotesAccessJob: Job? = null

	init {
		viewModelScope.launch {
			flightsRepository.getFlight(flightId).collect { flight ->
				if (flight != null)
					onFlightRetrieved(flight = flight)
				else
					_navigateBack.emit(Unit)
			}
		}
	}

	fun updateNotesVisibility() {
		_uiState.update {
			it.copy(notesVisible = !_uiState.value.notesVisible)
		}
	}

	fun onFlightNotesTextChange(text: String) {
		updateFlightNotesUIState(_uiState.value.flight!!.flightNotes!!.copy(text = text))
	}

	fun editNotes() {
		if (flightNotesAccessJob == null || flightNotesAccessJob?.isActive == false) {
			if (_uiState.value.flight!!.flightNotes == null)
				flightNotesAccessJob = createFlightNotesDatabase()
			_uiState.update { it.copy(notesEdition = true) }
		}
	}

	fun discardFlightNotesChanges() {
		if (flightNotesAccessJob == null || flightNotesAccessJob?.isActive == false) {
			flightNotesAccessJob = getNotesDatabase(flightId)
			_uiState.update {
				it.copy(notesEdition = false)
			}
		}
	}

	fun saveFlightNotes() {
		if (flightNotesAccessJob == null || flightNotesAccessJob?.isActive == false) {
			flightNotesAccessJob = updateFlightNotesDatabase(_uiState.value.flight!!.flightNotes!!)
			_uiState.update {
				it.copy(notesEdition = false)
			}
		}
	}

	fun deleteFlightNotes() {
		if (flightNotesAccessJob == null || flightNotesAccessJob?.isActive == false) {
			flightNotesAccessJob = deleteFlightNotesDatabase(flightId)
			updateFlightNotesUIState(null)
			_uiState.update { it.copy(notesEdition = false) }
		}
	}

	fun shareFlightCard(flightCard: Bitmap) {
		viewModelScope.launch {
			val cardImageUri = imageRepository.saveBitmapToCache(flightCard)
			if (cardImageUri != null)
				_shareCard.emit(cardImageUri)
		}
	}

	private fun updateFlightNotesUIState(flightNotes: FlightNotes?) {
		_uiState.update {
			it.copy(flight = it.flight!!.copy(flightNotes = flightNotes))
		}
	}

	private	fun onFlightRetrieved(flight: FlightDetails) {
		val flightMapState = FlightMapState.fromAirports(flight.originAirport, flight.destinationAirport)

		_uiState.update {
			it.copy(flight = flight,
				flightMapState = flightMapState)
		}
	}

	private fun createFlightNotesDatabase()  = viewModelScope.launch {
		val newFlightNotes = FlightNotes(flightId, "")
		flightNotesRepository.insertFlightNotes(newFlightNotes)
		updateFlightNotesUIState(flightNotes = newFlightNotes)
	}

	private fun updateFlightNotesDatabase(flightNotes: FlightNotes) = viewModelScope.launch {
		flightNotesRepository.updateFlightNotes(flightNotes)
	}

	private fun deleteFlightNotesDatabase(flightId: Int) = viewModelScope.launch {
		flightNotesRepository.removeFlightNotesById(flightId)
	}

	private fun getNotesDatabase(flightId: Int) = viewModelScope.launch {
		val flightNotes = flightNotesRepository.getFromFlight(flightId)
		updateFlightNotesUIState(flightNotes = flightNotes)
	}
}