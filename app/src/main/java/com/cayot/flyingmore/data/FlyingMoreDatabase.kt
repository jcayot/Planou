package com.cayot.flyingmore.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cayot.flyingmore.data.airport.Airport
import com.cayot.flyingmore.data.airport.AirportDao
import com.cayot.flyingmore.data.flight.Flight
import com.cayot.flyingmore.data.flight.FlightDao
import com.cayot.flyingmore.data.flightNotes.FlightNotes
import com.cayot.flyingmore.data.flightNotes.FlightNotesDao

@Database(entities = [Flight::class, Airport::class, FlightNotes::class], version = 1, exportSchema = false)
abstract class FlyingMoreDatabase : RoomDatabase() {

	abstract fun flightDao() : FlightDao
	abstract fun airportDao() : AirportDao
	abstract fun flightNotesDao() : FlightNotesDao

	companion object {
		@Volatile
		private var Instance: FlyingMoreDatabase? = null

		fun getDatabase(context: Context) : FlyingMoreDatabase {
			return Instance ?: synchronized(this) {
				Room.databaseBuilder(context, FlyingMoreDatabase::class.java, "planou_database")
					.createFromAsset("airports.db")
					.build()
					.also { Instance = it }
			}
		}
	}
}
