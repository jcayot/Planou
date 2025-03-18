package com.cayot.flyingmore.data.repository

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.cayot.flyingmore.data.model.statistics.enums.FlyingStatistic
import com.cayot.flyingmore.workers.GenerateFlyingStatisticsWorker
import com.cayot.flyingmore.workers.STATISTICS_TO_GENERATE_KEY
import com.cayot.flyingmore.workers.YEAR_KEY
import java.time.Year

class WorkManagerGenerateFlyingStatisticRepository(
    private val context: Context,
    private val flyingStatisticsRepository: FlyingStatisticsRepository,
    private val flightsRepository: FlightsRepository
) : GenerateFlyingStatisticRepository{

    private val workManager = WorkManager.getInstance(context)

    override fun generateFlyingStatistics(
        statisticsToGenerate: List<FlyingStatistic>,
        year: Year
    ) {
        val statisticGenerator = OneTimeWorkRequestBuilder<GenerateFlyingStatisticsWorker>()

        statisticGenerator.setInputData(Data.Builder()
            .putIntArray(STATISTICS_TO_GENERATE_KEY, statisticsToGenerate.map { it.ordinal }.toIntArray())
            .putInt(YEAR_KEY, year.value).build()
        )

        workManager.enqueue(statisticGenerator.build())
    }
}