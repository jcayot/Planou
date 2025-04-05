package com.cayot.flyingmore.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cayot.flyingmore.data.model.statistics.generator.FlightStatisticDataGenerator
import com.cayot.flyingmore.data.repository.FlightRepository
import com.cayot.flyingmore.data.repository.FlyingStatisticsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

const val NEW_FLIGHT_KEY = "NEW_FLIGHT"

class AddFlightToFlyingStatisticsWorker(
    ctx: Context,
    params: WorkerParameters,
    private val flyingStatisticsRepository: FlyingStatisticsRepository,
    private val flightRepository: FlightRepository
) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        val newFlightId = inputData.getInt(NEW_FLIGHT_KEY, -1)

        if (newFlightId == -1)
            return (Result.failure())

        try {
            return withContext(Dispatchers.IO) {
                var newFlight = flightRepository.getFlight(newFlightId).first()

                val statisticsToUpdate = flyingStatisticsRepository.getFlyingStatisticContainingTime(newFlight.departureTime.timeInMillis)

                for (statistic in statisticsToUpdate) {
                    val newData = FlightStatisticDataGenerator.addFlightToStatisticData(
                        newFlight = newFlight,
                        currentData = statistic.data,
                        statisticToGenerate = statistic.statisticType
                    )

                    val newStatistic = statistic.copy(data = newData)

                    flyingStatisticsRepository.updateFlyingStatistic(newStatistic)
                }
                Result.success()
            }
        } catch (_: Exception) {
            return (Result.failure())
        }
    }
}