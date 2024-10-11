package com.cayot.planou.ui.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.cayot.planou.data.FlightMapState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.AdvancedMarker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings

@Composable
fun FlightMap(
	flightMapState: FlightMapState,
	modifier: Modifier = Modifier
) {
	LaunchedEffect(flightMapState.mapBounds) {
		flightMapState.cameraPositionState.animate(
			CameraUpdateFactory.newLatLngBounds(flightMapState.mapBounds, 100)
		)
	}
	GoogleMap(
		cameraPositionState = flightMapState.cameraPositionState,
		properties = MapProperties(
			minZoomPreference = 1.0f
		),
		uiSettings = MapUiSettings(
			compassEnabled = false,
			indoorLevelPickerEnabled = false,
			mapToolbarEnabled = false,
			myLocationButtonEnabled = false,
			rotationGesturesEnabled = false,
			scrollGesturesEnabled = false,
			scrollGesturesEnabledDuringRotateOrZoom = false,
			tiltGesturesEnabled = false,
			zoomControlsEnabled = false,
			zoomGesturesEnabled = false
		),
		modifier = modifier.fillMaxSize(),
		) {
		AdvancedMarker(
			state = flightMapState.originMarkerState
		)
		AdvancedMarker(
			state = flightMapState.destinationMarkerState
		)
	}
}
