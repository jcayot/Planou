package com.cayot.flyingmore.data.model

import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import java.io.InvalidObjectException
import java.util.Calendar

class FlightTest {

    private val testFlight = Flight.getPlaceholder1()

    @Test
    fun getDepartureArrivalDayDifference_testSameDay() {
        assertEquals(0, testFlight.getDepartureArrivalDayDifference())
    }

    @Test
    fun getDepartureArrivalDayDifference_oneDay() {
        val flightWithOneDay = Flight.getPlaceholder1().copy(
            departureTime = Calendar.getInstance().apply {
                set(2024, 10, 1)
            },
            arrivalTime = Calendar.getInstance().apply {
                set(2024, 10, 2)
            }
        )
        assertEquals(1, flightWithOneDay.getDepartureArrivalDayDifference())
    }

    @Test
    fun getDepartureArrivalDayDifference_longDifference() {
        val flightWithLongTime = Flight.getPlaceholder1().copy(
            departureTime = Calendar.getInstance().apply {
                set(2023, 11, 10)
            },
            arrivalTime = Calendar.getInstance().apply {
                set(2024, 6, 27)
            }
        )
        assertEquals(230, flightWithLongTime.getDepartureArrivalDayDifference())
    }

    @Test
    fun getDepartureArrivalDayDifference_throwOnNegativeValue() {
        val flightWithNegative = Flight.getPlaceholder1().copy(
            departureTime = Calendar.getInstance().apply {
                set(2024, 10, 3)
            },
            arrivalTime = Calendar.getInstance().apply {
                set(2024, 10, 2)
            }
        )
        assertThrows(InvalidObjectException::class.java) {
            flightWithNegative.getDepartureArrivalDayDifference()
        }
    }
}