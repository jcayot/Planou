package com.cayot.flyingmore.data.flight

import androidx.compose.ui.util.fastRoundToInt
import androidx.room.Embedded
import androidx.room.Relation
import com.cayot.flyingmore.data.TravelClass
import com.cayot.flyingmore.data.airport.Airport
import com.cayot.flyingmore.data.flightNotes.FlightNotes
import com.cayot.flyingmore.ui.flightEdit.FlightForm
import java.text.SimpleDateFormat
import java.time.temporal.ChronoUnit
import java.util.Calendar
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
    val departureTime: Calendar,
    val	arrivalTime: Calendar?,
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
                departureTime = Calendar.getInstance(),
                arrivalTime = Calendar.getInstance(),
                seatNumber = "33D"
            ))
        }
    }
}

fun FlightDetails.getDistanceString() : String {
    return ((distance / 1000.0f).fastRoundToInt().toString() + " km")
}

fun FlightDetails.getDepartureDateString() : String {
    return (SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(departureTime.time))
}

fun FlightDetails.getDepartureTimeString() : String {
    return (SimpleDateFormat("h:mm a", Locale.getDefault()).format(departureTime.time))
}

fun FlightDetails.getArrivalTimeString() : String? {
    if (arrivalTime == null) {
        return (null)
    }

    val dayDifference = ChronoUnit.DAYS.between(departureTime.toInstant(), arrivalTime.toInstant())

    val dayDiffSuffix = if (dayDifference < 1) "" else " + $dayDifference"

    val formattedArrivalTime = SimpleDateFormat("h:mm a", Locale.getDefault()).format(arrivalTime.time)

    return (formattedArrivalTime + dayDiffSuffix)
}

fun FlightDetails.toFlightForm() : FlightForm {
    return (FlightForm(
        id = id,
        flightNumber = flightNumber,
        airline = airline,
        originAirportString = originAirport.iataCode,
        destinationAirportString = destinationAirport.iataCode,
        departureTime = departureTime.toInstant(),
        arrivalTime = arrivalTime?.let { arrivalTime.toInstant() },
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
        departureTime = Calendar.getInstance().apply { timeInMillis = flightApiModel.departureTime },
        arrivalTime = flightApiModel.arrivalTime?.let { Calendar.getInstance().apply { timeInMillis = flightApiModel.arrivalTime }},
        seatNumber = flightApiModel.seatNumber,
        flightNotes = flightNotes
    ))
}