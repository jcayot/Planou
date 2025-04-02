package com.cayot.flyingmore.ui.statistic

import com.cayot.flyingmore.data.model.statistics.MapStringNumberDailyTemporalStatistic
import com.cayot.flyingmore.data.model.statistics.NumberDailyTemporalStatistic
import com.cayot.flyingmore.data.model.statistics.enums.FlyingStatistic
import com.cayot.flyingmore.data.model.statistics.enums.TimeFrame

data class FlyingStatisticUIState(
    val statisticData: StatisticData? = null,
    val displayResolution: TimeFrame = TimeFrame.MONTH,
)

sealed interface StatisticData{
    data class Number(val numberYearTemporalStatistic: NumberDailyTemporalStatistic) : StatisticData
    data class MapStringNumber(val mapStringNumberTemporalStatistic: MapStringNumberDailyTemporalStatistic): StatisticData
}

fun FlyingStatisticUIState.getStatisticType() : FlyingStatistic {
    if (statisticData == null)
        throw IllegalStateException("No statistic data")

    return when (statisticData) {
        is StatisticData.MapStringNumber -> statisticData.mapStringNumberTemporalStatistic.statisticType
        is StatisticData.Number -> statisticData.numberYearTemporalStatistic.statisticType
    }
}