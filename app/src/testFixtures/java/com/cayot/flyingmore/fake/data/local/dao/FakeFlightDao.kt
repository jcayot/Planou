package com.cayot.flyingmore.fake.data.local.dao

import com.cayot.flyingmore.data.local.dao.FlightDao
import com.cayot.flyingmore.data.local.model.Airport
import com.cayot.flyingmore.data.local.model.FlightEntity
import com.cayot.flyingmore.data.local.model.FlightNotes
import com.cayot.flyingmore.data.model.FlightBriefPOJO
import com.cayot.flyingmore.data.model.FlightDetailsPOJO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlin.collections.plus

class FakeFlightDao : FlightDao {
    private val _flights = MutableStateFlow<List<FlightEntity>>(emptyList())
    private val flights: Flow<List<FlightEntity>> = _flights

    override suspend fun insert(flight: FlightEntity) {
        _flights.update {
            it + flight.copy(id = (it.maxOfOrNull { it.id } ?: 0) + 1)
        }
    }

    override suspend fun update(flight: FlightEntity) {
        _flights.update { list ->
            list.map {
                if (it.id == flight.id)
                    flight
                else
                    it
            }
        }
    }

    override suspend fun deleteById(id: Int) {
        _flights.update { list ->
            list.filterNot { it.id == id }
        }
    }

    override fun getFlightDetails(flightId: Int): Flow<FlightDetailsPOJO> {
        return flights.map { list ->
            list.first { it.id == flightId }.toFlightDetailsPOJO()
        }
    }

    override fun getAllFlightForDepartureTimeRange(
        startTime: Long,
        endTime: Long
    ): List<FlightDetailsPOJO> {
        return _flights.value.filter {
            it.departureTime >= startTime && it.departureTime < endTime
        }.map { it.toFlightDetailsPOJO() }
    }

    override fun getAllFlightBriefs(): Flow<List<FlightBriefPOJO>> {
        return flights.map { list ->
            list.map { it.toFlightBriefPOJO() }
        }
    }

    private fun FlightEntity.toFlightDetailsPOJO(
        originAirport: Airport = Airport.getCDG(),
        destinationAirport: Airport = Airport.getJFK(),
        flightNotes: FlightNotes? = null
    ): FlightDetailsPOJO = FlightDetailsPOJO(
        flightEntity = this,
        originAirport = originAirport,
        destinationAirport = destinationAirport,
        flightNotes = flightNotes
    )

    private fun FlightEntity.toFlightBriefPOJO(
        originAirport: Airport = Airport.getCDG(),
        destinationAirport: Airport = Airport.getJFK(),
    ): FlightBriefPOJO = FlightBriefPOJO(
        flightEntity = this,
        originAirport = originAirport,
        destinationAirport = destinationAirport
    )
}