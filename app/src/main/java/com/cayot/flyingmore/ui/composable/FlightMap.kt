package com.cayot.flyingmore.ui.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.cayot.flyingmore.data.FlightMapState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker

@Composable
fun FlightMap(
	flightMapState: FlightMapState,
	padding: Int,
	modifier: Modifier = Modifier
) {
	LaunchedEffect(flightMapState.mapBounds) {
		flightMapState.cameraPositionState.animate(
			CameraUpdateFactory.newLatLngBounds(flightMapState.mapBounds, padding)
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
		Marker(
			state = flightMapState.originMarkerState
		)
		Marker(
			state = flightMapState.destinationMarkerState
		)
	}
}
