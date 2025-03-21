package com.cayot.flyingmore.data.repository

import com.cayot.flyingmore.data.model.statistics.enums.FlyingStatistic

interface GenerateFlyingStatisticRepository {
    fun generateFlyingStatistics(statisticsToGenerate : List<FlyingStatistic>,
                                 year: Int,
                                 month: Int? = null,
                                 week: Int? = null,
                                 day: Int? = null)
}