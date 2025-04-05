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

const val FORMER_FLIGHT_KEY = "FORMER_FLIGHT"

class RemoveFlightFromFlyingStatisticsWorker(
    ctx: Context,
    params: WorkerParameters,
    private val flyingStatisticsRepository: FlyingStatisticsRepository,
    private val flightRepository: FlightRepository
) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        val formerFlightId = inputData.getInt(FORMER_FLIGHT_KEY, -1)

        if (formerFlightId == -1)
            return (Result.failure())

        try {
            return withContext(Dispatchers.IO) {
                val formerFlight = flightRepository.getFlight(formerFlightId).first()

                val statisticsToUpdate = flyingStatisticsRepository.getFlyingStatisticContainingTime(formerFlight.departureTime.timeInMillis)

                for (statistic in statisticsToUpdate) {
                    val newData = FlightStatisticDataGenerator.removeFlightFromStatisticData(
                        formerFlight = formerFlight,
                        currentData = statistic.data,
                        statisticToGenerate = statistic.statisticType
                    )

                    val newStatistic = statistic.copy(data = newData)

                    if (newStatistic.isEmpty())
                        flyingStatisticsRepository.deleteFlyingStatistic(newStatistic)
                    else
                        flyingStatisticsRepository.updateFlyingStatistic(newStatistic)
                }
                Result.success()
            }
        } catch (_: Exception) {
            return (Result.failure())
        }
    }
}