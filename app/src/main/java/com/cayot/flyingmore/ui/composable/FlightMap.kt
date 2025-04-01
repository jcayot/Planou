package com.cayot.flyingmore.ui.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.cayot.flyingmore.data.local.model.Airport
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

@Composable
fun FlightMap(
	originAirport: Airport,
	destinationAirport: Airport,
	modifier: Modifier = Modifier,
	padding: Int = 0
) {
	val originLatLng = LatLng(originAirport.latitude, originAirport.longitude)
	val destLatLng = LatLng(destinationAirport.latitude, destinationAirport.longitude)
	val mapBounds = LatLngBounds.builder().include(originLatLng).include(destLatLng).build()

	val cameraPositionState = rememberCameraPositionState {
		position = CameraPosition.fromLatLngZoom(mapBounds.center, 1f)
	}
	val originMarkerState = rememberUpdatedMarkerState(position = originLatLng)
	val destinationMarkerState = rememberUpdatedMarkerState(position = destLatLng)

	LaunchedEffect(mapBounds) {
		cameraPositionState.animate(
			CameraUpdateFactory.newLatLngBounds(mapBounds, padding)
		)
	}

	GoogleMap(
		cameraPositionState = cameraPositionState,
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
			state = originMarkerState
		)
		Marker(
			state = destinationMarkerState
		)
	}
}
