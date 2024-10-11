package com.cayot.planou.ui.flightList

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cayot.planou.data.FlightMapState
import com.cayot.planou.data.airport.AirportsRepository
import com.cayot.planou.data.flight.FlightsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FlightListViewModel(
	private val flightsRepository : FlightsRepository,
	private val airportsRepository: AirportsRepository) : ViewModel() {

	private val _uiState = MutableStateFlow(FlightListUIState())
	val uiState: StateFlow<FlightListUIState> = _uiState

	init {
		viewModelScope.launch {
			flightsRepository.getAllFlightsStream()
				.map { flights ->
					val flightItems = flights.map { flight ->
						FlightItem(flight = flight, flightMapState = null)
					}
					FlightListUIState(
						flightItemList = flightItems,
						listState = LazyListState()
					)
				}.collect {
					_uiState.value = it
				}
		}
	}

	fun updateFlightMap(flightItem: FlightItem) {
		if (!flightItem.mapLoaded) {
			viewModelScope.launch {
				val originAirport = airportsRepository.getAirportByIataCode(flightItem.flight.originAirportCode)
				val destinationAirport = airportsRepository.getAirportByIataCode(flightItem.flight.destinationAirportCode)
				if (originAirport != null && destinationAirport != null) {
					val mapState = FlightMapState.fromAirports(originAirport, destinationAirport)

					val updatedFlightItemList = _uiState.value.flightItemList.map {
						if (it.flight.id == flightItem.flight.id)
							it.copy(
								flightMapState = mapState,
								mapLoaded = true
							)
						else
							it
					}
					_uiState.update {
						it.copy(
							flightItemList = updatedFlightItemList
						)
					}
				}
			}
		}
	}
}
