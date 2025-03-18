package com.cayot.flyingmore.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cayot.flyingmore.data.model.statistics.DailyTemporalStatistic
import com.cayot.flyingmore.data.model.statistics.enums.FlyingStatistic
import com.cayot.flyingmore.data.model.statistics.enums.TimeFrame
import com.cayot.flyingmore.data.model.statistics.generator.FlightStatisticDataGenerator.generateFlightStatisticData
import com.cayot.flyingmore.data.repository.FlightsRepository
import com.cayot.flyingmore.data.repository.FlyingStatisticsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.Year
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit


const val STATISTICS_TO_GENERATE_KEY = "STATISTICS_TO_GENERATE"
const val YEAR_KEY = "YEAR"

//TODO Really Bad
class StatisticsGenerateWorker(
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
        val minDepartureTime: LocalDate
        val maxDepartureTime: LocalDate

        //TODO Extract
        when (dataTimeFrame) {
            TimeFrame.DAY -> throw NotImplementedError()
            TimeFrame.WEEK -> throw NotImplementedError()
            TimeFrame.MONTH -> throw NotImplementedError()
            TimeFrame.YEAR -> {
                val statisticYearInt = inputData.getInt(YEAR_KEY, -1)
                if (statisticYearInt == -1)
                    return Result.failure()
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

                for (statisticToGenerate: FlyingStatistic in statisticsToGenerateList) {
                    val generatedData = generateFlightStatisticData(
                        flightsData = flightsData,
                        sizeToGenerate = ChronoUnit.DAYS.between(minDepartureTime, maxDepartureTime).toInt(),
                        statisticToGenerate = statisticToGenerate
                    )

                    val currentStatistic = flyingStatisticsRepository.getFlyingStatistic(
                        statisticTypeInt = statisticToGenerate.ordinal,
                        timeFrameStart = minDepartureTime,
                        timeFrameEnd = maxDepartureTime
                    )

                    val statisticToAdd = currentStatistic?.copy(data = generatedData)
                        ?: DailyTemporalStatistic.makeDailyTemporalStatistic(
                            timeFrameStart = minDepartureTime,
                            timeFrameEnd = maxDepartureTime,
                            data = generatedData,
                            statisticType = statisticToGenerate
                        )
                    flyingStatisticsRepository.insertFlyingStatistic(statisticToAdd)
                }

                Result.success()
            } catch (_: Exception) {
                Result.failure()
            }
        }
    }
}