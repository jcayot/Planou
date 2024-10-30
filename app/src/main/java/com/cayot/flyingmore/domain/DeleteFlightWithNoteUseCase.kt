package com.cayot.flyingmore.domain

import com.cayot.flyingmore.data.flight.FlightsRepository
import com.cayot.flyingmore.data.flightNotes.FlightNotesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteFlightWithNoteUseCase(
    private val flightsRepository: FlightsRepository,
    private val flightNotesRepository: FlightNotesRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend operator fun invoke(id: Int) = withContext(defaultDispatcher) {
        flightsRepository.deleteFlightById(id)
        flightNotesRepository.removeFlightNotesById(id)
    }
}
