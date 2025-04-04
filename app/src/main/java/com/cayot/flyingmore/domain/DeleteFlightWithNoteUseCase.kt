package com.cayot.flyingmore.domain

import com.cayot.flyingmore.data.repository.FlightRepository
import com.cayot.flyingmore.data.repository.FlightNotesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteFlightWithNoteUseCase(
    private val flightRepository: FlightRepository,
    private val flightNotesRepository: FlightNotesRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend operator fun invoke(id: Int) = withContext(defaultDispatcher) {
        flightRepository.deleteFlightById(id)
        flightNotesRepository.removeFlightNotesById(id)
    }
}
