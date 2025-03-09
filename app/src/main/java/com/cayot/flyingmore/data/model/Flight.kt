package com.cayot.flyingmore.data.model

import androidx.compose.ui.util.fastRoundToInt
import androidx.room.Embedded
import androidx.room.Relation
import com.cayot.flyingmore.data.local.model.Airport
import com.cayot.flyingmore.data.local.model.FlightEntity
import com.cayot.flyingmore.data.local.model.FlightNotes
import com.cayot.flyingmore.domain.ConvertUtcTimeToLocalCalendarUseCase
import com.cayot.flyingmore.ui.flight.edit.FlightForm
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

data class Flight(
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
        fun getPlaceholder1() : Flight {
            return (Flight(
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

    //TODO FUCKING RETARDED
    fun getDepartureArrivalDayDifference() : Int {
        if (arrivalTime == null) {
            return 0
        }
        val departureYear = departureTime.get(Calendar.YEAR)
        val departureMonth = departureTime.get(Calendar.MONTH)
        val departureDay = departureTime.get(Calendar.DAY_OF_MONTH)

        val arrivalYear = arrivalTime.get(Calendar.YEAR)
        val arrivalMonth = arrivalTime.get(Calendar.MONTH)
        val arrivalDay = arrivalTime.get(Calendar.DAY_OF_MONTH)

        var dayDifference = 0
        val departureCalendar = Calendar.getInstance().apply {
            set(departureYear, departureMonth, departureDay)
        }
        val arrivalCalendar = Calendar.getInstance().apply {
            set(arrivalYear, arrivalMonth, arrivalDay)
        }

        while (departureCalendar.before(arrivalCalendar)) {
            departureCalendar.add(Calendar.DAY_OF_YEAR, 1)
            dayDifference++
        }
        return (dayDifference)
    }
}

fun Flight.getDistanceString() : String {
    return ((distance / 1000.0f).fastRoundToInt().toString() + " km")
}

fun Flight.getDepartureDateString() : String {
    val dateFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone(originAirport.timezone)
    }

    return (dateFormat.format(departureTime.time))
}

fun Flight.getDepartureTimeString() : String {
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone(originAirport.timezone)
    }

    return (timeFormat.format(departureTime.time))
}

fun Flight.getArrivalTimeString() : String? {
    if (arrivalTime == null) {
        return null
    }

    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone(destinationAirport.timezone)
    }

    val dayDifference = getDepartureArrivalDayDifference()

    val dayDiffSuffix = if (dayDifference < 1) "" else " + $dayDifference"

    val formattedArrivalTime = timeFormat.format(arrivalTime.time)

    return (formattedArrivalTime + dayDiffSuffix)
}

fun Flight.toFlightForm(
    convertUtcTimeToLocalCalendarUseCase: ConvertUtcTimeToLocalCalendarUseCase = ConvertUtcTimeToLocalCalendarUseCase()
) : FlightForm {
    val departureLocalCalendar = convertUtcTimeToLocalCalendarUseCase(departureTime.timeInMillis, originAirport.timezone)

    val arrivalLocalCalendar = arrivalTime?.let {
        convertUtcTimeToLocalCalendarUseCase(it.timeInMillis, destinationAirport.timezone)
    }

    val departureCopy = departureLocalCalendar.clone() as Calendar
    departureCopy.apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }


    val dayDifference = arrivalLocalCalendar?.let {
        val diffInDays = getDepartureArrivalDayDifference()
        when (diffInDays) {
            0 -> DayDifference.ZERO
            1 -> DayDifference.ONE
            2 -> DayDifference.TWO
            else -> {
                DayDifference.ZERO
            }
        }
    } ?: DayDifference.ZERO

    return FlightForm(
        id = id,
        flightNumber = flightNumber,
        airline = airline,
        originAirportString = originAirport.iataCode,
        destinationAirportString = destinationAirport.iataCode,
        departureDate = departureCopy.timeInMillis,
        departureHour = departureLocalCalendar.get(Calendar.HOUR_OF_DAY),
        departureMinute = departureLocalCalendar.get(Calendar.MINUTE),
        dayDifference = dayDifference,
        arrivalHour = arrivalLocalCalendar?.get(Calendar.HOUR_OF_DAY),
        arrivalMinute = arrivalLocalCalendar?.get(Calendar.MINUTE),
        travelClass = travelClass,
        planeModel = planeModel,
        seatNumber = seatNumber ?: ""
    )
}

fun Flight.toFlightEntity() : FlightEntity {
    return FlightEntity(
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
    val flightEntity: FlightEntity,
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

fun FlightDetailsPOJO.toFlightDetails(
    convertUtcTimeToLocalCalendarUseCase: ConvertUtcTimeToLocalCalendarUseCase = ConvertUtcTimeToLocalCalendarUseCase()
) : Flight {
    return (Flight(
        id = flightEntity.id,
        flightNumber = flightEntity.flightNumber,
        originAirport = originAirport,
        destinationAirport = destinationAirport,
        airline = flightEntity.airline,
        distance = flightEntity.distance,
        travelClass = flightEntity.travelClass,
        planeModel = flightEntity.planeModel,
        departureTime = convertUtcTimeToLocalCalendarUseCase(flightEntity.departureTime, originAirport.timezone),
        arrivalTime = flightEntity.arrivalTime?.let { convertUtcTimeToLocalCalendarUseCase(flightEntity.arrivalTime, destinationAirport.timezone)},
        seatNumber = flightEntity.seatNumber,
        flightNotes = flightNotes
    ))
}