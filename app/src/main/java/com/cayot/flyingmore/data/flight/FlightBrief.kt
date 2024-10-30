package com.cayot.flyingmore.data.flight

import androidx.compose.ui.util.fastRoundToInt
import androidx.room.Embedded
import androidx.room.Relation
import com.cayot.flyingmore.data.airport.Airport
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale

data class FlightBrief(
    val id: Int,
    val	flightNumber: String,
    val	airline: String,
    val originAirport: Airport,
    val destinationAirport: Airport,
    val distance: Float,
    val departureTime: Date
) {
    companion object {
        fun getPlaceholder1() : FlightBrief {
            return FlightBrief(
                id = 0,
                flightNumber = "AF001",
                airline = "Air France",
                originAirport = Airport.getCDG(),
                destinationAirport = Airport.getJFK(),
                distance = 6000000f,
                departureTime = Date()
            )
        }
    }
}

fun FlightBrief.getDistanceString() : String {
    return ((distance / 1000.0f).fastRoundToInt().toString() + " km")
}

fun FlightBrief.getDepartureDateString() : String {
    return (SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(departureTime))
}

data class FlightBriefPOJO(
    @Embedded
    val flightApiModel: FlightApiModel,
    @Relation(
        parentColumn = "originAirportId",
        entityColumn = "id"
    )
    val originAirport: Airport,
    @Relation(
        parentColumn = "destinationAirportId",
        entityColumn = "id"
    )
    val	destinationAirport: Airport,
)

fun FlightBriefPOJO.toFlightBrief() : FlightBrief {
    return (FlightBrief(
        id = flightApiModel.id,
        flightNumber = flightApiModel.flightNumber,
        airline = flightApiModel.airline,
        originAirport = originAirport,
        destinationAirport = destinationAirport,
        departureTime = Date.from(Instant.ofEpochMilli(flightApiModel.departureTime)),
        distance = flightApiModel.distance
    ))
}