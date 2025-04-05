package com.cayot.flyingmore.data.model.statistics.enums

import androidx.annotation.StringRes
import com.cayot.flyingmore.R
import com.cayot.flyingmore.data.model.Flight
import kotlin.math.roundToInt

enum class FlyingStatistic(
    @StringRes val displayNameResource: Int = R.string.statistic_name,
    val dataTimeFrame: TimeFrame = TimeFrame.YEAR,
    val dataResolution: TimeFrame = TimeFrame.DAY,
    val briefDisplayResolution: TimeFrame = TimeFrame.MONTH,
    val defaultDisplayResolution: TimeFrame = TimeFrame.MONTH,
    val allowedDisplayResolutions: List<TimeFrame> = TimeFrame.entries,
    val dataType: ListDataType = ListDataType.INT,
    val chartType: ChartType = ChartType.BAR_GRAPH,
    @StringRes val unitResource: Int = R.string.unit
) {
    NUMBER_OF_FLIGHT(
        displayNameResource = R.string.number_of_flights,
        unitResource = R.string.flights
    ),
    FLOWN_DISTANCE(
        displayNameResource = R.string.flown_distance,
        unitResource = R.string.km
    ),
    AIRPORT_VISIT_NUMBER(
        displayNameResource = R.string.airports_visits,
        briefDisplayResolution = TimeFrame.YEAR,
        dataType = ListDataType.MAP_STRING_INT,
        unitResource = R.string.visits
    );

    //Merge into one ?
    companion object {
        fun <T> flightAggregator(flyingStatistic: FlyingStatistic) : (T, Flight) -> T {
            return when (flyingStatistic) {
                NUMBER_OF_FLIGHT -> { currentNumber: Int, flight: Flight ->
                    (currentNumber + 1)
                }
                FLOWN_DISTANCE -> { currentDistance: Int, flight: Flight ->
                    (currentDistance + flight.distance.roundToInt() / 1000)
                }
                AIRPORT_VISIT_NUMBER -> { currentCodeVisitMap: MutableMap<String, Int>, flight: Flight ->
                    currentCodeVisitMap.merge(flight.originAirport.iataCode, 1) {
                        previous, new -> previous + new
                    }
                    currentCodeVisitMap.merge(flight.destinationAirport.iataCode, 1) {
                        previous, new -> previous + new
                    }
                    currentCodeVisitMap
                }
            } as (T, Flight) -> T
        }

        fun <T> flightRemover(flyingStatistic: FlyingStatistic) : (T, Flight) -> T {
            return when (flyingStatistic) {
                NUMBER_OF_FLIGHT -> { currentNumber: Int, flight: Flight ->
                    if (currentNumber == 0)
                            throw IllegalStateException("Removing flight from empty statistic")
                    (currentNumber - 1)
                }
                FLOWN_DISTANCE -> { currentDistance: Int, flight: Flight ->
                    val newDistance = currentDistance - flight.distance.roundToInt() / 1000
                    if (newDistance < 0)
                        throw IllegalStateException("Removing more distance than flown")
                    (newDistance)
                }
                AIRPORT_VISIT_NUMBER -> { currentCodeVisitMap: MutableMap<String, Int>, flight: Flight ->
                    if (currentCodeVisitMap.containsKey(flight.originAirport.iataCode).not() ||
                        currentCodeVisitMap.containsKey(flight.destinationAirport.iataCode).not())
                        throw IllegalStateException("Removing visit from unvisited airport")

                    currentCodeVisitMap.merge(flight.originAirport.iataCode, 1) { previous, new ->
                        val newValue = previous - new

                        if (newValue == 0)
                            null
                        else
                            newValue
                    }
                    currentCodeVisitMap.merge(flight.destinationAirport.iataCode, 1) { previous, new ->
                        val newValue = previous - new

                        if (newValue == 0)
                            null
                        else
                            newValue
                    }
                    currentCodeVisitMap
                }
            } as (T, Flight) -> T
        }
    }
}
