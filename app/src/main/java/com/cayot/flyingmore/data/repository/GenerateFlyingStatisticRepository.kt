package com.cayot.flyingmore.data.repository

import com.cayot.flyingmore.data.model.statistics.enums.FlyingStatistic
import java.time.Year

interface GenerateFlyingStatisticRepository {
    fun generateFlyingStatistics(statisticsToGenerate : List<FlyingStatistic>, year: Year)
}