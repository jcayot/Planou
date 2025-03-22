package com.cayot.flyingmore.fake.data.model

import com.cayot.flyingmore.data.local.model.Airport
import com.cayot.flyingmore.data.model.Flight
import com.cayot.flyingmore.data.model.TravelClass
import java.util.Calendar
import kotlin.random.Random

fun generateFakeFlight(
    number: Int,
    departureTimeRange: Pair<Long, Long> = System.nanoTime() to System.nanoTime(),
    arrivalTimeRange: Pair<Long, Long>? = null
    ) : List<Flight> {
    val flights = mutableListOf<Flight>()

    for (i in 1..number) {
        val departureTime = Calendar.getInstance()
        departureTime.timeInMillis = Random.nextLong(departureTimeRange.first, departureTimeRange.second)
        val arrivalTime = arrivalTimeRange?.let { range ->
            Calendar.getInstance().apply {
                timeInMillis = Random.nextLong(range.first, range.second)
            }
        }
        flights.add(Flight(
            id = i,
            flightNumber = "AF$i",
            airline = "Air France",
            originAirport = Airport.getCDG(),
            destinationAirport = Airport.getHEL(),
            distance = 1000f,
            travelClass = TravelClass.ECONOMY,
            planeModel = "Airbus A300",
            departureTime = departureTime,
            arrivalTime = arrivalTime,
            seatNumber = "21A"
        ))
    }

    return flights
}
