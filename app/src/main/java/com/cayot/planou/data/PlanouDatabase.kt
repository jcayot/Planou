package com.cayot.planou.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cayot.planou.data.airport.Airport
import com.cayot.planou.data.airport.AirportDao
import com.cayot.planou.data.flight.Flight
import com.cayot.planou.data.flight.FlightDao

@Database(entities = [Flight::class, Airport::class], version = 1, exportSchema = false)
abstract class PlanouDatabase : RoomDatabase() {

	abstract fun flightDao() : FlightDao
	abstract fun airportDao() : AirportDao

	companion object {
		@Volatile
		private var Instance: PlanouDatabase? = null

		fun getDatabase(context: Context) : PlanouDatabase {
			return Instance ?: synchronized(this) {
				Room.databaseBuilder(context, PlanouDatabase::class.java, "planou_database")
					.createFromAsset("airports.db")
					.build()
					.also { Instance = it }
			}
		}
	}
}
