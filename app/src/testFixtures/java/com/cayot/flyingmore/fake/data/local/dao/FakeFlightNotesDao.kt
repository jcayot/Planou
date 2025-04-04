package com.cayot.flyingmore.fake.data.local.dao

import com.cayot.flyingmore.data.local.dao.FlightNotesDao
import com.cayot.flyingmore.data.local.model.FlightNotes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeFlightNotesDao : FlightNotesDao {
    private val _flightNotes = MutableStateFlow<MutableMap<Int, FlightNotes>>(mutableMapOf())

    override suspend fun insert(flightNotes: FlightNotes) {
        _flightNotes.update {
            it.apply {
                this[flightNotes.flightId] = flightNotes
            }
        }
    }

    override suspend fun update(flightNotes: FlightNotes) {
        _flightNotes.update {
            it.apply {
                this[flightNotes.flightId] = flightNotes
            }
        }
    }

    override suspend fun deleteById(flightId: Int) {
        _flightNotes.update {
            it.apply {
                remove(flightId)
            }
        }
    }

    override suspend fun getFromFlight(flightId: Int): FlightNotes? {
        return _flightNotes.value[flightId]
    }
}