package com.cayot.flyingmore.data.model.statistics.enums

import androidx.annotation.StringRes
import com.cayot.flyingmore.R
import com.cayot.flyingmore.data.model.Flight
import kotlin.math.roundToInt

enum class FlyingStatistic(
    @StringRes val displayNameResource: Int = R.string.statistic_name,
    val dataTimeFrame: TimeFrame = TimeFrame.YEAR,
    val dataResolution: TimeFrame = TimeFrame.DAY,
    val defaultDisplayResolution: TimeFrame = TimeFrame.DAY,
    val allowedDisplayResolutions: List<TimeFrame> = TimeFrame.entries,
    val dataType: ListDataType = ListDataType.INT,
    val chartType: ChartType = ChartType.BAR_GRAPH,
    @StringRes val unitResource: Int? = null
) {
    NUMBER_OF_FLIGHT(
        displayNameResource = R.string.number_of_flights
    ),
    FLOWN_DISTANCE(
        displayNameResource = R.string.flown_distance
    ),
    AIRPORT_VISIT_NUMBER(
        displayNameResource = R.string.airports_visits,
        dataType = ListDataType.MAP_STRING_INT
    );

    companion object {
        fun flightAggregator(flyingStatistic: FlyingStatistic) : (Any, Flight) -> Any {
            return when (flyingStatistic) {
                NUMBER_OF_FLIGHT -> { currentNumber: Any, flight: Flight ->
                    (currentNumber as Int + 1)
                }
                FLOWN_DISTANCE -> { currentDistance: Any, flight: Flight ->
                    (currentDistance as Int + flight.distance.roundToInt())
                }
                AIRPORT_VISIT_NUMBER -> { currentCodeVisitMap: Any, flight: Flight ->
                    (currentCodeVisitMap as MutableMap<String, Int>)
                        .merge(flight.originAirport.iataCode, 1) { previous, new -> previous + new }
                    currentCodeVisitMap
                }
            }
        }

        fun initialValueFactory(flyingStatistic: FlyingStatistic) : () -> Any {
            return {
                when (flyingStatistic) {
                    NUMBER_OF_FLIGHT -> { 0 }
                    FLOWN_DISTANCE -> { 0 }
                    AIRPORT_VISIT_NUMBER -> { mutableMapOf<String, Int>() }
                }
            }
        }
    }
}
