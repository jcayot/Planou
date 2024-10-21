package com.cayot.flyingmore.data

import com.cayot.flyingmore.data.airport.Airport
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MarkerState

data class FlightMapState (
	val cameraPositionState: CameraPositionState,
	val mapBounds: LatLngBounds,
	val originMarkerState: MarkerState,
	val destinationMarkerState: MarkerState,
) {
	companion object {

		fun fromAirports(
			originAirport: Airport,
			destinationAirport: Airport
		) : FlightMapState {
			val originLatLng = LatLng(originAirport.latitude, originAirport.longitude)
			val destLatLng = LatLng(destinationAirport.latitude, destinationAirport.longitude)

			val mapBounds = LatLngBounds.builder()
				.include(originLatLng)
				.include(destLatLng)
				.build()
			val cameraPositionState = CameraPositionState(
				position = CameraPosition.fromLatLngZoom(mapBounds.center, 1f)
			)
			val originMarkerState = MarkerState(originLatLng)
			val destinationMarkerState = MarkerState(destLatLng)

			return (FlightMapState(
				cameraPositionState = cameraPositionState,
				mapBounds = mapBounds,
				originMarkerState = originMarkerState,
				destinationMarkerState = destinationMarkerState
			))
		}
	}
}