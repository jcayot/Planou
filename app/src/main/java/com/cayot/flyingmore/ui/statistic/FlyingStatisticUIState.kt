package com.cayot.flyingmore.ui.statistic

import com.cayot.flyingmore.data.model.statistics.MapStringNumberDailyTemporalStatistic
import com.cayot.flyingmore.data.model.statistics.NumberDailyTemporalStatistic

data class FlyingStatisticUIState(
    val statisticData: StatisticData
)

sealed interface StatisticData{
    data class Number(val numberYearTemporalStatistic: NumberDailyTemporalStatistic) : StatisticData
    data class MapStringNumber(val mapStringNumberTemporalStatistic: MapStringNumberDailyTemporalStatistic): StatisticData
}