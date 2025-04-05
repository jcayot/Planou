package com.cayot.flyingmore.data.model.statistics.generator

import com.cayot.flyingmore.data.model.Flight
import com.cayot.flyingmore.data.model.statistics.enums.FlyingStatistic
import com.cayot.flyingmore.data.model.statistics.enums.ListDataType
import java.util.Calendar.DAY_OF_YEAR

object FlightStatisticDataGenerator {
    fun generateFlightStatisticData(
        flightsData: List<Flight>,
        sizeToGenerate: Int,
        statisticToGenerate: FlyingStatistic
    ) : List<Any> {
        when (statisticToGenerate.dataType) {
            ListDataType.INT -> {
                var statisticData: MutableList<Int> =
                    MutableList(size = sizeToGenerate, init = { 0 })

                val perFlightAggregator = FlyingStatistic.flightAggregator<Int>(statisticToGenerate)
                for (flight in flightsData) {
                    statisticData[flight.departureTime.get(DAY_OF_YEAR) - 1] =
                        perFlightAggregator(statisticData[flight.departureTime.get(DAY_OF_YEAR) - 1], flight)
                }
                return (statisticData)
            }
            ListDataType.MAP_STRING_INT -> {
                var statisticData: MutableList<MutableMap<String, Int>> =
                    MutableList(size = sizeToGenerate, init = { mutableMapOf<String, Int>() })

                val perFlightAggregator = FlyingStatistic.flightAggregator<MutableMap<String, Int>>(statisticToGenerate)

                for (flight in flightsData) {
                    statisticData[flight.departureTime.get(DAY_OF_YEAR) - 1] =
                        perFlightAggregator(statisticData[flight.departureTime.get(DAY_OF_YEAR) - 1], flight)
                }
                return (statisticData)
            }
        }
    }

    fun addFlightToStatisticData(
        newFlight: Flight,
        currentData: List<Any>,
        statisticToGenerate: FlyingStatistic
    ) : List<Any> {
        return when (statisticToGenerate.dataType) {
            ListDataType.INT -> {
                var dataToUpdate = currentData[newFlight.departureTime.get(DAY_OF_YEAR) - 1] as Int
                val perFlightAggregator = FlyingStatistic.flightAggregator<Int>(statisticToGenerate)

                dataToUpdate = perFlightAggregator(dataToUpdate, newFlight)

                val newData = currentData as MutableList<Int>
                newData[newFlight.departureTime.get(DAY_OF_YEAR) - 1] = dataToUpdate
                (newData)
            }
            ListDataType.MAP_STRING_INT -> {
                var dataToUpdate = (currentData[newFlight.departureTime.get(DAY_OF_YEAR) - 1] as Map<String, Int>).toMutableMap()
                val perFlightAggregator = FlyingStatistic.flightAggregator<MutableMap<String, Int>>(statisticToGenerate)

                dataToUpdate = perFlightAggregator(dataToUpdate, newFlight)

                val newData = currentData as MutableList<Map<String, Int>>
                newData[newFlight.departureTime.get(DAY_OF_YEAR) - 1] = dataToUpdate
                (newData)
            }
        }
    }

    fun removeFlightFromStatisticData(
        formerFlight: Flight,
        currentData: List<Any>,
        statisticToGenerate: FlyingStatistic
    ) : List<Any> {
        return when (statisticToGenerate.dataType) {
            ListDataType.INT -> {
                var dataToUpdate =
                    currentData[formerFlight.departureTime.get(DAY_OF_YEAR) - 1] as Int

                val perFlightRemover = FlyingStatistic.flightRemover<Int>(statisticToGenerate)
                dataToUpdate = perFlightRemover(dataToUpdate, formerFlight)

                val newData = currentData as MutableList<Int>
                newData[formerFlight.departureTime.get(DAY_OF_YEAR) - 1] = dataToUpdate
                (newData)
            }
            ListDataType.MAP_STRING_INT -> {
                var dataToUpdate =
                    (currentData[formerFlight.departureTime.get(DAY_OF_YEAR) - 1] as Map<String, Int>).toMutableMap()

                val perFlightRemover = FlyingStatistic.flightRemover<MutableMap<String, Int>>(statisticToGenerate)
                dataToUpdate = perFlightRemover(dataToUpdate, formerFlight)

                val newData = currentData as MutableList<Map<String, Int>>
                newData[formerFlight.departureTime.get(DAY_OF_YEAR) - 1] = dataToUpdate
                (newData)
            }
        }
    }
}