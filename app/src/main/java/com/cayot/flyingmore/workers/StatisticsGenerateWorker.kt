package com.cayot.flyingmore.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cayot.flyingmore.data.model.Flight
import com.cayot.flyingmore.data.model.statistics.enums.FlyingStatistic
import com.cayot.flyingmore.data.model.statistics.enums.TimeFrame
import com.cayot.flyingmore.data.repository.FlightsRepository
import com.cayot.flyingmore.data.repository.FlyingStatisticsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.Year
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.util.Calendar.DAY_OF_YEAR
import kotlin.math.roundToInt


const val STATISTIC_TO_GENERATE_KEY = "STATISTIC_TO_GENERATE"
const val YEAR_KEY = "YEAR"

//TODO Really Bad
class StatisticsGenerateWorker(
    ctx: Context,
    params: WorkerParameters,
    private val flyingStatisticsRepository: FlyingStatisticsRepository,
    private val flightsRepository: FlightsRepository
) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {
        val statisticToCalculate: FlyingStatistic = FlyingStatistic.entries[inputData.getInt(STATISTIC_TO_GENERATE_KEY, 0)]

        val minDepartureTime: LocalDate
        val maxDepartureTime: LocalDate

        when (statisticToCalculate.dataTimeFrame) {
            TimeFrame.DAY -> throw NotImplementedError()
            TimeFrame.WEEK -> throw NotImplementedError()
            TimeFrame.MONTH -> throw NotImplementedError()
            TimeFrame.YEAR -> {
                val statisticYearInt = inputData.getInt(YEAR_KEY, -1)
                if (statisticYearInt == -1)
                    throw RuntimeException()
                minDepartureTime = Year.of(statisticYearInt).atMonth(1).atDay(1)
                maxDepartureTime = Year.of(statisticYearInt).plusYears(1).atMonth(1).atDay(1)
            }
        }

        return withContext(Dispatchers.IO) {
            try {
                val flightsData = flightsRepository.getAllFlightForDepartureTimeRange(
                    startTime = minDepartureTime.atStartOfDay(ZoneId.from(ZoneOffset.UTC)).toInstant().toEpochMilli(),
                    endTime = maxDepartureTime.atStartOfDay(ZoneId.from(ZoneOffset.UTC)).toInstant().toEpochMilli())

                require(flightsData.isNotEmpty())

                //TODO Extract Aggregator
                val generatedData = when (statisticToCalculate) {
                    FlyingStatistic.NUMBER_OF_FLIGHT -> {
                        generateFlightStatisticData(
                            flightsData = flightsData,
                            sizeToGenerate = ChronoUnit.DAYS.between(minDepartureTime, maxDepartureTime).toInt(),
                            perFlightAggregator = { currentNumber: Int, flight: Flight ->
                                (currentNumber + 1)
                            },
                            initialValueFactory = { 0 }
                        )
                    }
                    FlyingStatistic.FLOWN_DISTANCE -> {
                        generateFlightStatisticData(
                            flightsData = flightsData,
                            sizeToGenerate = ChronoUnit.DAYS.between(minDepartureTime, maxDepartureTime).toInt(),
                            perFlightAggregator = { currentDistance: Int, flight: Flight ->
                                (currentDistance + flight.distance.roundToInt())
                            },
                            initialValueFactory = { 0 }
                        )
                    }
                    FlyingStatistic.AIRPORT_VISIT_NUMBER -> {
                        generateFlightStatisticData(
                            flightsData = flightsData,
                            sizeToGenerate = ChronoUnit.DAYS.between(
                                minDepartureTime,
                                maxDepartureTime
                            ).toInt(),
                            perFlightAggregator = { currentCodeVisitMap: MutableMap<String, Int>, flight: Flight ->
                                currentCodeVisitMap.merge(flight.originAirport.iataCode, 1) { previous, new -> previous + new }
                                currentCodeVisitMap
                            },
                            initialValueFactory = { mutableMapOf() },
                        )
                    }
                }

                //TODO Insert to repository and avoid duplicate sir :P


                Result.success()
            } catch (_: Exception) {
                Result.failure()
            }
        }
    }

    private fun <T> generateFlightStatisticData(
        flightsData: List<Flight>,
        sizeToGenerate: Int,
        perFlightAggregator: (T, Flight) -> T,
        initialValueFactory: () -> T
    ) : List<T> {
        var statisticData: MutableList<T> =
            MutableList(size = sizeToGenerate, init = { initialValueFactory() })

        for (flight in flightsData) {
            statisticData[flight.departureTime.get(DAY_OF_YEAR) - 1] =
                perFlightAggregator(statisticData[flight.departureTime.get(DAY_OF_YEAR) - 1], flight)
        }
        return (statisticData)
    }
}