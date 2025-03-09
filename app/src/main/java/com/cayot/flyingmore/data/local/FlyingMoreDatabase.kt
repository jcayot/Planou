package com.cayot.flyingmore.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cayot.flyingmore.data.local.dao.AirportDao
import com.cayot.flyingmore.data.local.dao.FlightDao
import com.cayot.flyingmore.data.local.dao.FlightNotesDao
import com.cayot.flyingmore.data.local.dao.FlyingStatisticsDao
import com.cayot.flyingmore.data.local.model.Airport
import com.cayot.flyingmore.data.local.model.FlightEntity
import com.cayot.flyingmore.data.local.model.FlightNotes
import com.cayot.flyingmore.data.local.model.FlyingStatisticEntity

@Database(entities = [FlightEntity::class, Airport::class, FlightNotes::class, FlyingStatisticEntity::class], version = 1, exportSchema = false)
abstract class FlyingMoreDatabase : RoomDatabase() {

	abstract fun flightDao() : FlightDao
	abstract fun airportDao() : AirportDao
	abstract fun flightNotesDao() : FlightNotesDao
	abstract fun flyingStatisticsDao() : FlyingStatisticsDao

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
