package com.cayot.flyingmore.data.model.statistics.generator

import com.cayot.flyingmore.data.model.Flight
import com.cayot.flyingmore.data.model.statistics.enums.FlyingStatistic
import java.util.Calendar.DAY_OF_YEAR

object FlightStatisticDataGenerator {
    fun generateFlightStatisticData(
        flightsData: List<Flight>,
        sizeToGenerate: Int,
        statisticToGenerate: FlyingStatistic
    ) : List<Any> {
        var statisticData: MutableList<Any> =
            MutableList(size = sizeToGenerate, init = { FlyingStatistic.initialValueFactory(statisticToGenerate) })
        val perFlightAggregator = FlyingStatistic.flightAggregator(statisticToGenerate)

        for (flight in flightsData) {
            statisticData[flight.departureTime.get(DAY_OF_YEAR) - 1] =
                perFlightAggregator(statisticData[flight.departureTime.get(DAY_OF_YEAR) - 1], flight)
        }
        return (statisticData)
    }
}