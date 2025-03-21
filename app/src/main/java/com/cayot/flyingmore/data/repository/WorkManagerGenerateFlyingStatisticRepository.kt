package com.cayot.flyingmore.data.repository

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.cayot.flyingmore.data.model.statistics.enums.FlyingStatistic
import com.cayot.flyingmore.data.model.statistics.enums.TimeFrame
import com.cayot.flyingmore.workers.DAY_KEY
import com.cayot.flyingmore.workers.GenerateFlyingStatisticsWorker
import com.cayot.flyingmore.workers.MONTH_KEY
import com.cayot.flyingmore.workers.STATISTICS_TO_GENERATE_KEY
import com.cayot.flyingmore.workers.WEEK_KEY
import com.cayot.flyingmore.workers.YEAR_KEY

class WorkManagerGenerateFlyingStatisticRepository(context: Context) : GenerateFlyingStatisticRepository {

    private val workManager = WorkManager.getInstance(context)

    override fun generateFlyingStatistics(
        statisticsToGenerate: List<FlyingStatistic>,
        year: Int,
        month: Int?,
        week: Int?,
        day: Int?
    ) {
        for (statisticToGenerate: FlyingStatistic in statisticsToGenerate) {
            if (statisticToGenerate.dataTimeFrame != statisticsToGenerate[0].dataTimeFrame)
                throw IllegalArgumentException("Data time frame must be identical for all statistics to generate")
        }

        val statisticGenerator = OneTimeWorkRequestBuilder<GenerateFlyingStatisticsWorker>()

        statisticGenerator.setInputData(buildInputData(
            statisticsToGenerate = statisticsToGenerate,
            year = year,
            month = month,
            week = week,
            day = day
        ))

        workManager.enqueue(statisticGenerator.build())
    }

    private fun buildInputData(statisticsToGenerate: List<FlyingStatistic>,
                       year: Int,
                       month: Int?,
                       week: Int?,
                       day: Int?
    ) : Data {
        //Base data for all statistic
        val data = Data.Builder()
            .putIntArray(STATISTICS_TO_GENERATE_KEY, statisticsToGenerate.map { it.ordinal }.toIntArray())
            .putInt(YEAR_KEY, year)

        //Weekly statistic building
        if (week != null) {
            if (statisticsToGenerate[0].dataTimeFrame != TimeFrame.WEEK)
                throw IllegalArgumentException("Week specified for non-weekly time frame")
            if (month != null || day != null)
                throw IllegalArgumentException("Weekly time frame shouldn't have month or day specified")
            data.putInt(WEEK_KEY, week)
            return (data.build())
        }

        //Other time frame building
        if (month != null) {
            if (statisticsToGenerate[0].dataTimeFrame > TimeFrame.MONTH)
                throw IllegalArgumentException("Month specified for bigger time frame")
            data.putInt(MONTH_KEY, month)
        }
        if (day != null) {
            if (statisticsToGenerate[0].dataTimeFrame > TimeFrame.DAY)
                throw IllegalArgumentException("Day specified for bigger time frame")
            data.putInt(DAY_KEY, day)
        }
        return(data.build())
    }
}