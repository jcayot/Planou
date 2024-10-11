package com.cayot.planou.ui.flightList

import androidx.compose.foundation.lazy.LazyListState
import com.cayot.planou.data.FlightMapState
import com.cayot.planou.data.flight.Flight

data class FlightListUIState(
	val flightItemList: List<FlightItem> = emptyList(),
	val listState: LazyListState? = null
)

data class FlightItem(
	val flight: Flight,
	val flightMapState: FlightMapState? = null
)