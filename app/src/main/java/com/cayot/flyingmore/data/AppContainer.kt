package com.cayot.flyingmore.data

import android.content.Context
import com.cayot.flyingmore.data.airport.AirportsRepository
import com.cayot.flyingmore.data.airport.OfflineAirportsRepository
import com.cayot.flyingmore.data.flight.FlightsRepository
import com.cayot.flyingmore.data.flight.OfflineFlightsRepository
import com.cayot.flyingmore.data.flightNotes.FlightNotesRepository
import com.cayot.flyingmore.data.flightNotes.OfflineFlightNotesRepository

interface AppContainer {
	val flightsRepository: FlightsRepository
	val airportsRepository: AirportsRepository
	val flightNotesRepository: FlightNotesRepository
	val imageRepository: ImageRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
	override val flightsRepository: FlightsRepository by lazy {
		OfflineFlightsRepository(PlanouDatabase.getDatabase(context).flightDao())
	}
	override val airportsRepository: AirportsRepository by lazy {
		OfflineAirportsRepository(PlanouDatabase.getDatabase(context).airportDao())
	}
	override val flightNotesRepository: FlightNotesRepository by lazy {
		OfflineFlightNotesRepository(PlanouDatabase.getDatabase(context).flightNotesDao())
	}
	override val imageRepository: ImageRepository by lazy {
		ImageRepository(context)
	}
}
