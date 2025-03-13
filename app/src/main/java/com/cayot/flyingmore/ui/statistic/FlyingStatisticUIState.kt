package com.cayot.flyingmore.ui.statistic

import com.cayot.flyingmore.data.model.statistics.MapStringNumberTemporalStatistic
import com.cayot.flyingmore.data.model.statistics.NumberYearTemporalStatistic

data class FlyingStatisticUIState(
    val statisticData: StatisticData
)

sealed interface StatisticData{
    data class Number(val numberYearTemporalStatistic: NumberYearTemporalStatistic) : StatisticData
    data class MapStringNumber(val mapStringNumberTemporalStatistic: MapStringNumberTemporalStatistic): StatisticData
}