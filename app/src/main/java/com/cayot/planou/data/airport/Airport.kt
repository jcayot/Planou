package com.cayot.planou.data.airport

import android.location.Location
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "airports")
data class Airport(
	@PrimaryKey(autoGenerate = true)
	val id: Int,
	val name: String,
	val isoCountry: String,
	val isoRegion: String,
	val municipality: String,
	val iataCode: String,
	val latitude: Double,
	val longitude: Double
) {

	override fun toString(): String {
		return (name)
	}

	companion object {

		fun getJFK() : Airport {
			return (Airport(
				id = 0,
				name = "John F Kennedy International Airport",
				isoCountry = "US",
				isoRegion = "US-NY",
				municipality = "New York",
				iataCode = "JFK",
				latitude = 40.639447,
				longitude = -73.779317
			))
		}

		fun getCDG() : Airport {
			return (Airport(
				id = 0,
				name = "Charles de Gaulle International Airport",
				isoCountry = "FR",
				isoRegion = "FR-IDF",
				municipality = "Paris (Roissy-en-France, Val-d'Oise)",
				iataCode = "CDG",
				latitude = 49.012798,
				longitude = 2.55
			))
		}

		fun getHEL() : Airport {
			return (Airport(
				id = 0,
				name = "Helsinki Vantaa Airport",
				isoCountry = "FI",
				isoRegion = "FI-18",
				municipality = "Helsinki",
				iataCode = "HEL",
				latitude = 60.318363,
				longitude = 24.963341
			))
		}
	}
}

fun Airport.distanceToAirport(destination : Airport) : Float {
	val distance = FloatArray(1)
	Location.distanceBetween(latitude, longitude, destination.latitude, destination.longitude, distance)
	return (distance[0])
}
