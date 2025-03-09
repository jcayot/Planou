package com.cayot.flyingmore.data.local.model

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
	val longitude: Double,
	val timezone: String
) {

	override fun toString(): String {
		return (name)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other)
			return true
		if (javaClass != other?.javaClass)
			return false

		other as Airport

		if (name != other.name)
			return false
		if (isoCountry != other.isoCountry)
			return false
		if (isoRegion != other.isoRegion)
			return false
		if (municipality != other.municipality)
			return false
		if (iataCode != other.iataCode)
			return false
		if (latitude != other.latitude)
			return false
		if (longitude != other.longitude)
			return false

		return true
	}

	override fun hashCode(): Int {
		var result = name.hashCode()
		result = 31 * result + isoCountry.hashCode()
		result = 31 * result + isoRegion.hashCode()
		result = 31 * result + municipality.hashCode()
		result = 31 * result + iataCode.hashCode()
		result = 31 * result + latitude.hashCode()
		result = 31 * result + longitude.hashCode()
		return result
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
				longitude = -73.779317,
				timezone = "America/New_York"
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
				longitude = 2.55,
				timezone = "Europe/Paris"
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
				longitude = 24.963341,
				timezone = "Europe/Helsinki"
			))
		}
	}
}

fun Airport.distanceToAirport(destination : Airport) : Float {
	val distance = FloatArray(1)
	Location.distanceBetween(latitude, longitude, destination.latitude, destination.longitude, distance)
	return (distance[0])
}
