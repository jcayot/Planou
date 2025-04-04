package com.cayot.flyingmore.data.model.statistics.enums

import org.junit.Test
import com.cayot.flyingmore.fake.data.model.generateFakeFlight
import junit.framework.TestCase.assertEquals

class FlyingStatisticTest {

    @Test
    fun flightAggregatorTest_numberOFFlights() {
        val aggregator = FlyingStatistic.flightAggregator<Int>(FlyingStatistic.NUMBER_OF_FLIGHT)
        val flight = generateFakeFlight(1).first()

        assertEquals(1, aggregator(0, flight))
        assertEquals(2, aggregator(1, flight))
        assertEquals(12, aggregator(11, flight))
    }

    @Test
    fun flightAggregatorTest_flownDistance() {
        val aggregator = FlyingStatistic.flightAggregator<Int>(FlyingStatistic.FLOWN_DISTANCE)
        val flight = generateFakeFlight(1).first()

        assertEquals(flight.distance.toInt() / 1000, aggregator(0, flight))
        assertEquals(1000 + (flight.distance.toInt() / 1000), aggregator(1000, flight))
        assertEquals(9000 + (flight.distance.toInt() / 1000), aggregator(9000, flight))
    }

    @Test
    fun flightAggregatorTest_airportVisitNumber() {
        val aggregator = FlyingStatistic.flightAggregator<MutableMap<String, Int>>(FlyingStatistic.AIRPORT_VISIT_NUMBER)
        val flight = generateFakeFlight(1).first()

        assertEquals(1, aggregator(mutableMapOf(), flight)["CDG"])
        assertEquals(2, aggregator(mutableMapOf("CDG" to 1), flight)["CDG"])
        assertEquals(1, aggregator(mutableMapOf("CDG" to 1), flight)["HEL"])
    }
}