package com.cayot.flyingmore.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cayot.flyingmore.data.model.statistics.DailyTemporalStatistic
import com.cayot.flyingmore.data.model.statistics.enums.FlyingStatistic
import com.cayot.flyingmore.data.model.statistics.generator.FlightStatisticDataGenerator.generateFlightStatisticData
import com.cayot.flyingmore.data.repository.FlightsRepository
import com.cayot.flyingmore.data.repository.FlyingStatisticsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit


const val STATISTICS_TO_GENERATE_KEY = "STATISTICS_TO_GENERATE"
const val YEAR_KEY = "YEAR"
const val MONTH_KEY = "MONTH"
const val WEEK_KEY = "WEEK"
const val DAY_KEY = "DAY"

//TODO Really Bad
class GenerateFlyingStatisticsWorker(
    ctx: Context,
    params: WorkerParameters,
    private val flyingStatisticsRepository: FlyingStatisticsRepository,
    private val flightsRepository: FlightsRepository
) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {
        val statisticsToGenerateIntArray = inputData.getIntArray(STATISTICS_TO_GENERATE_KEY) ?: return Result.failure()
        val statisticsToGenerateList: List<FlyingStatistic> = statisticsToGenerateIntArray.map { FlyingStatistic.entries[it] }
        val dataTimeFrame = statisticsToGenerateList[0].dataTimeFrame

        for (statisticToGenerate: FlyingStatistic in statisticsToGenerateList) {
            if (statisticToGenerate.dataTimeFrame != dataTimeFrame)
                return Result.failure()
        }

        try {
            val dateTimeRange = getDateRange(inputData, dataTimeFrame)

            return withContext(Dispatchers.IO) {
                val flightsData = flightsRepository.getAllFlightForDepartureTimeRange(
                    startTime = dateTimeRange.first.atStartOfDay(ZoneId.from(ZoneOffset.UTC)).toInstant().toEpochMilli(),
                    endTime = dateTimeRange.second.atStartOfDay(ZoneId.from(ZoneOffset.UTC)).toInstant().toEpochMilli()
                )

                require(flightsData.isNotEmpty())

                for (statisticToGenerate: FlyingStatistic in statisticsToGenerateList) {
                    val generatedData = generateFlightStatisticData(
                        flightsData = flightsData,
                        sizeToGenerate = ChronoUnit.DAYS.between(dateTimeRange.first, dateTimeRange.second).toInt(),
                        statisticToGenerate = statisticToGenerate
                    )

                    val statisticToAdd = DailyTemporalStatistic.makeDailyTemporalStatistic(
                        timeFrameStart = dateTimeRange.first,
                        timeFrameEnd = dateTimeRange.second,
                        data = generatedData,
                        statisticType = statisticToGenerate
                    )

                    flyingStatisticsRepository.insertFlyingStatistic(statisticToAdd)
                }

                Result.success()
            }

        } catch (e: Exception) {
            e.printStackTrace() //TODO Remove
            return (Result.failure())
        }
    }
}