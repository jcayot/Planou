package com.cayot.flyingmore.data.model.statistics.enums

import androidx.annotation.StringRes
import com.cayot.flyingmore.R

enum class FlyingStatistic(
    @StringRes val displayNameResource: Int = R.string.statistic_name,
    val dataResolution: Resolution = Resolution.DAILY,
    val defaultDisplayResolution: Resolution = Resolution.DAILY,
    val allowedDisplayResolutions: List<Resolution> = Resolution.entries,
    val dataType: ListDataType = ListDataType.INT,
    val chartType: ChartType = ChartType.BAR_GRAPH,
    @StringRes val unitResource: Int? = null
) {
    ALL,
    FLIGHT_NUMBER(
        displayNameResource = R.string.number_of_flights
    ),
    FLIGHT_DISTANCE(
        displayNameResource = R.string.flown_distance
    ),
    AIRPORT_VISIT_NUMBER(
        displayNameResource = R.string.airports_visits,
        dataType = ListDataType.MAP_STRING_INT
    ),
}
