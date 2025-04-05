package com.cayot.flyingmore.data.model.statistics.generator

import com.cayot.flyingmore.data.model.Flight
import com.cayot.flyingmore.data.model.statistics.enums.FlyingStatistic
import junit.framework.TestCase.assertEquals
import org.junit.Test

class FlightStatisticDataGeneratorTest {
    val flightData = listOf<Flight>()

    @Test
    fun generateFlightStatisticDataTest_listOfInt() {

        val statisticData = FlightStatisticDataGenerator.generateFlightStatisticData(
            flightsData = flightData,
            sizeToGenerate = 365,
            statisticToGenerate = FlyingStatistic.NUMBER_OF_FLIGHT
        )

        assertEquals(365, statisticData.size)
        assert(statisticData.first() is Int)
    }


    @Test
    fun generateFlightStatisticDataTest_listOfMap() {
        val statisticData = FlightStatisticDataGenerator.generateFlightStatisticData(
            flightsData = flightData,
            sizeToGenerate = 365,
            statisticToGenerate = FlyingStatistic.AIRPORT_VISIT_NUMBER
        )

        assertEquals(365, statisticData.size)
        assert(statisticData.first() is Map<*, *>)
    }

    @Test
    fun addFlightToStatisticData_listOfInt() {
        val statisticData: List<Int> = List(365, { 1 })

        FlightStatisticDataGenerator.addFlightToStatisticData(
            newFlight = Flight.getPlaceholder1(),
            currentData = statisticData,
            statisticToGenerate = FlyingStatistic.NUMBER_OF_FLIGHT
        )
        assertEquals(365, statisticData.size)
        assert(statisticData.contains(2))
    }

    @Test
    fun addFlightToStatisticData_listOfMap() {
        val statisticData: List<Map<String, Int>> = List(365, { mapOf<String, Int>() })

        FlightStatisticDataGenerator.addFlightToStatisticData(
            newFlight = Flight.getPlaceholder1(),
            currentData = statisticData,
            statisticToGenerate = FlyingStatistic.AIRPORT_VISIT_NUMBER
        )
        assertEquals(365, statisticData.size)
        assert(statisticData.contains(mapOf("CDG" to 1, "JFK" to 1)))
    }
}