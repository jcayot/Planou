package com.cayot.flyingmore.data

import android.content.Context
import com.cayot.flyingmore.data.local.FlyingMoreDatabase
import com.cayot.flyingmore.data.repository.AirportsRepository
import com.cayot.flyingmore.data.local.repository.OfflineAirportsRepository
import com.cayot.flyingmore.data.repository.FlightsRepository
import com.cayot.flyingmore.data.local.repository.OfflineFlightsRepository
import com.cayot.flyingmore.data.repository.FlightNotesRepository
import com.cayot.flyingmore.data.local.repository.OfflineFlightNotesRepository
import com.cayot.flyingmore.data.local.repository.OfflineFlyingStatisticsRepository
import com.cayot.flyingmore.data.repository.FlyingStatisticsRepository
import com.cayot.flyingmore.data.repository.ImageRepository

interface AppContainer {
	val flightsRepository: FlightsRepository
	val airportsRepository: AirportsRepository
	val flightNotesRepository: FlightNotesRepository
	val flyingStatisticsRepository: FlyingStatisticsRepository
	val imageRepository: ImageRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
	override val flightsRepository: FlightsRepository by lazy {
		OfflineFlightsRepository(FlyingMoreDatabase.getDatabase(context).flightDao())
	}
	override val airportsRepository: AirportsRepository by lazy {
		OfflineAirportsRepository(FlyingMoreDatabase.getDatabase(context).airportDao())
	}
	override val flightNotesRepository: FlightNotesRepository by lazy {
		OfflineFlightNotesRepository(FlyingMoreDatabase.getDatabase(context).flightNotesDao())
	}
	override val flyingStatisticsRepository: FlyingStatisticsRepository by lazy {
		OfflineFlyingStatisticsRepository(FlyingMoreDatabase.getDatabase(context).flyingStatisticsDao())
	}
	override val imageRepository: ImageRepository by lazy {
        ImageRepository(context)
	}
}
