package com.cayot.flyingmore.data.flight

import androidx.compose.ui.util.fastRoundToInt
import androidx.room.Embedded
import androidx.room.Relation
import com.cayot.flyingmore.data.TravelClass
import com.cayot.flyingmore.data.airport.Airport
import com.cayot.flyingmore.data.flightNotes.FlightNotes
import com.cayot.flyingmore.ui.flightEdit.FlightForm
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class FlightDetails(
    val id: Int,
    val	flightNumber: String,
    val	airline: String,
    val originAirport: Airport,
    val	destinationAirport: Airport,
    val distance: Float,
    val travelClass: TravelClass,
    val planeModel: String,
    val departureTime: Date,
    val	arrivalTime: Date?,
    val seatNumber: String?,
    val flightNotes: FlightNotes? = null
) {
    companion object {
        fun getPlaceholder1() : FlightDetails {
            return (FlightDetails(
                id = 0,
                flightNumber = "AF001",
                airline = "Air France",
                originAirport = Airport.getCDG(),
                destinationAirport = Airport.getJFK(),
                distance = 6000000f,
                travelClass = TravelClass.ECONOMY,
                planeModel = "Airbus A350",
                departureTime = Date(),
                arrivalTime = Date(),
                seatNumber = "33D"
            ))
        }
    }
}

fun FlightDetails.getDistanceString() : String {
    return ((distance / 1000.0f).fastRoundToInt().toString() + " km")
}

fun FlightDetails.getDepartureDateString() : String {
    return (SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(departureTime))
}

fun FlightDetails.getDepartureTimeString() : String {
    return (SimpleDateFormat("h:mm a", Locale.getDefault()).format(departureTime))
}

fun FlightDetails.getArrivalTimeString() : String? {
    if (arrivalTime == null) {
        return null
    }

    val departureCalendar = Calendar.getInstance().apply { time = departureTime }
    val arrivalCalendar = Calendar.getInstance().apply { time = arrivalTime }

    var dayDifference = 0
    while (departureCalendar.before(arrivalCalendar)) {
        departureCalendar.add(Calendar.DAY_OF_YEAR, 1)
        dayDifference++
    }

    val dayDiffSuffix = if (dayDifference < 1) "" else "+ $dayDifference"

    val formattedArrivalTime = SimpleDateFormat("h:mm a", Locale.getDefault()).format(arrivalTime)

    return formattedArrivalTime + dayDiffSuffix
}

fun FlightDetails.toFlightForm() : FlightForm {
    return (FlightForm(
        id = id,
        flightNumber = flightNumber,
        airline = airline,
        originAirportString = originAirport.iataCode,
        destinationAirportString = destinationAirport.iataCode,
        departureTime = departureTime.toInstant(),
        arrivalTime = arrivalTime?.let { departureTime.toInstant() },
        travelClass = travelClass,
        planeModel = planeModel,
        seatNumber = seatNumber ?: ""
    ))
}

fun FlightDetails.toFlightApiModel() : FlightApiModel {
    return FlightApiModel(
        id = id,
        flightNumber = flightNumber,
        airline = airline,
        originAirportId = originAirport.id,
        destinationAirportId = destinationAirport.id,
        distance = distance,
        travelClass = travelClass,
        planeModel = planeModel,
        departureTime = departureTime.toInstant().toEpochMilli(),
        arrivalTime = arrivalTime?.toInstant()?.toEpochMilli(),
        seatNumber = seatNumber
    )
}

data class FlightDetailsPOJO(
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
    @Relation(
        parentColumn = "id",
        entityColumn = "flightId"
    )
    val flightNotes: FlightNotes?
)

fun FlightDetailsPOJO.toFlightDetails() : FlightDetails {
    return (FlightDetails(
        id = flightApiModel.id,
        flightNumber = flightApiModel.flightNumber,
        originAirport = originAirport,
        destinationAirport = destinationAirport,
        airline = flightApiModel.airline,
        distance = flightApiModel.distance,
        travelClass = flightApiModel.travelClass,
        planeModel = flightApiModel.planeModel,
        departureTime = Date.from(Instant.ofEpochMilli(flightApiModel.departureTime)),
        arrivalTime = flightApiModel.arrivalTime?.let { Date.from(Instant.ofEpochMilli(it)) },
        seatNumber = flightApiModel.seatNumber,
        flightNotes = flightNotes
    ))
}