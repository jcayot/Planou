package com.cayot.flyingmore.data.model.statistics.enums

import org.junit.Test
import com.cayot.flyingmore.fake.data.model.generateFakeFlight
import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertThrows

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

    @Test
    fun flightRemoverTest_numberOFFlights() {
        val remover = FlyingStatistic.flightRemover<Int>(FlyingStatistic.NUMBER_OF_FLIGHT)
        val flight = generateFakeFlight(1).first()

        assertEquals(0, remover(1, flight))
        assertEquals(1, remover(2, flight))
        assertEquals(11, remover(12, flight))
        assertThrows(IllegalStateException::class.java) {
            remover(0, flight)
        }
    }

    @Test
    fun flightRemoverTest_flownDistance() {
        val remover = FlyingStatistic.flightRemover<Int>(FlyingStatistic.FLOWN_DISTANCE)
        val flight = generateFakeFlight(1).first()

        assertEquals(0, remover(flight.distance.toInt() / 1000, flight))
        assertEquals(1000 - (flight.distance.toInt() / 1000), remover(1000, flight))
        assertEquals(9000 - (flight.distance.toInt() / 1000), remover(9000, flight))
        assertThrows(IllegalStateException::class.java) {
            remover(0, flight)
        }
    }

    @Test
    fun flightRemoverTest_airportVisitNumber() {
        val remover = FlyingStatistic.flightRemover<MutableMap<String, Int>>(FlyingStatistic.AIRPORT_VISIT_NUMBER)
        val flight = generateFakeFlight(1).first()

        assertEquals(emptyMap<String, Int>(), remover(mutableMapOf("CDG" to 1, "HEL" to 1), flight))
        assertEquals(mapOf("CDG" to 1), remover(mutableMapOf("CDG" to 2, "HEL" to 1), flight))
        assertThrows(IllegalStateException::class.java) {
            remover(emptyMap<String, Int>().toMutableMap(), flight)
        }
    }
}