package com.cayot.flyingmore.data.model.statistics.generator

import com.cayot.flyingmore.data.model.Flight
import com.cayot.flyingmore.data.model.statistics.enums.FlyingStatistic
import junit.framework.TestCase.assertEquals
import org.junit.Test

class FlightStatisticDataGeneratorTest {
    val flightData = listOf<Flight>()

    @Test
    fun flightStatisticDataGeneratorTest_listOfInt() {

        val statisticData = FlightStatisticDataGenerator.generateFlightStatisticData(
            flightsData = flightData,
            sizeToGenerate = 365,
            statisticToGenerate = FlyingStatistic.NUMBER_OF_FLIGHT
        )

        assertEquals(365, statisticData.size)
        assert(statisticData.first() is Int)
    }


    @Test
    fun flightStatisticDataGeneratorTest_listOfMap() {
        val statisticData = FlightStatisticDataGenerator.generateFlightStatisticData(
            flightsData = flightData,
            sizeToGenerate = 365,
            statisticToGenerate = FlyingStatistic.AIRPORT_VISIT_NUMBER
        )

        assertEquals(365, statisticData.size)
        assert(statisticData.first() is Map<*, *>)
    }
}