package com.cayot.flyingmore.fake.data.local.model

import com.cayot.flyingmore.data.local.model.FlightEntity
import com.cayot.flyingmore.data.model.TravelClass
import kotlin.random.Random

fun generateFakeFlightEntity(
    number: Int,
    departureTimeRange: Pair<Long, Long> = System.nanoTime() to System.nanoTime(),
    arrivalTimeRange: Pair<Long, Long>? = null
    ) : List<FlightEntity> {
    val flights = mutableListOf<FlightEntity>()

    for (i in 1..number) {
        flights.add(FlightEntity(
            flightNumber = "AF$i",
            airline = "Air France",
            originAirportId = 1,
            destinationAirportId = 2,
            distance = 1000f,
            travelClass = TravelClass.ECONOMY,
            planeModel = "Airbus A300",
            departureTime = Random.nextLong(departureTimeRange.first, departureTimeRange.second),
            arrivalTime = arrivalTimeRange?.let { Random.nextLong(it.first, it.second) },
            seatNumber = "21A"
        ))
    }
    return flights
}
